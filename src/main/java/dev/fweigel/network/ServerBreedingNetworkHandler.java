package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class ServerBreedingNetworkHandler {

    private static final Set<ServerPlayer> MOD_PLAYERS = Collections.newSetFromMap(new WeakHashMap<>());

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(AxolotlUtilsPayloads.HelloC2S.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            MOD_PLAYERS.add(player);
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.HelloAckS2C());
            AxolotlUtils.LOGGER.debug("Axolotl Utils handshake with {}", player.getName().getString());
        });
    }

    public static void notifyBreedingEvent(ServerPlayer player) {
        if (MOD_PLAYERS.contains(player)) {
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.BreedingEventS2C());
        }
    }

    public static void notifyFishFedEvent(ServerPlayer player) {
        if (MOD_PLAYERS.contains(player)) {
            ServerPlayNetworking.send(player, new AxolotlUtilsPayloads.FishFedEventS2C());
        }
    }

    private ServerBreedingNetworkHandler() {}
}
