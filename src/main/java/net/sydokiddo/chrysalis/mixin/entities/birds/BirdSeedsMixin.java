package net.sydokiddo.chrysalis.mixin.entities.birds;

import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.ItemStack;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Chicken.class, Parrot.class})
public abstract class BirdSeedsMixin {

    // Un-hard-codes the Seed items that Chickens and Parrots can eat and makes it into a tag

    @Inject(at = @At("HEAD"), method = "isFood", cancellable = true)
    public void chrysalis_seedItemsTag(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(itemStack.is(ChrysalisTags.SEEDS));
    }
}
