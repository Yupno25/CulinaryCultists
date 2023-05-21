package net.yupno.culinarycultists.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.stream.Collectors;

public class SacrificeBlockEntity extends BlockEntity {
    public SacrificeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SACRIFICE_BLOCK.get(), blockPos, blockState);
    }

    VoxelShape ABOVE = Block.box(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);

    public static List<ItemEntity> getItemsAbove(Level level, SacrificeBlockEntity sacrificeBlockEntity) {
        return sacrificeBlockEntity.ABOVE.toAabbs().stream().flatMap((aabb) -> {
            return level.getEntitiesOfClass(ItemEntity.class,
                    aabb.move(sacrificeBlockEntity.getLevelX(), sacrificeBlockEntity.getLevelY(), sacrificeBlockEntity.getLevelZ()),
                    EntitySelector.ENTITY_STILL_ALIVE).stream();
        }).collect(Collectors.toList());
    }

    public double getLevelX() {
        return (double)this.worldPosition.getX();
    }

    public double getLevelY() {
        return (double)this.worldPosition.getY();
    }

    public double getLevelZ() {
        return (double)this.worldPosition.getZ();
    }
}
