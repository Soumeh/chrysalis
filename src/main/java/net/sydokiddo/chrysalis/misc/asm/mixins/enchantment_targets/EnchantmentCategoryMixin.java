package net.sydokiddo.chrysalis.misc.asm.mixins.enchantment_targets;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("ALL")
@Mixin(EnchantmentCategory.class)
public abstract class EnchantmentCategoryMixin {

    @Shadow public abstract boolean canEnchant(Item item);
}