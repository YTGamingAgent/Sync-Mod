package net.stacking.sync_mod.client;

import net.fabricmc.api.ClientModInitializer;
import net.stacking.sync_mod.Sync_mod;
import net.stacking.sync_mod.networking.SyncPackets;

public class Sync_modClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Sync_mod.LOGGER.info("Initializing Sync Mod Client...");

        // Register client-side networking
        SyncPackets.registerClient();

        // TODO: Add block entity renderers later when needed

        Sync_mod.LOGGER.info("Sync Mod Client initialized!");
    }
}
