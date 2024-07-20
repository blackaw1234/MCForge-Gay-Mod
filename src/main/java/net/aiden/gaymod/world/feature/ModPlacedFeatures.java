package net.aiden.gaymod.world.feature;

import net.aiden.gaymod.GayMod;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, GayMod.MOD_ID);

    public static final RegistryObject<PlacedFeature> ENDSTONE_REDSTONE_ORE_PLACED = PLACED_FEATURES.register(
            "end_redstone_ore_placed", () -> new PlacedFeature(ModConfiguredFeatures.END_REDSTONE_ORE.getHolder().get(),
                    commonOrePlacement(7,
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80),
                                    VerticalAnchor.aboveBottom(80)))));

    public static final RegistryObject<PlacedFeature> MOON_ANCIENT_DEBRIS_PLACED = PLACED_FEATURES.register(
            "moon_ancient_debris_placed", () -> new PlacedFeature(ModConfiguredFeatures.MOON_ANCIENT_DEBRIS.getHolder().get(),
                    commonOrePlacement(7,
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80),
                                    VerticalAnchor.aboveBottom(80)))));

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountOnEveryLayerPlacement.of(p_195344_), p_195345_);
    }

    private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
