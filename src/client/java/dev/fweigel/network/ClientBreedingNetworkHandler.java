package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.AxolotlUtilsStorage;
import dev.fweigel.BreedingTracker;
import dev.fweigel.TrackingMode;
import dev.fweigel.mobutils.core.client.network.ClientHandshakeTracker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ClientBreedingNetworkHandler {

    private static final ClientHandshakeTracker handshake = new ClientHandshakeTracker();

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(AxolotlUtilsPayloads.HelloAckS2C.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                handshake.onAck();
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
            handshake.startHandshake();
        } else {
            BreedingTracker.setMode(TrackingMode.CLIENT_ONLY);
            AxolotlUtils.LOGGER.info("Breeding tracking: CLIENT_ONLY mode (server does not support hello)");
        }
    }

    public static void tick() {
        if (handshake.tick()) {
            BreedingTracker.setMode(TrackingMode.CLIENT_ONLY);
            AxolotlUtils.LOGGER.info("Breeding tracking: CLIENT_ONLY mode (handshake timeout)");
        }
    }

    private ClientBreedingNetworkHandler() {}
}
