package galysso.codicraft.loottimer.mixin;

import galysso.codicraft.loottimer.util.LootTimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {
    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    private static void onCanInsert(Inventory inventory, ItemStack stack, int slot, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (stack.contains(LootTimerUtil.getBoundsToPlayerComponent())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "canExtract", at = @At("HEAD"), cancellable = true)
    private static void onCanExtract(Inventory hopperInventory, Inventory fromInventory, ItemStack stack, int slot, Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (stack.contains(LootTimerUtil.getBoundsToPlayerComponent())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
