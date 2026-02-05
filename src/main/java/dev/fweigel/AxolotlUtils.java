package dev.fweigel;

import dev.fweigel.network.AxolotlUtilsPayloads;
import dev.fweigel.network.ServerBreedingNetworkHandler;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AxolotlUtils implements ModInitializer {
    public static final String MOD_ID = "axolotlutils";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AxolotlUtilsPayloads.registerAll();
        ServerBreedingNetworkHandler.register();
        LOGGER.info("Axolotl Utils initialized");
    }
}
