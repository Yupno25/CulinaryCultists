package net.yupno.culinarycultists.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yupno.culinarycultists.CulinaryCultists;
import net.yupno.culinarycultists.block.entity.SacrificeBlockEntity;
import net.yupno.culinarycultists.sacrificepoints.PlayerSacrificePointsProvider;
import net.yupno.culinarycultists.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class SacrificeBlock extends BaseEntityBlock {

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(interactionHand != interactionHand.MAIN_HAND)
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

        BlockEntity entity = level.getBlockEntity(blockPos);

        if(entity instanceof SacrificeBlockEntity){
            SacrificeBlockEntity sacrificeBlockEntity = ((SacrificeBlockEntity)entity);

            // Not removing items properly
            if(!level.isClientSide){
                player.getCapability(PlayerSacrificePointsProvider.PLAYER_SACRIFICE_POINTS).ifPresent(points -> {
                    points.addSacrificePoints(sacrificeStuff(level, sacrificeBlockEntity,player, blockPos));

                    player.sendSystemMessage(Component.literal("You offered some of your food!"));
                    player.sendSystemMessage(Component.literal("Current Sacrifice Points: " + points.getSacrificePoints()));
                });
            }else {
                sacrificeStuff(level, sacrificeBlockEntity,player, blockPos);
            }
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    private int sacrificeStuff(Level level, SacrificeBlockEntity sacrificeBlockEntity, Player player, BlockPos blockPos){
        List<ItemEntity> itemEntities = sacrificeBlockEntity.getItemsAbove(level, sacrificeBlockEntity);
        int totalSaturation = 0;

        level.playSound(player, blockPos, ModSounds.SACRIFICE_SOUND.get(), SoundSource.BLOCKS, 1f, 1f);

        // Removes items, stores their saturation and does particle effects
        for (ItemEntity itemEntity : itemEntities) {
            if(itemEntity.getItem().isEdible() && !itemEntity.getItem().getDescriptionId().contains(CulinaryCultists.MOD_ID)){
                int stackSize = itemEntity.getItem().getCount();
                for (int i = 0; i < stackSize; i++) {
                    itemVanishParticles(level, itemEntity.xOld, itemEntity.yOld, itemEntity.zOld);
                    // Calculation that gives higher nutrition and saturation food more value
                    // Values still need to be adjusted
                    totalSaturation += Math.round(Math.pow(
                            (itemEntity.getItem().getFoodProperties(null).getNutrition() *
                                    (1 + itemEntity.getItem().getFoodProperties(null).getSaturationModifier()))
                                    /2, 2)/8);

                    itemEntity.getItem().shrink(1);
                }
            }
        }

        return totalSaturation;
    }

    private void itemVanishParticles(Level level, double x, double y, double z){
        Random rand = new Random();

        for (int i = 0; i < 50; i++) {
            float r1 = rand.nextFloat(0.2f);
            float r2 = rand.nextFloat(0.2f);

            if(rand.nextBoolean())
                r1 *= -1;
            if(rand.nextBoolean())
                r2 *= -1;

            level.addParticle(ParticleTypes.WITCH, x + r1, y, z + r2, 1D, 0.5D, 1D);
        }
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

    public SacrificeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
