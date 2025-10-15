package net.stacking.sync_mod.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.stacking.sync_mod.Sync_mod;

/**
 * Central registry for all Sync mod sounds.
 * Uses Minecraft sounds by default, with custom sound support.
 */
public class SyncSounds {

    // ==================== CUSTOM SOUNDS (Registered) ====================
    // These will use custom .ogg files if present, otherwise fall back to Minecraft sounds

    public static final SoundEvent SHELL_CONSTRUCTION_START = registerCustom("shell_construction_start");
    public static final SoundEvent SHELL_CONSTRUCTION_PROGRESS = registerCustom("shell_construction_progress");
    public static final SoundEvent SHELL_CONSTRUCTION_COMPLETE = registerCustom("shell_construction_complete");
    public static final SoundEvent SHELL_ACTIVATE = registerCustom("shell_activate");
    public static final SoundEvent SHELL_DEACTIVATE = registerCustom("shell_deactivate");
    public static final SoundEvent SHELL_SYNC = registerCustom("shell_sync");

    public static final SoundEvent TREADMILL_START = registerCustom("treadmill_start");
    public static final SoundEvent TREADMILL_RUNNING = registerCustom("treadmill_running");
    public static final SoundEvent TREADMILL_STOP = registerCustom("treadmill_stop");
    public static final SoundEvent TREADMILL_OVERHEAT = registerCustom("treadmill_overheat");

    public static final SoundEvent STORAGE_OPEN = registerCustom("storage_open");
    public static final SoundEvent STORAGE_CLOSE = registerCustom("storage_close");
    public static final SoundEvent STORAGE_POWER_ON = registerCustom("storage_power_on");
    public static final SoundEvent STORAGE_POWER_OFF = registerCustom("storage_power_off");

    public static final SoundEvent FINGERSTICK_USE = registerCustom("fingerstick_use");
    public static final SoundEvent ENERGY_TRANSFER = registerCustom("energy_transfer");
    public static final SoundEvent ERROR = registerCustom("error");
    public static final SoundEvent SUCCESS = registerCustom("success");

    // ==================== MINECRAFT FALLBACK SOUNDS ====================
    // Used when custom sounds are not available

    public static class Fallback {
        // Shell sounds
        public static final SoundEvent SHELL_CONSTRUCTION_START = SoundEvents.BLOCK_ANVIL_USE;
        public static final SoundEvent SHELL_CONSTRUCTION_PROGRESS = SoundEvents.BLOCK_ANVIL_LAND;
        public static final SoundEvent SHELL_CONSTRUCTION_COMPLETE = SoundEvents.BLOCK_BEACON_ACTIVATE;
        public static final SoundEvent SHELL_ACTIVATE = SoundEvents.BLOCK_PORTAL_TRIGGER;
        public static final SoundEvent SHELL_DEACTIVATE = SoundEvents.BLOCK_PORTAL_TRAVEL;
        public static final SoundEvent SHELL_SYNC = SoundEvents.ENTITY_ENDERMAN_TELEPORT;

        // Treadmill sounds
        public static final SoundEvent TREADMILL_START = SoundEvents.BLOCK_PISTON_EXTEND;
        public static final SoundEvent TREADMILL_RUNNING = SoundEvents.BLOCK_METAL_STEP;
        public static final SoundEvent TREADMILL_STOP = SoundEvents.BLOCK_PISTON_CONTRACT;
        public static final SoundEvent TREADMILL_OVERHEAT = SoundEvents.BLOCK_FIRE_EXTINGUISH;

        // Storage sounds
        public static final SoundEvent STORAGE_OPEN = SoundEvents.BLOCK_CHEST_OPEN;
        public static final SoundEvent STORAGE_CLOSE = SoundEvents.BLOCK_CHEST_CLOSE;
        public static final SoundEvent STORAGE_POWER_ON = SoundEvents.BLOCK_BEACON_POWER_SELECT;
        public static final SoundEvent STORAGE_POWER_OFF = SoundEvents.BLOCK_BEACON_DEACTIVATE;

        // Interaction sounds
        public static final SoundEvent FINGERSTICK_USE = SoundEvents.ENTITY_PLAYER_HURT;
        public static final SoundEvent ENERGY_TRANSFER = SoundEvents.BLOCK_BEACON_AMBIENT;
        public static final SoundEvent ERROR = SoundEvents.ENTITY_VILLAGER_NO;
        public static final SoundEvent SUCCESS = SoundEvents.ENTITY_PLAYER_LEVELUP;
    }

    // ==================== SOUND CATEGORIES ====================

    public enum Category {
        SHELL_CONSTRUCTION,
        TREADMILL,
        STORAGE,
        INTERACTION,
        AMBIENT
    }

    // ==================== REGISTRATION ====================

    /**
     * Register a custom sound event.
     * If the .ogg file exists, it will be used. Otherwise, falls back to Minecraft sounds.
     */
    private static SoundEvent registerCustom(String name) {
        Identifier id = Identifier.of(Sync_mod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    /**
     * Initialize and register all sounds.
     * Call this during mod initialization.
     */
    public static void register() {
        Sync_mod.LOGGER.info("Registering {} custom sound events", 18);
    }

    // ==================== HELPER METHODS ====================

    /**
     * Get the appropriate sound for a category and event.
     */
    public static SoundEvent getSound(Category category, String event) {
        return switch (category) {
            case SHELL_CONSTRUCTION -> switch (event) {
                case "start" -> SHELL_CONSTRUCTION_START;
                case "progress" -> SHELL_CONSTRUCTION_PROGRESS;
                case "complete" -> SHELL_CONSTRUCTION_COMPLETE;
                default -> Fallback.SUCCESS;
            };
            case TREADMILL -> switch (event) {
                case "start" -> TREADMILL_START;
                case "running" -> TREADMILL_RUNNING;
                case "stop" -> TREADMILL_STOP;
                case "overheat" -> TREADMILL_OVERHEAT;
                default -> Fallback.TREADMILL_RUNNING;
            };
            case STORAGE -> switch (event) {
                case "open" -> STORAGE_OPEN;
                case "close" -> STORAGE_CLOSE;
                case "power_on" -> STORAGE_POWER_ON;
                case "power_off" -> STORAGE_POWER_OFF;
                default -> Fallback.STORAGE_OPEN;
            };
            case INTERACTION -> switch (event) {
                case "fingerstick" -> FINGERSTICK_USE;
                case "energy" -> ENERGY_TRANSFER;
                case "error" -> ERROR;
                case "success" -> SUCCESS;
                default -> Fallback.SUCCESS;
            };
            default -> Fallback.SUCCESS;
        };
    }

    /**
     * Check if a custom sound file exists for the given sound event.
     * This is a placeholder - actual implementation would check resource pack.
     */
    public static boolean hasCustomSound(SoundEvent sound) {
        // TODO: Implement resource pack checking
        return false;
    }
}
