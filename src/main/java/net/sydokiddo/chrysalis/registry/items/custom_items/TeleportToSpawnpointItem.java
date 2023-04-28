package net.sydokiddo.chrysalis.registry.items.custom_items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class TeleportToSpawnpointItem extends Item {

    public TeleportToSpawnpointItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }

    private MutableComponent getDisplayName() {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    // Teleports the player to their spawn when right-clicked

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);

        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            player.playNotifySound(SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            serverPlayer.teleportTo(Objects.requireNonNull(serverPlayer.getRespawnPosition()).getX(), serverPlayer.getRespawnPosition().getY() + 1, serverPlayer.getRespawnPosition().getZ());
            serverPlayer.sendSystemMessage(Component.translatable("item.chrysalis.teleport_to_spawnpoint_message"));
            player.getCooldowns().addCooldown(this, 60);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
