package net.aiden.gaymod.world.feature;

import com.google.common.base.Suppliers;
import net.aiden.gaymod.GayMod;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, GayMod.MOD_ID);
    public static final Supplier<List<OreConfiguration.TargetBlockState>> ENDSTONE_REDSTONE_ORE = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), Blocks.REDSTONE_ORE.defaultBlockState())));

    public static final RegistryObject<ConfiguredFeature<?, ?>> END_REDSTONE_ORE = CONFIGURED_FEATURES.register(
            "end_redstone_ore", () -> new ConfiguredFeature<>(Feature.ORE,
                    new OreConfiguration(ENDSTONE_REDSTONE_ORE.get(), 7)));


    public static void register(IEventBus eventBus)
    {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
