package net.stacking.sync_mod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.stacking.sync_mod.Sync_mod;
import net.stacking.sync_mod.config.SyncConfig;
import net.stacking.sync_mod.sound.SoundManager;
import net.stacking.sync_mod.sound.SyncSoundEvents;

public class ShellConstructorBlockEntity extends BlockEntity {
    private int ticksElapsed = 0;
    private int totalTicksRequired = 0;
    private boolean isConstructing = false;
    private boolean wasCompleted = false;
    private float lastProgressSound = 0;

    public ShellConstructorBlockEntity(BlockPos pos, BlockState state) {
        super(SyncBlockEntities.SHELL_CONSTRUCTOR, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, ShellConstructorBlockEntity be) {
        // Client-side animations can go here
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, ShellConstructorBlockEntity be) {
        if (!be.isConstructing) {
            return;
        }

        be.ticksElapsed++;

        // Calculate progress (0.0 to 1.0)
        float progress = (float) be.ticksElapsed / (float) be.totalTicksRequired;
        progress = MathHelper.clamp(progress, 0.0F, 1.0F);

        // Play progress sounds at 25%, 50%, 75%
        if (progress - be.lastProgressSound >= 0.25f) {
            SyncSoundEvents.onShellConstructionProgress(world, pos, progress);
            be.lastProgressSound = progress;
        }

        // Check if construction is complete
        if (be.ticksElapsed >= be.totalTicksRequired && !be.wasCompleted) {
            be.completeConstruction(world, pos);
        }

        // Auto-save every second
        if (be.ticksElapsed % 20 == 0) {
            be.markDirty();
        }
    }

    public ActionResult onUse(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        SyncConfig config = Sync_mod.getConfig();

        // If already constructing, show progress
        if (isConstructing) {
            if (wasCompleted) {
                // Shell is ready - activate it
                SoundManager.playShellActivate(world, pos);
                player.sendMessage(Text.translatable("sync.shell.ready"), false);

                // Reset for next shell
                resetConstruction();
            } else {
                // Show construction progress
                int progressPercent = (int) (getProgress() * 100);
                int secondsRemaining = (totalTicksRequired - ticksElapsed) / 20;
                player.sendMessage(
                        Text.literal(String.format("Shell Construction: %d%% (%d seconds remaining)",
                                progressPercent, secondsRemaining)),
                        true
                );
            }
            return ActionResult.SUCCESS;
        }

        // Start new construction
        startConstruction(world, pos, player, config);
        return ActionResult.SUCCESS;
    }

    private void startConstruction(World world, BlockPos pos, PlayerEntity player, SyncConfig config) {
        // Calculate construction time with bounds
        int constructionTime = config.shellConstructionTimeInTicks();
        int minTime = config.minShellConstructionTime();
        int maxTime = config.maxShellConstructionTime();

        // Apply bounds: 30 seconds minimum, 2 minutes maximum
        totalTicksRequired = MathHelper.clamp(constructionTime, minTime, maxTime);

        // Check if instant construction is enabled (creative mode)
        if (player.isCreative() && config.enableInstantShellConstruction()) {
            totalTicksRequired = 1; // Instant
        }

        // Start construction
        isConstructing = true;
        ticksElapsed = 0;
        wasCompleted = false;
        lastProgressSound = 0;

        // Play start sound
        SyncSoundEvents.onShellConstructionStart(world, pos);

        // Notify player
        int seconds = totalTicksRequired / 20;
        player.sendMessage(
                Text.literal(String.format("Shell construction started! (%d seconds)", seconds)),
                false
        );

        markDirty();
    }

    private void completeConstruction(World world, BlockPos pos) {
        wasCompleted = true;
        isConstructing = false;

        // Play completion sound and effects
        SyncSoundEvents.onShellConstructionComplete(world, pos);

        Sync_mod.LOGGER.info("Shell construction completed at {} after {} ticks", pos, ticksElapsed);
        markDirty();
    }

    private void resetConstruction() {
        isConstructing = false;
        wasCompleted = false;
        ticksElapsed = 0;
        totalTicksRequired = 0;
        lastProgressSound = 0;
        markDirty();
    }

    /**
     * Get the current construction progress as a value between 0.0 and 1.0.
     */
    public float getProgress() {
        if (totalTicksRequired == 0) {
            return 0.0F;
        }
        return MathHelper.clamp((float) ticksElapsed / (float) totalTicksRequired, 0.0F, 1.0F);
    }

    /**
     * Get the current construction progress as a percentage (0-100).
     */
    public int getProgressPercentage() {
        return (int) (getProgress() * 100);
    }

    /**
     * Check if construction is in progress.
     */
    public boolean isConstructing() {
        return isConstructing;
    }

    /**
     * Check if construction is complete and shell is ready.
     */
    public boolean isComplete() {
        return wasCompleted;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("ticksElapsed", ticksElapsed);
        nbt.putInt("totalTicksRequired", totalTicksRequired);
        nbt.putBoolean("isConstructing", isConstructing);
        nbt.putBoolean("wasCompleted", wasCompleted);
        nbt.putFloat("lastProgressSound", lastProgressSound);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        ticksElapsed = nbt.getInt("ticksElapsed");
        totalTicksRequired = nbt.getInt("totalTicksRequired");
        isConstructing = nbt.getBoolean("isConstructing");
        wasCompleted = nbt.getBoolean("wasCompleted");
        lastProgressSound = nbt.getFloat("lastProgressSound");
    }
}
