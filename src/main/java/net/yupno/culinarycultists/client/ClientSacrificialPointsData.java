package net.yupno.culinarycultists.client;

public class ClientSacrificialPointsData {
    private static int player_sacrifice_points;

    public static void set(int sacrifice_points) {
        ClientSacrificialPointsData.player_sacrifice_points = sacrifice_points;
    }

    public static int getPlayerThirst() {
        return player_sacrifice_points;
    }
}
