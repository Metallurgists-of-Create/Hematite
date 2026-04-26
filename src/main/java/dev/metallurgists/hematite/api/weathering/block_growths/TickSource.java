package dev.metallurgists.hematite.api.weathering.block_growths;

import com.mojang.serialization.Codec;
import dev.metallurgists.hematite.HematiteRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.tags.TagKey;

import java.util.function.Predicate;

public class TickSource {
    private final Holder.Reference<TickSource> builtInRegistryHolder;

    public TickSource() {
        this.builtInRegistryHolder = HematiteRegistries.TICK_SOURCE_REGISTRY.createIntrusiveHolder(this);
    }

    public static final Codec<TickSource> CODEC = HematiteRegistries.TICK_SOURCE_REGISTRY.byNameCodec();
    public static final Codec<Holder<TickSource>> HOLDER_CODEC = HematiteRegistries.TICK_SOURCE_REGISTRY.holderByNameCodec();

    public static final Codec<HolderSet<TickSource>> LIST_CODEC = RegistryCodecs.homogeneousList(HematiteRegistries.TICK_SOURCE);

    public boolean is(TagKey<TickSource> tag) {
        return this.builtInRegistryHolder.is(tag);
    }

    public boolean is(TickSource source) {
        return this == source;
    }

    public boolean is(Predicate<Holder<TickSource>> predicate) {
        return predicate.test(this.builtInRegistryHolder);
    }
}
