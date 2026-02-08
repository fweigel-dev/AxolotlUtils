package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.AxolotlUtilsStorage;
import dev.fweigel.BreedingTracker;
import dev.fweigel.TrackingMode;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ClientBreedingNetworkHandler {

    private static int handshakeTimer = -1;
    private static final int HANDSHAKE_TIMEOUT = 60;

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(AxolotlUtilsPayloads.HelloAckS2C.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                handshakeTimer = -1;
                BreedingTracker.setMode(TrackingMode.SERVER_SYNCED);
                AxolotlUtils.LOGGER.info("Breeding tracking: SERVER_SYNCED mode");
            });
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
        if (ClientPlayNetworking.canSend(AxolotlUtilsPayloads.HelloC2S.TYPE)) {
            ClientPlayNetworking.send(new AxolotlUtilsPayloads.HelloC2S());
            handshakeTimer = HANDSHAKE_TIMEOUT;
        } else {
            BreedingTracker.setMode(TrackingMode.CLIENT_ONLY);
            AxolotlUtils.LOGGER.info("Breeding tracking: CLIENT_ONLY mode (server does not support hello)");
        }
    }

    public static void tick() {
        if (handshakeTimer > 0) {
            handshakeTimer--;
            if (handshakeTimer == 0) {
                handshakeTimer = -1;
                BreedingTracker.setMode(TrackingMode.CLIENT_ONLY);
                AxolotlUtils.LOGGER.info("Breeding tracking: CLIENT_ONLY mode (handshake timeout)");
            }
        }
    }

    private ClientBreedingNetworkHandler() {}
}
