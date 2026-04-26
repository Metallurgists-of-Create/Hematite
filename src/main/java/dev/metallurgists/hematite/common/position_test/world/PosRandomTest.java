package dev.metallurgists.hematite.common.position_test.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Random;
import java.util.function.Supplier;

public record PosRandomTest(int rarity)  implements PositionTest {

    public static final MapCodec<PosRandomTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(0, 10000).fieldOf("rarity").forGetter(PosRandomTest::rarity)
    ).apply(instance, PosRandomTest::new));

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        Random posRandom = new Random(Mth.getSeed(pos));
        return posRandom.nextInt(rarity) == 0;
    }

    @Override
    public PositionTestType<?> getType() {
        return HematitePositionTestTypes.POS_RANDOM.get();
    }
}
