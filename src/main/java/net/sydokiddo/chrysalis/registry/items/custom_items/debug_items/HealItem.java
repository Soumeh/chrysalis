package net.sydokiddo.chrysalis.registry.items.custom_items.debug_items;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.chrysalis.registry.misc.ChrysalisSoundEvents;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class HealItem extends DebugUtilityItem {

    public HealItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        ItemHelper.addUseTooltip(list);
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
    }

    /**
     * Sets the user to full health when right-clicked with.
     **/

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (player.getHealth() < player.getMaxHealth()) {

            if (!level.isClientSide()) {
                player.playNotifySound(ChrysalisSoundEvents.HEAL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
                player.awardStat(Stats.ITEM_USED.get(this));
                player.setHealth(player.getMaxHealth());
                player.sendSystemMessage(Component.translatable("gui.chrysalis.heal_message", player.getName().getString()));
            }

            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        }

        return InteractionResultHolder.pass(itemStack);
    }
}