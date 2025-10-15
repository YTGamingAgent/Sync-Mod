package net.stacking.sync_mod;

import net.fabricmc.api.ModInitializer;
import net.stacking.sync_mod.block.SyncBlocks;
import net.stacking.sync_mod.block.entity.SyncBlockEntities;
import net.stacking.sync_mod.config.SyncConfig;
import net.stacking.sync_mod.item.SyncItems;
import net.stacking.sync_mod.networking.SyncPackets;
import net.stacking.sync_mod.sound.SyncSounds; // ⭐ ADD THIS
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sync_mod implements ModInitializer {
    public static final String MOD_ID = "sync_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static SyncConfig config;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Sync Mod...");

        config = SyncConfig.resolve();

        SyncBlocks.register();
        SyncBlockEntities.register();
        SyncItems.register();
        SyncSounds.register(); // ⭐ ADD THIS LINE

        SyncPackets.registerServer();

        LOGGER.info("Sync Mod initialized successfully!");
    }

    public static SyncConfig getConfig() {
        return config;
    }
}
