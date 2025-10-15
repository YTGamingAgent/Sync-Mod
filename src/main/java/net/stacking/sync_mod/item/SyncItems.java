package net.stacking.sync_mod.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.stacking.sync_mod.Sync_mod;

public class SyncItems {
    public static final Item SYNC_CORE = register("sync_core",
            new Item(new Item.Settings()));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Sync_mod.MOD_ID, name), item);
    }

    public static void register() {
        Sync_mod.LOGGER.info("Registering items for " + Sync_mod.MOD_ID);
    }
}
