package net.sydokiddo.chrysalis.misc.util.helpers;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@SuppressWarnings("unused")
public class ItemHelper {

    /**
     * Checks to see if a block entity as an item is empty when in the player's inventory.
     **/

    public static boolean containerIsEmpty(ItemStack itemStack) {
        ItemContainerContents component = itemStack.get(DataComponents.CONTAINER);
        if (component == null) return true;
        return component.nonEmptyStream().toList().isEmpty();
    }

    /**
     * Checks for a specific item in the player's inventory.
     **/

    public static boolean hasItemInInventory(Item item, Player player) {
        for (int slots = 0; slots <= 35; slots++) {
            if (player.getInventory().getItem(slots).is(item) || player.getOffhandItem().is(item) || player.getItemBySlot(EquipmentSlot.HEAD).is(item) ||
            player.getItemBySlot(EquipmentSlot.CHEST).is(item) || player.getItemBySlot(EquipmentSlot.LEGS).is(item) || player.getItemBySlot(EquipmentSlot.FEET).is(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a food item's saturation amount.
     **/

    public static BigDecimal getFoodSaturation(ItemStack itemStack) {
        FoodProperties component = itemStack.get(DataComponents.FOOD);
        if (component == null) return new BigDecimal(0);
        return BigDecimal.valueOf(component.nutrition() * component.saturation() * 2.0F).setScale(1, RoundingMode.DOWN);
    }

    /**
     * Checks for enchantments or armor trims.
     **/

    public static int getEnchantmentLevel(ItemStack itemStack, ResourceKey<Enchantment> enchantment) {
        int noEnchantments = 0;
        ItemEnchantments itemEnchantments = itemStack.get(DataComponents.ENCHANTMENTS);
        if (itemEnchantments == null || itemEnchantments.isEmpty()) return noEnchantments;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantments.entrySet()) {
            if (entry.getKey().is(enchantment)) return EnchantmentHelper.getItemEnchantmentLevel(entry.getKey(), itemStack);
            return noEnchantments;
        }
        return noEnchantments;
    }

    public static boolean hasArmorTrim(ItemStack itemStack) {
        return itemStack.has(DataComponents.TRIM);
    }

    public static boolean hasEnchantmentOrTrim(ItemStack itemStack) {
        return (itemStack.isEnchanted() || hasArmorTrim(itemStack));
    }

    // region Custom Item Tooltips

    /**
     * Custom tooltips for items.
     **/

    public static void addHoldingTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_held").withStyle(ChatFormatting.GRAY));
    }

    public static void addUseTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_used", Minecraft.getInstance().options.keyUse.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
    }

    public static void addAttackTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_used", Minecraft.getInstance().options.keyAttack.getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
    }

    public static void addFoodTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_eaten").withStyle(ChatFormatting.GRAY));
    }

    public static void addDrinkTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("gui.chrysalis.item.when_drank").withStyle(ChatFormatting.GRAY));
    }

    public static void addCoordinatesTooltip(List<Component> tooltip, int x, int y, int z) {
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.coordinates", x, y, z).withStyle(ChatFormatting.BLUE)));
    }

    public static void addDirectionTooltip(List<Component> tooltip, Minecraft minecraft) {
        if (minecraft.player != null) {
            Component direction = Component.translatable("gui.chrysalis.direction." + minecraft.player.getDirection().getName()).withStyle(ChatFormatting.BLUE);
            tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.facing_direction", direction).withStyle(ChatFormatting.BLUE)));
        }
    }

    public static void addDimensionTooltip(List<Component> tooltip, String dimension) {
        String registryKey = dimension.split(":")[0];
        String registryPath = dimension.split(":")[1];

        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.dimension",
                Component.translatable("dimension." + registryKey + "." + registryPath).withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE)));
    }

    public static void addSpaceOnTooltipIfEnchantedOrTrimmed(ItemStack itemStack, List<Component> tooltip) {
        if (hasEnchantmentOrTrim(itemStack)) tooltip.add(CommonComponents.EMPTY);
    }

    public static void addNullTooltip(List<Component> tooltip) {
        tooltip.add(CommonComponents.space().append(Component.translatable("gui.chrysalis.none").withStyle(ChatFormatting.BLUE)));
    }

    public static Component addTooltipWithIcon(Component icon, Component tooltip) {
        return Component.translatable("gui.chrysalis.item.tooltip_with_icon", icon, tooltip);
    }

    // endregion
}