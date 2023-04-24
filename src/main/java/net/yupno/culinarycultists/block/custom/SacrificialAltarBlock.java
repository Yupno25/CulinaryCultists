package net.yupno.culinarycultists.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yupno.culinarycultists.block.entity.SacrificialAltarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SacrificialAltarBlock extends BaseEntityBlock {
    public SacrificialAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide && interactionHand.name().equals("MAIN_HAND")){
            player.sendSystemMessage(Component.literal("It works!"));

            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof SacrificialAltarBlockEntity){
                player.sendSystemMessage(Component.literal("Test succesful: " +
                        ((SacrificialAltarBlockEntity)blockEntity).getItemsAbove(level, (SacrificialAltarBlockEntity)blockEntity)));
            }
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
