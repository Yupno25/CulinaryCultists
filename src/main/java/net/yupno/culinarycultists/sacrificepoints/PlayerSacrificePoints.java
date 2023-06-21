package net.yupno.culinarycultists.sacrificepoints;

import net.minecraft.nbt.CompoundTag;

public class PlayerSacrificePoints {
    private int sacrifice_points;
    private final int MAX_SACRIFICE_POINTS = 1000;
    private final int MIN_SACRIFICE_POINTS = 0;

    public int getSacrificePoints(){
        return sacrifice_points;
    }

    public void addSacrificePoints(int add){
        // Limits sacrifice points to maximum
        this.sacrifice_points = Math.min(sacrifice_points + add, MAX_SACRIFICE_POINTS);
    }

    public void subSacrificePoints(int sub){
        // Limits sacrifice points to minimum
        this.sacrifice_points = Math.max(sacrifice_points - sub, MIN_SACRIFICE_POINTS);
    }

    public void copyFrom(PlayerSacrificePoints source){
        this.sacrifice_points = source.sacrifice_points;
    }

    public void saveNBTData(CompoundTag nbt){
        nbt.putInt("sacrifice_points", sacrifice_points);
    }

    public void loadNBTData(CompoundTag nbt){
        sacrifice_points = nbt.getInt("sacrifice_points");
    }
}
