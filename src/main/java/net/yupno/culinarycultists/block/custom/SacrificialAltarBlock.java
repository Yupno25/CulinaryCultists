package net.yupno.culinarycultists.block.custom;


import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yupno.culinarycultists.block.entity.SacrificialAltarBlockEntity;
import net.yupno.culinarycultists.recipe.SacrificialAltarRecipe;
import net.yupno.culinarycultists.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SacrificialAltarBlock extends BaseEntityBlock {
    public SacrificialAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(interactionHand != interactionHand.MAIN_HAND)
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

        BlockEntity entity = level.getBlockEntity(blockPos);

        if(entity instanceof SacrificialAltarBlockEntity){

            List<ItemEntity> itemEntities = ((SacrificialAltarBlockEntity)entity).getItemsAbove(level, (SacrificialAltarBlockEntity)entity);
            SimpleContainer inventory = new SimpleContainer(itemEntities.size());

            int t = 0;
            for (ItemEntity itemEntity : itemEntities) {
                ItemStack itemStack = itemEntity.getItem();
                inventory.setItem(t, itemStack);
                t++;
            }

            Optional<SacrificialAltarRecipe> recipe = level.getRecipeManager().getRecipeFor(SacrificialAltarRecipe.Type.INSTANCE, inventory, level);

            if(!recipe.isEmpty()){
                level.playSound(player, blockPos, ModSounds.SACRIFICE_SOUND.get(), SoundSource.BLOCKS, 1f, 1f);

                List<ItemStack> itemsToRemove = recipe.get().getItemsToRemove();

                // Items to remove doesn't empty itself
                player.sendSystemMessage(Component.literal("ItemsToRemove: " + itemsToRemove));

                // Removes items and does particle effects
                for (ItemEntity itemEntity : itemEntities) {
                    for (int i = 0; i < itemsToRemove.size(); i++) {
                        if(itemEntity.getItem() == itemsToRemove.get(i))
                        {
                            itemVanishParticles(level, itemEntity.xOld, itemEntity.yOld, itemEntity.zOld);
                            itemsToRemove.get(i).shrink(1);
                        }
                    }
                }

                // Spawns output if the items on top match a recipe
                level.addFreshEntity(new ItemEntity(level, (int)(((SacrificialAltarBlockEntity)entity).getLevelX()) + 0.5D,
                        (int)(((SacrificialAltarBlockEntity)entity).getLevelY()) + 1D, (int)(((SacrificialAltarBlockEntity)entity).getLevelZ())  + 0.5D,
                        recipe.get().getResultItem(null), 0, 0, 0));
            }
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
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
        return new SacrificialAltarBlockEntity(blockPos, blockState);
    }
}
