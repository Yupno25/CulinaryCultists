package net.yupno.culinarycultists.sacrificepoints;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSacrificePointsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerSacrificePoints> PLAYER_SACRIFICE_POINTS = CapabilityManager.get(new CapabilityToken<PlayerSacrificePoints>() {});
    private PlayerSacrificePoints sacrifice_points = null;
    private final LazyOptional<PlayerSacrificePoints> optional = LazyOptional.of(this::createPlayerSacrificePoints);

    private PlayerSacrificePoints createPlayerSacrificePoints() {
        if (this.sacrifice_points == null){
            this.sacrifice_points = new PlayerSacrificePoints();
        }

        return this.sacrifice_points;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_SACRIFICE_POINTS){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerSacrificePoints().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerSacrificePoints().loadNBTData(nbt);
    }
}
