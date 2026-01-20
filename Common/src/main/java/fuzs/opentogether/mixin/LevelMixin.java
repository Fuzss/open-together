package fuzs.opentogether.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.opentogether.OpenTogether;
import fuzs.opentogether.util.DoubleBlockLogic;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Level.class)
abstract class LevelMixin {

    @ModifyArg(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z"),
               index = 3)
    public int setBlock(int recursionLeft, @Local(argsOnly = true) BlockState blockState) {
        for (DoubleBlockLogic doubleBlockLogic : OpenTogether.DOUBLE_BLOCK_LOGIC) {
            recursionLeft = Math.min(recursionLeft,
                    doubleBlockLogic.getRecursionLeft(Level.class.cast(this), blockState));
        }

        return recursionLeft;
    }
}
