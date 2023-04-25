package net.yupno.culinarycultists.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yupno.culinarycultists.block.entity.SacrificialAltarBlockEntity;
import org.jetbrains.annotations.Nullable;

import static net.yupno.culinarycultists.block.entity.SacrificialAltarBlockEntity.getItemsAbove;

public class SacrificialAltarBlock extends BaseEntityBlock {
    public SacrificialAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        int i = 0;
        String[] recipe = new String[]{"Diamond", "Emerald"};

        if(level.isClientSide && interactionHand.name().equals("MAIN_HAND")){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);

            if(blockEntity instanceof SacrificialAltarBlockEntity){

                for (ItemEntity itemEntity : ((SacrificialAltarBlockEntity)blockEntity).getItemsAbove(level, (SacrificialAltarBlockEntity)blockEntity)) {
                    for (int j = 0; j < recipe.length; j++){
                        if(itemEntity.getName().getString().equals(recipe[j])){
                            i++;
                            recipe[j] = null;
                        }
                    }
                }

                /*
                // crashes if there is no item on top
                player.sendSystemMessage(Component.literal("Test successful: " +
                        ((SacrificialAltarBlockEntity)blockEntity).getItemsAbove(level, (SacrificialAltarBlockEntity)blockEntity).get(0).getItem()
                                .getFoodProperties(player).getNutrition()));
                 */
                // .get(0).getItem().getFoodProperties(player).getNutrition(), getSaturationModifier(); .get(0).getItem().isEdible()
                //player.sendSystemMessage(Component.literal("Display name: " + itemEntity.getName().getString()));
            }
        }

        // Only gives fake diamonds
        if (i == 2 && interactionHand.name().equals("MAIN_HAND")) {
            player.addItem(new ItemStack(Items.DIAMOND, 4));
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SacrificialAltarBlockEntity(blockPos, blockState);
    }
}
