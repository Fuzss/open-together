package fuzs.opentogether.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fuzs.opentogether.common.OpenTogether;
import fuzs.opentogether.common.world.level.block.DoubleBlockLogic;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelWriter.class)
interface LevelMixin {

    @ModifyArg(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/world/level/LevelWriter;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z"),
               index = 3)
    default int setBlock(int recursionLeft, @Local(argsOnly = true) BlockState blockState) {
        if (!(this instanceof Level level)) {
            return recursionLeft;
        }

        for (DoubleBlockLogic doubleBlockLogic : OpenTogether.DOUBLE_BLOCK_LOGIC) {
            recursionLeft = Math.min(recursionLeft,
                    doubleBlockLogic.getRecursionLeft(level, blockState));
        }

        return recursionLeft;
    }
}
