package galysso.codicraft.loottimer.mixin;

import galysso.codicraft.loottimer.access.LootableContainerBlockEntityAccessor;
import galysso.codicraft.loottimer.util.LootTimerUtil;
import galysso.codicraft.loottimer.util.ServerUtil;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.block.entity.LootableContainerBlockEntity.class)
public class LootableContainerBlockEntityMixin implements LootableContainerBlockEntityAccessor {
    private final static long maxTicks = 20*60*5;
    //private final static long maxTicks = 20*10;
    private long generationDate = -1;
    private Boolean isOnClock = false;
    private Boolean isNormalChest = false;

    @Shadow
    public RegistryKey<LootTable> getLootTable() {return null;}

    @Shadow
    public void setStack(int slot, ItemStack stack) {}

    @Unique
    private Boolean isGenerated() {
        return this.generationDate != -1;
    }

    @Unique
    private void checkInventory() {
        if (isNormalChest) {
            return;
        }
        if (!isOnClock && getLootTable() == null) {
            isNormalChest = true;
            return;
        }
        isOnClock = true;
        if (isGenerated()) {
            if (ServerUtil.getServerTicks() > generationDate + maxTicks) {
                clearInventory();
            }
        } else {
            if (getLootTable() != null) {
                generationDate = ServerUtil.getServerTicks();
            }
        }
    }

    @Inject(method = "isEmpty", at = @At("HEAD"))
    public void isEmpty(CallbackInfoReturnable<Boolean> cir) {
        checkInventory();
    }

    @Inject(method = "isEmpty", at = @At("RETURN"))
    public void isEmptyAtReturn(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            isOnClock = false;
            isNormalChest = true;
        }
    }

    @Inject(method = "getStack", at = @At("HEAD"))
    public void getStack(int slot, CallbackInfoReturnable<ItemStack> cir) {
        checkInventory();
    }

    @Inject(method = "removeStack*", at = @At("HEAD"))
    public void removeStack(CallbackInfoReturnable<ScreenHandler> cir) {
        checkInventory();
    }

    @Inject(method = "setStack", at = @At("HEAD"))
    public void setStack(int slot, ItemStack stack, CallbackInfo ci) {
        checkInventory();
    }

    @Inject(method = "createMenu", at = @At("HEAD"))
    public void createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity, CallbackInfoReturnable<ScreenHandler> cir) {
        checkInventory();
    }

    @Unique
    private void clearInventory() {
        isNormalChest = true;
        if (this instanceof net.minecraft.inventory.Inventory inventory) {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.getStack(i).contains(LootTimerUtil.getBoundsToPlayerComponent())) {
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    public Boolean isOnClock() {
        return isOnClock;
    }

    @Override
    public long getRemainingTicks() {
        if (isNormalChest) {
            return 0;
        }
        if (isGenerated()) {
            return generationDate + maxTicks - ServerUtil.getServerTicks();
        } else {
            return 0;
        }
    }
}
