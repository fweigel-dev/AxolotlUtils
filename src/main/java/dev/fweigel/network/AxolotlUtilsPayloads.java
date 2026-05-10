package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import dev.fweigel.mobutils.core.network.ModHandshake;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public final class AxolotlUtilsPayloads {

    public static final ModHandshake HANDSHAKE = new ModHandshake(AxolotlUtils.MOD_ID);

    public record BreedingEventS2C() implements CustomPacketPayload {
        public static final Type<BreedingEventS2C> TYPE = new Type<>(
                Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "breeding_event"));

        @Override
        public Type<BreedingEventS2C> type() {
            return TYPE;
        }
    }

    public record FishFedEventS2C() implements CustomPacketPayload {
        public static final Type<FishFedEventS2C> TYPE = new Type<>(
                Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "fish_fed_event"));

        @Override
        public Type<FishFedEventS2C> type() {
            return TYPE;
        }
    }

    public static void registerAll() {
        HANDSHAKE.registerPayloads();
        PayloadTypeRegistry.clientboundPlay().register(BreedingEventS2C.TYPE, StreamCodec.unit(new BreedingEventS2C()));
        PayloadTypeRegistry.clientboundPlay().register(FishFedEventS2C.TYPE, StreamCodec.unit(new FishFedEventS2C()));
    }

    private AxolotlUtilsPayloads() {}
}
