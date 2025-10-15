package net.stacking.sync_mod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.stacking.sync_mod.sound.SyncSoundEvents; // ⭐ ADD

public class TreadmillBlockEntity extends BlockEntity {
    private Entity currentRunner;
    private int runningTime = 0;

    public TreadmillBlockEntity(BlockPos pos, BlockState state) {
        super(SyncBlockEntities.TREADMILL, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, TreadmillBlockEntity be) {
        // Client animations
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, TreadmillBlockEntity be) {
        if (be.currentRunner != null) {
            be.runningTime++;

            // ⭐ Sound: Running sound every second
            SyncSoundEvents.onTreadmillTick(world, pos, be.runningTime);

            // ⭐ Sound: Overheat warning
            if (be.runningTime >= 18000) { // 15 minutes
                SyncSoundEvents.onTreadmillOverheat(world, pos);
            }
        }
    }

    public void onSteppedOn(BlockPos pos, BlockState state, Entity entity) {
        if (currentRunner == null) {
            currentRunner = entity;
            runningTime = 0;

            // ⭐ Sound: Entity steps on
            SyncSoundEvents.onEntityStepOnTreadmill(entity.getWorld(), pos, entity);
        }
    }

    public void stopRunning() {
        if (currentRunner != null) {
            // ⭐ Sound: Entity leaves
            SyncSoundEvents.onEntityLeavesTreadmill(world, pos);

            currentRunner = null;
            runningTime = 0;
        }
    }
}
