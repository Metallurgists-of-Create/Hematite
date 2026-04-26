package dev.metallurgists.hematite.mixin;

import dev.metallurgists.hematite.api.weathering.block_growths.events.LightningStruckBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin extends Entity {
    @Shadow
    protected abstract BlockPos getStrikePosition();

    protected LightningBoltMixin(EntityType<?> type, Level world) {
        super(type, world);
    }


    @Inject(method = "powerLightningRod", at = @At("HEAD"))
    private void powerLightningRod(CallbackInfo ci) {
        BlockPos blockPos = this.getStrikePosition();
        BlockState blockState = this.level().getBlockState(blockPos);
        NeoForge.EVENT_BUS.post(new LightningStruckBlockEvent(blockState, level(), blockPos, (LightningBolt) (Object) this));
    }
}
