package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.AxolotlUtilsStorage;
import dev.fweigel.BreedingTracker;
import dev.fweigel.TrackingMode;
import dev.fweigel.mobutils.core.client.network.ModHandshakeClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ClientBreedingNetworkHandler {

    private static final ModHandshakeClient handshakeClient =
            new ModHandshakeClient(AxolotlUtilsPayloads.HANDSHAKE);

    public static void register() {
        handshakeClient.registerReceiver(() -> {
            BreedingTracker.setMode(TrackingMode.SERVER_SYNCED);
            AxolotlUtils.LOGGER.info("Breeding tracking: SERVER_SYNCED mode");
        });

        ClientPlayNetworking.registerGlobalReceiver(AxolotlUtilsPayloads.BreedingEventS2C.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                BreedingTracker.increment();
                AxolotlUtilsStorage.save();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(AxolotlUtilsPayloads.FishFedEventS2C.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                BreedingTracker.incrementFishUsed();
                AxolotlUtilsStorage.save();
            });
        });
    }

    public static void onJoin() {
        handshakeClient.onJoin(() -> {
            BreedingTracker.setMode(TrackingMode.CLIENT_ONLY);
            AxolotlUtils.LOGGER.info("Breeding tracking: CLIENT_ONLY mode (server does not support hello)");
        });
    }

    public static void tick() {
        if (handshakeClient.tick()) {
            BreedingTracker.setMode(TrackingMode.CLIENT_ONLY);
            AxolotlUtils.LOGGER.info("Breeding tracking: CLIENT_ONLY mode (handshake timeout)");
        }
    }

    private ClientBreedingNetworkHandler() {}
}
