package net.stacking.sync_mod.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.stacking.sync_mod.Sync_mod;

public class SyncBlocks {
    public static final Block SHELL_CONSTRUCTOR = register(
            "shell_constructor",
            new ShellConstructorBlock(AbstractBlock.Settings.create()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()
                    .nonOpaque())
    );

    public static final Block SHELL_STORAGE = register(
            "shell_storage",
            new ShellStorageBlock(AbstractBlock.Settings.create()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()
                    .nonOpaque())
    );

    public static final Block TREADMILL = register(
            "treadmill",
            new TreadmillBlock(AbstractBlock.Settings.create()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool()
                    .nonOpaque())
    );

    private static Block register(String name, Block block) {
        Identifier id = Identifier.of(Sync_mod.MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void register() {
        Sync_mod.LOGGER.info("Registering blocks for " + Sync_mod.MOD_ID);
    }
}
