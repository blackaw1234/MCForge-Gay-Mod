package net.aiden.gaymod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.aiden.gaymod.block.ModBlocks.SUPERCHARGED_PISTON_HEAD_BLOCK;

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

    private boolean moveBlock(Level level, BlockPos basePos, Direction pumpDirection) {
        BlockPos headPos = basePos.relative(pumpDirection);

        PistonStructureResolver pistonStructureResolver = new PistonStructureResolver(level, basePos, pumpDirection, true);
        if (!pistonStructureResolver.resolve()) {
            return false;
        } else {
            List<BlockPos> locationsToDestroy = pistonStructureResolver.getToDestroy();

            for (int i = locationsToDestroy.size() - 1; i >= 0; --i) {
                BlockPos locationToDestroy = locationsToDestroy.get(i);
                BlockState blockStateToDestroy = level.getBlockState(locationToDestroy);
                BlockEntity blockEntityToDestroy = blockStateToDestroy.hasBlockEntity() ? level.getBlockEntity(locationToDestroy) : null;

                dropResources(blockStateToDestroy, level, locationToDestroy, blockEntityToDestroy);
                level.setBlock(locationToDestroy, Blocks.AIR.defaultBlockState(), 18);
                level.gameEvent(GameEvent.BLOCK_DESTROY, locationToDestroy, GameEvent.Context.of(blockStateToDestroy));
                if (!blockStateToDestroy.is(BlockTags.FIRE)) {
                    level.addDestroyBlockEffect(locationToDestroy, blockStateToDestroy);
                }
            }

            PistonType pistonType = PistonType.DEFAULT;
            BlockState pumpHeadState = SUPERCHARGED_PISTON_HEAD_BLOCK.get().defaultBlockState().setValue(SuperchargedPistonHeadBlock.FACING, pumpDirection).setValue(SuperchargedPistonHeadBlock.TYPE, pistonType);
            BlockState movingPistonBlockState = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, pumpDirection).setValue(MovingPistonBlock.TYPE, PistonType.DEFAULT);

            level.setBlock(headPos, movingPistonBlockState, 68);
            level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(headPos, movingPistonBlockState, pumpHeadState, pumpDirection, true, true));

            level.updateNeighborsAt(headPos, SUPERCHARGED_PISTON_HEAD_BLOCK.get());

            return true;
        }
    }

    @Override
    public boolean triggerEvent(BlockState baseState, Level level, @NotNull BlockPos basePos, int extensionFlag, int direction) {
        Direction pumpDirection = baseState.getValue(FACING);

        // pump is not opposed by a powered piston
        if (extensionFlag == TRIGGER_EXTEND) {
            // trigger no event if pump is already extending
            if (net.minecraftforge.event.ForgeEventFactory.onPistonMovePre(level, basePos, pumpDirection, true)) {
                return false;
            }
            // trigger no event if pump cannot push the blocks in front of it
            if (!this.moveBlock(level, basePos, pumpDirection)) {
                return false;
            }

            level.setBlock(basePos, baseState.setValue(EXTENDED, true), 67);//replace the base with an extended version of itself
            level.playSound(null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.6F);
            level.gameEvent(null, GameEvent.PISTON_EXTEND, basePos);
        } else {
            // pump is contracting
            if (net.minecraftforge.event.ForgeEventFactory.onPistonMovePre(level, basePos, pumpDirection, false))
                return false;//trigger no event if piston is already contracting
            BlockEntity headEntity = level.getBlockEntity(basePos.relative(pumpDirection));
            // if the block entity at the head's position is a PistonMovingBlockEntity,
            if (headEntity instanceof PistonMovingBlockEntity) ((PistonMovingBlockEntity) headEntity).finalTick();

            BlockState blockstate = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, pumpDirection).setValue(MovingPistonBlock.TYPE, PistonType.DEFAULT);
            level.setBlock(basePos, blockstate, 20);
            level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(basePos, blockstate, this.defaultBlockState().setValue
                    (FACING, Direction.from3DDataValue(direction & 7)), pumpDirection, false, true));
            level.blockUpdated(basePos, blockstate.getBlock());// tell the level that a moving piston got updated at the position of the piston's base
            blockstate.updateNeighbourShapes(level, basePos, 2);
            level.removeBlock(basePos.relative(pumpDirection), false);// remove the piston head

            level.playSound(null, basePos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.15F + 0.6F);
            level.gameEvent(null, GameEvent.PISTON_CONTRACT, basePos);
        }

        net.minecraftforge.event.ForgeEventFactory.onPistonMovePost(level, basePos, pumpDirection, (extensionFlag == TRIGGER_EXTEND));
        return true;
    }
}
