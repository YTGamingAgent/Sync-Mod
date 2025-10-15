package net.stacking.sync_mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.stacking.sync_mod.block.entity.SyncBlockEntities;
import net.stacking.sync_mod.block.entity.TreadmillBlockEntity;
import org.jetbrains.annotations.Nullable;

public class TreadmillBlock extends BlockWithEntity {
    // ⭐ ADD THIS: Codec for block serialization
    public static final MapCodec<TreadmillBlock> CODEC = createCodec(TreadmillBlock::new);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public TreadmillBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    // ⭐ ADD THIS: Required method in 1.21+
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TreadmillBlockEntity(pos, state);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.getBlockEntity(pos) instanceof TreadmillBlockEntity be) {
            be.onSteppedOn(pos, state, entity);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, SyncBlockEntities.TREADMILL,
                world.isClient ? TreadmillBlockEntity::clientTick : TreadmillBlockEntity::serverTick);
    }
}
