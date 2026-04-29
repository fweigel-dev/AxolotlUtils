package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.mobutils.core.network.ServerModPlayerRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public final class ServerBreedingNetworkHandler {

    private static final ServerModPlayerRegistry modPlayers = new ServerModPlayerRegistry();

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(AxolotlUtilsPayloads.HelloC2S.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            modPlayers.add(player);
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.HelloAckS2C());
            AxolotlUtils.LOGGER.debug("Axolotl Utils handshake with {}", player.getName().getString());
        });
    }

    public static void notifyBreedingEvent(ServerPlayer player) {
        if (modPlayers.contains(player)) {
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.BreedingEventS2C());
        }
    }

    public static void notifyFishFedEvent(ServerPlayer player) {
        if (modPlayers.contains(player)) {
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.FishFedEventS2C());
        }
    }

    private ServerBreedingNetworkHandler() {}
}
