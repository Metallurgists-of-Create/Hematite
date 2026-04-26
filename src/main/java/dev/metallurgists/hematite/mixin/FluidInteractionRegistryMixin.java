package dev.metallurgists.hematite.mixin;

import dev.metallurgists.hematite.api.weathering.fluid_generators.data.FluidGeneratorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidInteractionRegistry.class)
public class FluidInteractionRegistryMixin {

    @Inject(method = "canInteract", at = @At("HEAD"), cancellable = true, remap = false)
    private static void dataFluidInteraction(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof LiquidBlock) {
            var f = ((LiquidBlock) state.getBlock()).fluid;
            boolean lava = state.getFluidState().is(FluidTags.LAVA);
            var successPos = FluidGeneratorHandler.applyGenerators(f, FluidGeneratorHandler.POSSIBLE_FLOW_DIRECTIONS, pos, level);
            if (successPos.isPresent()) {
                if (lava) {
                    level.levelEvent(1501, pos, 0);
                }
                cir.setReturnValue(false);
            }
        }
    }
}
