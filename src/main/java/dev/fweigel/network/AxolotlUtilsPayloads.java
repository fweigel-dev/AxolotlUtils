package dev.fweigel.network;

import dev.fweigel.AxolotlUtils;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public final class AxolotlUtilsPayloads {

    public record HelloC2S() implements CustomPacketPayload {
        public static final Type<HelloC2S> TYPE = new Type<>(
                Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "hello"));

        @Override
        public Type<HelloC2S> type() {
            return TYPE;
        }
    }

    public record HelloAckS2C() implements CustomPacketPayload {
        public static final Type<HelloAckS2C> TYPE = new Type<>(
                Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "hello_ack"));

        @Override
        public Type<HelloAckS2C> type() {
            return TYPE;
        }
    }

    public record BreedingEventS2C() implements CustomPacketPayload {
        public static final Type<BreedingEventS2C> TYPE = new Type<>(
                Identifier.fromNamespaceAndPath(AxolotlUtils.MOD_ID, "breeding_event"));

        @Override
        public Type<BreedingEventS2C> type() {
            return TYPE;
        }
    }

    public static void registerAll() {
        PayloadTypeRegistry.playC2S().register(HelloC2S.TYPE, StreamCodec.unit(new HelloC2S()));
        PayloadTypeRegistry.playS2C().register(HelloAckS2C.TYPE, StreamCodec.unit(new HelloAckS2C()));
        PayloadTypeRegistry.playS2C().register(BreedingEventS2C.TYPE, StreamCodec.unit(new BreedingEventS2C()));
    }

    private AxolotlUtilsPayloads() {}
}
