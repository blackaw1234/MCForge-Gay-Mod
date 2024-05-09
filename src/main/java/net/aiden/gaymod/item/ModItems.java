package net.aiden.gaymod.item;

import net.aiden.gaymod.GayMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static net.aiden.gaymod.block.ModBlocks.CANNABIS_BLOCK;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GayMod.MOD_ID);

    public static final RegistryObject<Item> CANNABIS = ITEMS.register("cannabis",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.GAY_TAB)));

    public static final RegistryObject<Item> CANNABIS_SEEDS = ITEMS.register("cannabis_seeds",
            () -> new ItemNameBlockItem(CANNABIS_BLOCK.get(),
            (new Item.Properties()).tab(ModCreativeModeTab.GAY_TAB)));



    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
