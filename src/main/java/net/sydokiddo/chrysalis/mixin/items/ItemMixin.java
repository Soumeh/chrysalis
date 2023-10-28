package net.sydokiddo.chrysalis.mixin.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    /**
     * Adds a description tooltip to the Debug Stick item.
     **/

    @Inject(method = "appendHoverText", at = @At("RETURN"))
    private void chrysalis$addDebugStickTooltip(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (itemStack.is(Items.DEBUG_STICK)) {
            ChrysalisRegistry.addUseTooltip(tooltip);
            tooltip.add(Component.translatable("item.chrysalis.debug_stick.desc").withStyle(ChatFormatting.BLUE));
        }
    }
}