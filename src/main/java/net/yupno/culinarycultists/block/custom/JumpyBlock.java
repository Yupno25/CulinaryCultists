package net.yupno.culinarycultists.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class JumpyBlock extends Block {

    public JumpyBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(interactionHand.name().equals("MAIN_HAND")){
            //player.sendSystemMessage(Component.literal("You offered some of your food!"));
            //level.isClientSide

            if(player.getFoodData().getFoodLevel() > 5) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 5);
                player.addItem(new ItemStack(Items.DIAMOND, 4));

            }
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }


}
