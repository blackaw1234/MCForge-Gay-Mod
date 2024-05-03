package net.aiden.gaymod.block;

import net.aiden.gaymod.GayMod;
import net.aiden.gaymod.block.custom.SuperchargedPistonHeadBlock;
import net.aiden.gaymod.item.ModCreativeModeTab;
import net.aiden.gaymod.block.custom.SuperchargedPistonBaseBlock;
import net.aiden.gaymod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, GayMod.MOD_ID);

    public static final RegistryObject<Block> SUPERCHARGED_PISTON_BASE_BLOCK = registerBlock("supercharged_piston",
            () -> new SuperchargedPistonBaseBlock(false, BlockBehaviour.Properties.of(Material.PISTON)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.GAY_TAB);

    public static final RegistryObject<Block> STICKY_SUPERCHARGED_PISTON_BASE_BLOCK = registerBlock("supercharged_piston_sticky",
            () -> new SuperchargedPistonBaseBlock(true, BlockBehaviour.Properties.of(Material.PISTON)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.GAY_TAB);

    public static final RegistryObject<Block> SUPERCHARGED_PISTON_HEAD_BLOCK = registerBlock("supercharged_piston_head",
            () -> new SuperchargedPistonHeadBlock( BlockBehaviour.Properties.of(Material.PISTON)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.GAY_TAB);

    public static final RegistryObject<Block> STICKY_SUPERCHARGED_PISTON_HEAD_BLOCK = registerBlock("supercharged_piston_head_sticky",
            () -> new SuperchargedPistonHeadBlock( BlockBehaviour.Properties.of(Material.PISTON)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.GAY_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab)
    {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}