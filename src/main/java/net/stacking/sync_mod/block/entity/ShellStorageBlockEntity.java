package net.stacking.sync_mod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ShellStorageBlockEntity extends BlockEntity {
    public ShellStorageBlockEntity(BlockPos pos, BlockState state) {
        super(SyncBlockEntities.SHELL_STORAGE, pos, state);
    }
}
