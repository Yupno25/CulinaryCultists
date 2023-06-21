package net.yupno.culinarycultists.block.custom;


import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.yupno.culinarycultists.block.ModBlocks;
import net.yupno.culinarycultists.block.entity.SacrificialAltarBlockEntity;
import net.yupno.culinarycultists.recipe.SacrificialAltarRecipe;
import net.yupno.culinarycultists.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SacrificialAltarBlock extends BaseEntityBlock {
    @Nullable
    private static BlockPattern altarPattern;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SacrificialAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(interactionHand != interactionHand.MAIN_HAND)
            return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

        BlockEntity entity = level.getBlockEntity(blockPos);

        if(entity instanceof SacrificialAltarBlockEntity){
            SacrificialAltarBlockEntity sacrificialAltarBlockEntity = ((SacrificialAltarBlockEntity)entity);

            // Checks for the multiblock structure
            BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = getOrCreateAltarPattern().find(level, blockPos);

            // Checks if there is a multiblock structure and if this block is the block at the center of it
            if (blockpattern$blockpatternmatch != null && blockpattern$blockpatternmatch.getBlock(3, 3,0).getPos().equals(blockPos))
                recipeStuff(level, sacrificialAltarBlockEntity, player, blockPos);
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    private static BlockPattern getOrCreateAltarPattern() {
        if (altarPattern == null) {
            altarPattern = BlockPatternBuilder.start().aisle("svvvvvs", ">sssss<", ">sssss<", ">ss#ss<", ">sssss<", ">sssss<", "s^^^^^s")
                    .where('s', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE_BRICKS)))
                    .where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(ModBlocks.SACRIFICIAL_ALTAR.get())))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE_BRICK_STAIRS).where(FACING, Predicates.equalTo(Direction.SOUTH))))
                    .where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE_BRICK_STAIRS).where(FACING, Predicates.equalTo(Direction.NORTH))))
                    .where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE_BRICK_STAIRS).where(FACING, Predicates.equalTo(Direction.WEST))))
                    .where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE_BRICK_STAIRS).where(FACING, Predicates.equalTo(Direction.EAST)))).build();
            //.where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
        }

        return altarPattern;
    }

    private void recipeStuff(Level level, SacrificialAltarBlockEntity sacrificialAltarBlockEntity, Player player, BlockPos blockPos){
        List<ItemEntity> itemEntities = sacrificialAltarBlockEntity.getItemsAbove(level, sacrificialAltarBlockEntity);
        SimpleContainer inventory = new SimpleContainer(itemEntities.size());

        int t = 0;
        for (ItemEntity itemEntity : itemEntities) {
            ItemStack itemStack = itemEntity.getItem();
            inventory.setItem(t, itemStack);
            t++;
        }

        // Checks if the items above the block match a recipe via the "matches" method
        Optional<SacrificialAltarRecipe> recipe = level.getRecipeManager().getRecipeFor(SacrificialAltarRecipe.Type.INSTANCE, inventory, level);


        if(!recipe.isEmpty()){
            level.playSound(player, blockPos, ModSounds.SACRIFICE_SOUND.get(), SoundSource.BLOCKS, 1f, 1f);

            List<ItemStack> itemsToRemove = recipe.get().getItemsToRemove();

            // Removes items and does particle effects
            for (ItemEntity itemEntity : itemEntities) {
                for (int i = 0; i < itemsToRemove.size(); i++) {
                    if(itemEntity.getItem() == itemsToRemove.get(i))
                    {
                        itemVanishParticles(level, itemEntity.xOld, itemEntity.yOld, itemEntity.zOld);
                        itemEntity.getItem().shrink(1);
                    }
                }
            }

            // Spawns output if the items on top match a recipe
            level.addFreshEntity(new ItemEntity(level, (int) sacrificialAltarBlockEntity.getLevelX() + 0.5D,
                    (int) sacrificialAltarBlockEntity.getLevelY() + 1D, (int) sacrificialAltarBlockEntity.getLevelZ()  + 0.5D,
                    recipe.get().getResultItem(null), 0, 0, 0));
        }
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
