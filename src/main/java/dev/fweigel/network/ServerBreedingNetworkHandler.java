package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class ServerBreedingNetworkHandler {

    public static void register() {
        AxolotlUtilsPayloads.HANDSHAKE.registerServerReceiver(AxolotlUtils.LOGGER);
    }

    public static void notifyBreedingEvent(ServerPlayer player) {
        if (AxolotlUtilsPayloads.HANDSHAKE.getModPlayers().contains(player)) {
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.BreedingEventS2C());
        }
    }

    public static void notifyFishFedEvent(ServerPlayer player) {
        if (AxolotlUtilsPayloads.HANDSHAKE.getModPlayers().contains(player)) {
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.FishFedEventS2C());
        }
    }

    private ServerBreedingNetworkHandler() {}
}
