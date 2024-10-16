package net.sydokiddo.chrysalis.mixin.entities.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private BlockHitResult getPlayerPOVHitResult() {

        Vec3 eyePosition = this.getEyePosition();

        float xCos = -Mth.cos(-this.getXRot() * 0.017453292F);
        float xSin = Mth.sin(-this.getXRot() * 0.017453292F);

        float yCos = Mth.cos(-this.getYRot() * 0.017453292F - 3.1415927F);
        float ySin = Mth.sin(-this.getYRot() * 0.017453292F - 3.1415927F);

        float sinTimesCos = ySin * xCos;
        float cosTimesCos = yCos * xCos;

        Vec3 vec3 = eyePosition.add((double) sinTimesCos * 5.0, (double) xSin * 5.0, (double) cosTimesCos * 5.0);
        return this.level().clip(new ClipContext(eyePosition, vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this));
    }

    @Inject(method = "isSecondaryUseActive", at = @At(value = "RETURN"), cancellable = true)
    private void chrysalis$allowBlockUseWhileSneaking(CallbackInfoReturnable<Boolean> cir) {
        BlockHitResult blockHitResult = this.getPlayerPOVHitResult();
        if (this.getMainHandItem().isEmpty() && this.level().getBlockState(blockHitResult.getBlockPos()).is(ChrysalisTags.ALLOWS_USE_WHILE_SNEAKING)) cir.setReturnValue(false);
    }

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"))
    private void chrysalis$setDeathItemsToNeverDespawn(Inventory inventory) {
        if (this.level().getGameRules().getBoolean(ChrysalisRegistry.RULE_PLAYER_DEATH_ITEM_DESPAWNING)) {
            inventory.dropAll();
        } else {
            inventory.compartments.forEach((getInventory) -> getInventory.forEach(itemStack -> {

                ItemEntity droppedItem = inventory.player.drop(itemStack, true, false);
                inventory.removeItem(itemStack);

                CompoundTag compoundTag = new CompoundTag();

                if (droppedItem != null) {
                    droppedItem.addAdditionalSaveData(compoundTag);
                    if (inventory.player.isDeadOrDying()) compoundTag.putInt("Age", -32768);
                    droppedItem.readAdditionalSaveData(compoundTag);
                }
            }));
        }
    }
}