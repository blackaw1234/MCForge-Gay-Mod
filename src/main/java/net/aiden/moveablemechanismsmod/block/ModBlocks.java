package net.aiden.moveablemechanismsmod.block;

import net.aiden.moveablemechanismsmod.MoveableMechanismsMod;
import net.aiden.moveablemechanismsmod.item.ModCreativeModeTab;
import net.aiden.moveablemechanismsmod.block.custom.SuperchargedPistonBaseBlock;
import net.aiden.moveablemechanismsmod.item.ModItems;
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
            DeferredRegister.create(ForgeRegistries.BLOCKS, MoveableMechanismsMod.MOD_ID);

    public static final RegistryObject<Block> SUPERCHARGED_PISTON_BASE_BLOCK = registerBlock("supercharged_piston_base_block",
            () -> new SuperchargedPistonBaseBlock(false, BlockBehaviour.Properties.of(Material.PISTON)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.MOVEABLE_MECHANISMS_TAB);

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