package net.aiden.gaymod.item;

import net.aiden.gaymod.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTab {
    public static final CreativeModeTab GAY_TAB = new CreativeModeTab("moveablemechanismstab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModBlocks.SUPERCHARGED_PISTON_BASE_BLOCK.get());
        }
    };
}
