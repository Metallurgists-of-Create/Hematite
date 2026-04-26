package dev.metallurgists.hematite.common.position_test.logic;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.metallurgists.hematite.api.position_test.PositionTest;
import dev.metallurgists.hematite.api.position_test.PositionTestType;
import dev.metallurgists.hematite.registry.HematitePositionTestTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.function.Supplier;

public record OrTest(List<PositionTest> predicates) implements PositionTest {

    public static final MapCodec<OrTest> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PositionTest.CODEC.listOf().fieldOf("predicates").forGetter(OrTest::predicates)
    ).apply(instance, OrTest::new));

    @Override
    public boolean test(Supplier<Holder<Biome>> biome, BlockPos pos, Level level) {
        for(var p : predicates){
            if(p.test(biome,pos, level))return true;
        }
        return false;
    }

    @Override
    public PositionTestType<?> getType() {
        return HematitePositionTestTypes.OR.get();
    }


}
