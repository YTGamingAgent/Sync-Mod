package net.stacking.sync_mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.stacking.sync_mod.block.entity.ShellStorageBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ShellStorageBlock extends BlockWithEntity {
    // ⭐ ADD THIS: Codec for block serialization
    public static final MapCodec<ShellStorageBlock> CODEC = createCodec(ShellStorageBlock::new);

    public ShellStorageBlock(Settings settings) {
        super(settings);
    }

    // ⭐ ADD THIS: Required method in 1.21+
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ShellStorageBlockEntity(pos, state);
    }
}
