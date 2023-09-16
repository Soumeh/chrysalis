package net.sydokiddo.chrysalis.misc.util.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.chrysalis.misc.util.RegistryHelpers;
import net.sydokiddo.chrysalis.registry.items.custom_items.MobInContainerItem;

@SuppressWarnings("ALL")
public class DispenseBucketMobDispenserBehavior implements DispenseItemBehavior {

    public static final DispenseBucketMobDispenserBehavior INSTANCE = new DispenseBucketMobDispenserBehavior();

    @Override
    public ItemStack dispense(BlockSource blockSource, ItemStack itemStack) {

        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        ServerLevel level = blockSource.level();
        BlockState blockState = level.getBlockState(blockPos);
        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);

        if (RegistryHelpers.isBlockStateFree(blockState)) {

            RegistryHelpers.playDispenserSound(blockSource);
            RegistryHelpers.playDispenserAnimation(blockSource, direction);
            level.gameEvent(GameEvent.ENTITY_PLACE, blockPos, GameEvent.Context.of(blockSource.state()));

            if (itemStack.getItem() instanceof MobInContainerItem mobInContainerItem) {
                level.playSound(null, blockPos, mobInContainerItem.emptySound, SoundSource.NEUTRAL, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
                mobInContainerItem.checkExtraContent(null, level, itemStack, blockPos);
                return new ItemStack(mobInContainerItem.returnItem.asItem());
            } else if (itemStack.getItem() instanceof MobBucketItem mobBucketItem) {
                mobBucketItem.emptyContents(null, level, blockPos, null);
                mobBucketItem.checkExtraContent(null, level, itemStack, blockPos);
                return new ItemStack(Items.BUCKET);
            }
        }
        return RegistryHelpers.defaultDispenseItemBehavior.dispense(blockSource, itemStack);
    }
}