package dev.metallurgists.hematite.api.recipe.ingredient;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import dev.metallurgists.hematite.HematiteRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BlockIngredient implements Predicate<BlockState> {
    private static final MapCodec<BlockIngredient> SINGLE_OR_TAG_CODEC = MapCodec.recursive("BlockIngredient.SINGLE_OR_TAG_CODEC", (self) -> singleOrTagCodec());
    public static final MapCodec<BlockIngredient> MAP_CODEC_NONEMPTY = makeMapCodec();
    private static final Codec<BlockIngredient> MAP_CODEC_CODEC;
    public static final Codec<List<BlockIngredient>> LIST_CODEC;
    public static final Codec<List<BlockIngredient>> LIST_CODEC_NON_EMPTY;
    public static final Codec<BlockIngredient> CODEC;
    public static final Codec<BlockIngredient> CODEC_NON_EMPTY;
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> STREAM_CODEC;
    private @Nullable BlockState[] states;

    public BlockIngredient() {
    }

    public final BlockState[] getStates() {
        if (this.states == null) {
            this.states = (this.generateStates().collect(Collectors.toCollection(BlockStateLinkedSet::createTypeAndComponentsSet))).toArray(BlockState[]::new);
        }

        return this.states;
    }

    public abstract boolean test(BlockState state);

    protected abstract Stream<BlockState> generateStates();

    public abstract boolean isSimple();

    public abstract BlockIngredientType<?> getType();

    public final boolean isEmpty() {
        return this == empty();
    }

    public final boolean hasNoBlocks() {
        return this.getStates().length == 0;
    }

    public abstract int hashCode();

    public abstract boolean equals(Object var1);

    public static BlockIngredient empty() {
        return EmptyBlockIngredient.INSTANCE;
    }

    public static BlockIngredient of() {
        return empty();
    }

    public static BlockIngredient of(BlockState... states) {
        return ofStream(Arrays.stream(states).map(BlockState::getBlock));
    }

    public static BlockIngredient of(Block... blocks) {
        return ofStream(Arrays.stream(blocks));
    }

    public static BlockIngredient ofStream(Stream<Block> blocks) {
        return CompoundBlockIngredient.of(blocks.map(BlockIngredient::single));
    }

    public static BlockIngredient single(BlockState state) {
        return single(state.getBlock());
    }

    public static BlockIngredient single(Block block) {
        return single(block.builtInRegistryHolder());
    }

    public static BlockIngredient single(Holder<Block> holder) {
        return new SingleBlockIngredient(holder);
    }

    public static BlockIngredient tag(TagKey<Block> tag) {
        return new TagBlockIngredient(tag);
    }

    private static MapCodec<BlockIngredient> singleOrTagCodec() {
        return NeoForgeExtraCodecs.xor(SingleBlockIngredient.CODEC, TagBlockIngredient.CODEC).xmap((either) -> (BlockIngredient)either.map((id) -> id, (id) -> id), (ingredient) -> {
            if (ingredient instanceof SingleBlockIngredient block) {
                return Either.left(block);
            } else if (ingredient instanceof TagBlockIngredient tag) {
                return Either.right(tag);
            } else {
                throw new IllegalStateException("Basic block ingredient should be either a block or a tag!");
            }
        });
    }

    private static MapCodec<BlockIngredient> makeMapCodec() {
        return NeoForgeExtraCodecs.<BlockIngredientType<?>, BlockIngredient, BlockIngredient>dispatchMapOrElse(HematiteRegistries.BLOCK_INGREDIENT_TYPE_REGISTRY.byNameCodec(), BlockIngredient::getType, BlockIngredientType::codec, SINGLE_OR_TAG_CODEC)
                .xmap((either) ->
                                either.map((id) -> id, (id) -> id),
                        (ingredient) ->
                                !(ingredient instanceof SingleBlockIngredient) && !(ingredient instanceof TagBlockIngredient) ? Either.left(ingredient) : Either.right(ingredient))
                .validate((ingredient) ->
                        ingredient.isEmpty() ? DataResult.error(() -> "Cannot serialize empty block ingredient using the map codec") : DataResult.success(ingredient));
    }

    private static Codec<BlockIngredient> codec(boolean allowEmpty) {
        Codec<List<BlockIngredient>> listCodec = Codec.lazyInitialized(() -> allowEmpty ? LIST_CODEC : LIST_CODEC_NON_EMPTY);
        return Codec.either(listCodec, MAP_CODEC_CODEC).xmap((either) -> either.map(CompoundBlockIngredient::of, (i) -> i), (ingredient) -> {
            if (ingredient instanceof CompoundBlockIngredient compound) {
                return Either.left(compound.children());
            } else {
                return ingredient.isEmpty() ? Either.left(List.of()) : Either.right(ingredient);
            }
        });
    }

    static {
        MAP_CODEC_CODEC = MAP_CODEC_NONEMPTY.codec();
        LIST_CODEC = MAP_CODEC_CODEC.listOf();
        LIST_CODEC_NON_EMPTY = LIST_CODEC.validate((list) -> list.isEmpty() ? DataResult.error(() -> "Block ingredient cannot be empty, at least one item must be defined") : DataResult.success(list));
        CODEC = codec(true);
        CODEC_NON_EMPTY = codec(false);
        STREAM_CODEC = new StreamCodec<>() {
            private static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> DISPATCH_CODEC;
            private static final StreamCodec<ByteBuf, Collection<BlockState>> BLOCK_LIST_CODEC;

            public void encode(RegistryFriendlyByteBuf buf, BlockIngredient ingredient) {
                if (ingredient.isSimple()) {
                    BLOCK_LIST_CODEC.encode(buf, Arrays.asList(ingredient.getStates()));
                } else {
                    buf.writeVarInt(-1);
                    DISPATCH_CODEC.encode(buf, ingredient);
                }

            }

            public BlockIngredient decode(RegistryFriendlyByteBuf buf) {
                int size = buf.readVarInt();
                return size == -1 ? DISPATCH_CODEC.decode(buf) : CompoundBlockIngredient.of(Stream.generate(() -> ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).decode(buf)).limit(size).map(BlockIngredient::single));
            }

            static {
                DISPATCH_CODEC = ByteBufCodecs.registry(HematiteRegistries.BLOCK_INGREDIENT_TYPE).dispatch(BlockIngredient::getType, BlockIngredientType::streamCodec);
                BLOCK_LIST_CODEC = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY).apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));
            }
        };
    }
}
