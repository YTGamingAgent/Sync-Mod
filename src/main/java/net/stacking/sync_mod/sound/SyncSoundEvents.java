package net.stacking.sync_mod.sound;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * High-level sound event system that automatically manages sound playback
 * for various game events.
 */
public class SyncSoundEvents {

    // ==================== SHELL CONSTRUCTION EVENTS ====================

    public static void onShellConstructionStart(World world, BlockPos pos) {
        if (world.isClient) return;
        SoundManager.playShellConstructionStart(world, pos);
    }

    public static void onShellConstructionProgress(World world, BlockPos pos, float progress) {
        if (world.isClient) return;

        // Play progress sound every 25% completion
        int progressPercent = (int) (progress * 100);
        if (progressPercent % 25 == 0 && progressPercent > 0) {
            SoundManager.playShellConstructionProgress(world, pos, progress);
        }
    }

    public static void onShellConstructionComplete(World world, BlockPos pos) {
        if (world.isClient) return;
        SoundManager.playShellConstructionComplete(world, pos);
    }

    public static void onShellActivate(World world, BlockPos pos) {
        if (world.isClient) return;
        SoundManager.playShellActivate(world, pos);
    }

    // ==================== TREADMILL EVENTS ====================

    public static void onEntityStepOnTreadmill(World world, BlockPos pos, Entity entity) {
        if (world.isClient) return;
        SoundManager.playTreadmillStart(world, pos);
    }

    public static void onTreadmillTick(World world, BlockPos pos, int runningTime) {
        if (world.isClient) return;

        // Play running sound every 20 ticks (1 second)
        if (runningTime % 20 == 0) {
            SoundManager.playTreadmillRunning(world, pos, runningTime);
        }
    }

    public static void onEntityLeavesTreadmill(World world, BlockPos pos) {
        if (world.isClient) return;
        SoundManager.playTreadmillStop(world, pos);
    }

    public static void onTreadmillOverheat(World world, BlockPos pos) {
        if (world.isClient) return;
        SoundManager.playTreadmillOverheat(world, pos);
    }

    // ==================== ENERGY EVENTS ====================

    public static void onEnergyTransfer(World world, BlockPos pos, long amount) {
        if (world.isClient) return;

        // Only play sound for significant energy transfers
        if (amount > 100) {
            SoundManager.playEnergyTransfer(world, pos);
        }
    }
}
