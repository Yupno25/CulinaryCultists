package net.yupno.culinarycultists.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yupno.culinarycultists.block.entity.SacrificeBlockEntity;
import net.yupno.culinarycultists.block.entity.SacrificialAltarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SacrificeBlock extends BaseEntityBlock {

    public SacrificeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(interactionHand.name().equals("MAIN_HAND"))
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

        player.sendSystemMessage(Component.literal("You offered some of your food!"));

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SacrificeBlockEntity(blockPos, blockState);
    }
}
