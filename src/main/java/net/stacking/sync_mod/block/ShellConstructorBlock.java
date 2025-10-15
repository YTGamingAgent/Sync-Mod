package net.stacking.sync_mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stacking.sync_mod.block.entity.ShellConstructorBlockEntity;
import net.stacking.sync_mod.block.entity.SyncBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ShellConstructorBlock extends BlockWithEntity {
    // ⭐ ADD THIS: Codec for block serialization
    public static final MapCodec<ShellConstructorBlock> CODEC = createCodec(ShellConstructorBlock::new);

    public ShellConstructorBlock(Settings settings) {
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
        return new ShellConstructorBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof ShellConstructorBlockEntity be) {
            return be.onUse(world, pos, player, player.getActiveHand());
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, SyncBlockEntities.SHELL_CONSTRUCTOR,
                world.isClient ? ShellConstructorBlockEntity::clientTick : ShellConstructorBlockEntity::serverTick);
    }
}
