package net.stacking.sync_mod.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.stacking.sync_mod.Sync_mod;
import net.stacking.sync_mod.block.entity.SyncBlockEntities;

public class SyncRenderers {
    public static void register() {
        Sync_mod.LOGGER.info("Registering client renderers");

        // Example: Register block entity renderer
        // BlockEntityRendererRegistry.register(
        //     SyncBlockEntities.SHELL_CONSTRUCTOR,
        //     ShellConstructorBlockEntityRenderer::new
        // );
    }
}
