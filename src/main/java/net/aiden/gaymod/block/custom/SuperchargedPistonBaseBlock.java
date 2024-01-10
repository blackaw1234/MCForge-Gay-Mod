package net.aiden.gaymod.block.custom;

import net.minecraft.world.level.block.piston.PistonBaseBlock;

/**
 * A piston that can push many more blocks than a normal piston, but requires more redstone signals to do so.
 *
 * @author Aiden
 */
public class SuperchargedPistonBaseBlock extends PistonBaseBlock {
    /**
     * Constructs a SuperchargedPistonBaseBlock object.
     *
     * @param isSticky   determines whether this piston is sticky.
     * @param properties behavioral properties provided during registration
     */
    public SuperchargedPistonBaseBlock(boolean isSticky, Properties properties) {
        super(isSticky, properties);
    }
}
