package net.stacking.sync_mod.sound;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.stacking.sync_mod.Sync_mod;
import net.stacking.sync_mod.config.SyncConfig;

/**
 * Advanced sound playback utility with volume control, distance attenuation, and configuration.
 */
public class SoundManager {

    // ==================== CONFIGURATION ====================

    private static final float DEFAULT_VOLUME = 1.0F;
    private static final float DEFAULT_PITCH = 1.0F;
    private static final double MAX_SOUND_DISTANCE = 16.0;

    // ==================== BASIC PLAYBACK ====================

    /**
     * Play a sound at a block position.
     */
    public static void playSound(World world, BlockPos pos, SoundEvent sound) {
        playSound(world, pos, sound, SoundCategory.BLOCKS, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Play a sound at a block position with custom volume and pitch.
     */
    public static void playSound(World world, BlockPos pos, SoundEvent sound,
                                 SoundCategory category, float volume, float pitch) {
        if (!shouldPlaySound()) return;

        world.playSound(
                null,
                pos,
                sound,
                category,
                volume,
                pitch
        );
    }

    /**
     * Play a sound at an entity's position.
     */
    public static void playSound(Entity entity, SoundEvent sound) {
        playSound(entity, sound, SoundCategory.PLAYERS, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Play a sound at an entity's position with custom parameters.
     */
    public static void playSound(Entity entity, SoundEvent sound,
                                 SoundCategory category, float volume, float pitch) {
        if (!shouldPlaySound() || entity.getWorld().isClient) return;

        entity.getWorld().playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                sound,
                category,
                volume,
                pitch
        );
    }

    // ==================== ADVANCED PLAYBACK ====================

    /**
     * Play a sound only to a specific player.
     * ⭐ FIXED: Correct API for 1.21.1
     */
    public static void playSoundToPlayer(ServerPlayerEntity player, SoundEvent sound) {
        if (!shouldPlaySound()) return;

        // Use the correct method signature: playSound(SoundEvent, float volume, float pitch)
        player.playSound(sound, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

    /**
     * Play a sound only to a specific player with custom volume and pitch.
     * ⭐ FIXED: Correct API for 1.21.1
     */
    public static void playSoundToPlayer(ServerPlayerEntity player, SoundEvent sound, float volume, float pitch) {
        if (!shouldPlaySound()) return;

        player.playSound(sound, volume, pitch);
    }

    /**
     * Play a sound to all players within range of a position.
     */
    public static void playSoundToNearbyPlayers(ServerWorld world, BlockPos pos,
                                                SoundEvent sound, double range) {
        if (!shouldPlaySound()) return;

        Vec3d soundPos = Vec3d.ofCenter(pos);

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.getPos().distanceTo(soundPos) <= range) {
                // ⭐ FIXED: Use correct method signature
                player.playSound(sound, DEFAULT_VOLUME, DEFAULT_PITCH);
            }
        }
    }

    /**
     * Play a sound to all players within range with custom volume based on distance.
     */
    public static void playSoundToNearbyPlayersWithDistance(ServerWorld world, BlockPos pos,
                                                            SoundEvent sound, double range) {
        if (!shouldPlaySound()) return;

        Vec3d soundPos = Vec3d.ofCenter(pos);

        for (ServerPlayerEntity player : world.getPlayers()) {
            double distance = player.getPos().distanceTo(soundPos);
            if (distance <= range) {
                float volume = calculateVolumeByDistance(soundPos, player.getPos());
                player.playSound(sound, volume, DEFAULT_PITCH);
            }
        }
    }

    /**
     * Play a looping sound at a position (for continuous effects like treadmill running).
     */
    public static void playLoopingSound(World world, BlockPos pos, SoundEvent sound) {
        if (!shouldPlaySound()) return;

        // Note: For true looping, you'd use SoundInstance on the client side
        playSound(world, pos, sound, SoundCategory.BLOCKS, 0.5F, 1.0F);
    }

    /**
     * Stop a looping sound (placeholder for future implementation).
     */
    public static void stopLoopingSound(World world, BlockPos pos) {
        // TODO: Implement sound stopping mechanism with client-side sound instances
    }

    // ==================== SHELL CONSTRUCTION SOUNDS ====================

    public static void playShellConstructionStart(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.SHELL_CONSTRUCTION_START,
                SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public static void playShellConstructionProgress(World world, BlockPos pos, float progress) {
        float pitch = 0.8F + (progress * 0.4F); // Pitch increases with progress
        playSound(world, pos, SyncSounds.SHELL_CONSTRUCTION_PROGRESS,
                SoundCategory.BLOCKS, 0.5F, pitch);
    }

    public static void playShellConstructionComplete(World world, BlockPos pos) {
        SyncConfig config = Sync_mod.getConfig();
        if (!config.playShellCompletionSound()) return;

        playSound(world, pos, SyncSounds.SHELL_CONSTRUCTION_COMPLETE,
                SoundCategory.BLOCKS, 1.5F, 1.0F);

        // Add celebration sound after a delay
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getServer().execute(() -> {
                try {
                    Thread.sleep(500); // 0.5 second delay
                    playSound(world, pos, SyncSounds.SUCCESS,
                            SoundCategory.BLOCKS, 1.0F, 1.2F);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public static void playShellActivate(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.SHELL_ACTIVATE,
                SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public static void playShellSync(PlayerEntity player) {
        playSound(player, SyncSounds.SHELL_SYNC,
                SoundCategory.PLAYERS, 1.0F, 0.9F);
    }

    // ==================== TREADMILL SOUNDS ====================

    public static void playTreadmillStart(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.TREADMILL_START,
                SoundCategory.BLOCKS, 0.8F, 1.0F);
    }

    public static void playTreadmillRunning(World world, BlockPos pos, int runningTime) {
        // Increase pitch and volume as the entity runs longer
        float pitch = 0.9F + Math.min(runningTime / 1000.0F, 0.3F);
        float volume = 0.4F + Math.min(runningTime / 2000.0F, 0.4F);

        playSound(world, pos, SyncSounds.TREADMILL_RUNNING,
                SoundCategory.BLOCKS, volume, pitch);
    }

    public static void playTreadmillStop(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.TREADMILL_STOP,
                SoundCategory.BLOCKS, 0.6F, 0.8F);
    }

    public static void playTreadmillOverheat(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.TREADMILL_OVERHEAT,
                SoundCategory.BLOCKS, 1.2F, 0.7F);
    }

    // ==================== STORAGE SOUNDS ====================

    public static void playStorageOpen(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.STORAGE_OPEN,
                SoundCategory.BLOCKS, 0.8F, 1.0F);
    }

    public static void playStorageClose(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.STORAGE_CLOSE,
                SoundCategory.BLOCKS, 0.8F, 1.0F);
    }

    public static void playStoragePowerChange(World world, BlockPos pos, boolean poweredOn) {
        SoundEvent sound = poweredOn ? SyncSounds.STORAGE_POWER_ON : SyncSounds.STORAGE_POWER_OFF;
        playSound(world, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    // ==================== INTERACTION SOUNDS ====================

    public static void playFingerstickUse(PlayerEntity player) {
        playSound(player, SyncSounds.FINGERSTICK_USE,
                SoundCategory.PLAYERS, 0.5F, 1.0F);
    }

    /**
     * Play fingerstick sound to a specific server player.
     * ⭐ FIXED: Added overload for ServerPlayerEntity
     */
    public static void playFingerstickUse(ServerPlayerEntity player) {
        playSoundToPlayer(player, SyncSounds.FINGERSTICK_USE, 0.5F, 1.0F);
    }

    public static void playEnergyTransfer(World world, BlockPos pos) {
        playSound(world, pos, SyncSounds.ENERGY_TRANSFER,
                SoundCategory.BLOCKS, 0.3F, 1.2F);
    }

    public static void playError(PlayerEntity player) {
        playSound(player, SyncSounds.ERROR,
                SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    /**
     * Play error sound to a specific server player.
     * ⭐ FIXED: Added overload for ServerPlayerEntity
     */
    public static void playError(ServerPlayerEntity player) {
        playSoundToPlayer(player, SyncSounds.ERROR, 1.0F, 1.0F);
    }

    public static void playSuccess(PlayerEntity player) {
        playSound(player, SyncSounds.SUCCESS,
                SoundCategory.PLAYERS, 1.0F, 1.2F);
    }

    /**
     * Play success sound to a specific server player.
     * ⭐ FIXED: Added overload for ServerPlayerEntity
     */
    public static void playSuccess(ServerPlayerEntity player) {
        playSoundToPlayer(player, SyncSounds.SUCCESS, 1.0F, 1.2F);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Check if sounds should be played based on configuration.
     */
    private static boolean shouldPlaySound() {
        // Add config check here if needed
        return true;
    }

    /**
     * Calculate volume based on distance from player.
     */
    public static float calculateVolumeByDistance(Vec3d soundPos, Vec3d playerPos) {
        double distance = soundPos.distanceTo(playerPos);
        if (distance >= MAX_SOUND_DISTANCE) return 0.0F;

        return (float) (1.0 - (distance / MAX_SOUND_DISTANCE));
    }

    /**
     * Play a random variant of a sound (useful for repeated actions).
     */
    public static void playRandomVariant(World world, BlockPos pos, SoundEvent sound) {
        float randomPitch = 0.9F + (world.random.nextFloat() * 0.2F);
        playSound(world, pos, sound, SoundCategory.BLOCKS, DEFAULT_VOLUME, randomPitch);
    }

    /**
     * Play a random variant to a specific player.
     */
    public static void playRandomVariantToPlayer(ServerPlayerEntity player, SoundEvent sound) {
        float randomPitch = 0.9F + (player.getWorld().random.nextFloat() * 0.2F);
        playSoundToPlayer(player, sound, DEFAULT_VOLUME, randomPitch);
    }
}
