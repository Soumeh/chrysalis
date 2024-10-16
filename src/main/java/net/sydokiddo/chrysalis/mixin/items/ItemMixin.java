package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract String getDescriptionId();

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void chrysalis$addTooltipToItems(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {

        if (itemStack.is(Items.DEBUG_STICK)) {
            ItemHelper.addUseTooltip(list);
            list.add(CommonComponents.space().append(Component.translatable("item.chrysalis.debug_stick.desc").withStyle(ChatFormatting.BLUE)));
        }

        if (this.getDescriptionId().contains(Chrysalis.MOD_ID)) {
            if (!list.isEmpty()) list.add(CommonComponents.EMPTY);
            MutableComponent chrysalisIcon = ChrysalisRegistry.CHRYSALIS_ICON;
            ChrysalisRegistry.setTooltipIconsFont(chrysalisIcon);
            Component chrysalisTooltip = ItemHelper.addTooltipWithIcon(chrysalisIcon, Component.translatable("mod.chrysalis").withStyle(style -> style.withFont(ChrysalisRegistry.FIVE_FONT).withColor(ChrysalisRegistry.CHRYSALIS_COLOR.getRGB())));
            list.add(chrysalisTooltip);
        }
    }
}