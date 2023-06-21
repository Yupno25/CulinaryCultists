package net.yupno.culinarycultists.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.yupno.culinarycultists.client.ClientSacrificialPointsData;

import java.util.function.Supplier;

public class SacrificePointsDataSyncS2CPacket {
    /*
    private int sacrifice_points;

    public SacrificePointsDataSyncS2CPacket(int sacrifice_points) {
        this.sacrifice_points = sacrifice_points;
    }

    public SacrificePointsDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.sacrifice_points = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(sacrifice_points);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientSacrificialPointsData.set(sacrifice_points);
        });
        return true;
    }
     */

}
