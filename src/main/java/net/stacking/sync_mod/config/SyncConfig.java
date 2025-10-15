package net.stacking.sync_mod.config;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;

import java.util.List;
import java.util.UUID;

public interface SyncConfig {
    List<EnergyMapEntry> DEFAULT_ENERGY_MAP = List.of(
            EnergyMapEntry.of(EntityType.CHICKEN, 2),
            EnergyMapEntry.of(EntityType.PIG, 16),
            EnergyMapEntry.of(EntityType.COW, 18),
            EnergyMapEntry.of(EntityType.SHEEP, 14),
            EnergyMapEntry.of(EntityType.RABBIT, 6),
            EnergyMapEntry.of(EntityType.HORSE, 30),
            EnergyMapEntry.of(EntityType.DONKEY, 25),
            EnergyMapEntry.of(EntityType.LLAMA, 22),
            EnergyMapEntry.of(EntityType.CAT, 10),
            EnergyMapEntry.of(EntityType.PARROT, 4),
            EnergyMapEntry.of(EntityType.FOX, 12),
            EnergyMapEntry.of(EntityType.PANDA, 28),
            EnergyMapEntry.of(EntityType.WOLF, 24),
            EnergyMapEntry.of(EntityType.PLAYER, 20)
    );

    static SyncConfig resolve() {
        return new SyncConfig() {};
    }

    // ==================== SHELL CONSTRUCTION TIME ====================

    /**
     * Base construction time in ticks.
     * Default: 1200 ticks (60 seconds / 1 minute)
     * Range: 600-2400 ticks (30 seconds to 2 minutes)
     *
     * @return Construction time in ticks (20 ticks = 1 second)
     */
    default int shellConstructionTimeInTicks() {
        return 1200; // 60 seconds (1 minute) - default
    }

    /**
     * Minimum construction time in ticks.
     * Cannot be faster than this no matter the configuration.
     *
     * @return Minimum time in ticks (default: 600 = 30 seconds)
     */
    default int minShellConstructionTime() {
        return 600; // 30 seconds minimum
    }

    /**
     * Maximum construction time in ticks.
     * Cannot be slower than this no matter the configuration.
     *
     * @return Maximum time in ticks (default: 2400 = 2 minutes)
     */
    default int maxShellConstructionTime() {
        return 2400; // 2 minutes maximum
    }

    // Shell Limits
    default int maxShellsPerPlayer() {
        return 5;
    }

    default boolean notifyOnShellLimit() {
        return true;
    }

    // Difficulty-Based Damage
    default float getFingerstickDamageForDifficulty(Difficulty difficulty) {
        return switch (difficulty) {
            case PEACEFUL -> 10.0F;
            case EASY -> 15.0F;
            case NORMAL -> 20.0F;
            case HARD -> 25.0F;
        };
    }

    default float hardcoreFingerstickDamage() {
        return 40F;
    }

    // Notifications
    default NotificationMode shellConstructionNotificationMode() {
        return NotificationMode.BOTH;
    }

    default boolean showConstructorProgressDisplay() {
        return true;
    }

    default boolean playShellCompletionSound() {
        return true;
    }

    // Other Settings
    default boolean enableInstantShellConstruction() {
        return false;
    }

    default boolean warnPlayerInsteadOfKilling() {
        return false;
    }

    default long shellStorageCapacity() {
        return 320;
    }

    default long shellStorageConsumption() {
        return 16;
    }

    default List<EnergyMapEntry> energyMap() {
        return DEFAULT_ENERGY_MAP;
    }

    // Enums
    enum NotificationMode {
        NONE, CHAT, VISUAL, BOTH
    }

    // Energy Map Entry
    interface EnergyMapEntry {
        default String entityId() {
            return "minecraft:pig";
        }

        default long outputEnergyQuantity() {
            return 16;
        }

        default EntityType<?> getEntityType() {
            Identifier id = Identifier.tryParse(this.entityId());
            return id == null ? EntityType.PIG : Registries.ENTITY_TYPE.get(id);
        }

        static EnergyMapEntry of(EntityType<?> entityType, long outputEnergyQuantity) {
            return of(Registries.ENTITY_TYPE.getId(entityType).toString(), outputEnergyQuantity);
        }

        static EnergyMapEntry of(String id, long outputEnergyQuantity) {
            return new EnergyMapEntry() {
                @Override
                public String entityId() {
                    return id;
                }

                @Override
                public long outputEnergyQuantity() {
                    return outputEnergyQuantity;
                }
            };
        }
    }
}
