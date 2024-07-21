package net.aiden.gaymod.world.biome;

import net.aiden.gaymod.GayMod;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.EndBiomes;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;




public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES =
            DeferredRegister.create(Registry.BIOME_REGISTRY, GayMod.MOD_ID);

    public static final RegistryObject<Biome> ASTEROID_FIELD = BIOMES.register(
            "asteroid_field", EndBiomes::theEnd);

    public static void register(IEventBus eventBus) {
        BIOMES.register(eventBus);
    }
}
