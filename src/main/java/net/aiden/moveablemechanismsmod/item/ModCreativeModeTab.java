package net.aiden.moveablemechanismsmod.item;

import net.aiden.moveablemechanismsmod.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTab {
    public static final CreativeModeTab MOVEABLE_MECHANISMS_TAB = new CreativeModeTab("moveablemechanismstab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModBlocks.SUPERCHARGED_PISTON_BASE_BLOCK.get());
        }
    };
}
