package net.stacking.sync_mod.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.stacking.sync_mod.Sync_mod;
import net.stacking.sync_mod.block.SyncBlocks;

public class SyncBlockEntities {
    public static final BlockEntityType<ShellConstructorBlockEntity> SHELL_CONSTRUCTOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Sync_mod.MOD_ID, "shell_constructor"),
            BlockEntityType.Builder.create(ShellConstructorBlockEntity::new, SyncBlocks.SHELL_CONSTRUCTOR).build()
    );

    public static final BlockEntityType<ShellStorageBlockEntity> SHELL_STORAGE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Sync_mod.MOD_ID, "shell_storage"),
            BlockEntityType.Builder.create(ShellStorageBlockEntity::new, SyncBlocks.SHELL_STORAGE).build()
    );

    public static final BlockEntityType<TreadmillBlockEntity> TREADMILL = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Sync_mod.MOD_ID, "treadmill"),
            BlockEntityType.Builder.create(TreadmillBlockEntity::new, SyncBlocks.TREADMILL).build()
    );

    public static void register() {
        Sync_mod.LOGGER.info("Registering block entities for " + Sync_mod.MOD_ID);
    }
}
