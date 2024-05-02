package net.aiden.gaymod.block.custom;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static net.aiden.gaymod.block.ModBlocks.SUPERCHARGED_PISTON_HEAD_BLOCK;

/**
 * A piston that can push many more blocks than a normal piston, but requires more redstone signals to do so.
 *
 * @author Aiden
 */
public class SuperchargedPistonBaseBlock extends PistonBaseBlock {
    private final boolean isSticky;
    /**
     * Constructs a SuperchargedPistonBaseBlock object.
     *
     * @param isSticky   determines whether this piston is sticky.
     * @param properties behavioral properties provided during registration
     */
    public SuperchargedPistonBaseBlock(boolean isSticky, Properties properties) {
        super(isSticky, properties);
        this.isSticky = isSticky;
    }

    private boolean moveBlocks(Level level, BlockPos basePos, Direction pumpDirection, boolean b) {
        BlockPos blockpos = basePos.relative(pumpDirection);
        if (!b && level.getBlockState(blockpos).is(SUPERCHARGED_PISTON_HEAD_BLOCK.get())) {
            level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 20);
        }

        SuperchargedPistonStructureResolver pistonstructureresolver = new SuperchargedPistonStructureResolver(level, basePos, pumpDirection, b);
        if (!pistonstructureresolver.resolve()) {
            return false;
        } else {
            Map<BlockPos, BlockState> map = Maps.newHashMap();
            List<BlockPos> list = pistonstructureresolver.getToPush();
            List<BlockState> list1 = Lists.newArrayList();

            for(int i = 0; i < list.size(); ++i) {
                BlockPos blockpos1 = list.get(i);
                BlockState blockstate = level.getBlockState(blockpos1);
                list1.add(blockstate);
                map.put(blockpos1, blockstate);
            }

            List<BlockPos> list2 = pistonstructureresolver.getToDestroy();
            BlockState[] ablockstate = new BlockState[list.size() + list2.size()];
            Direction direction = b ? pumpDirection : pumpDirection.getOpposite();
            int j = 0;

            for(int k = list2.size() - 1; k >= 0; --k) {
                BlockPos blockpos2 = list2.get(k);
                BlockState blockstate1 = level.getBlockState(blockpos2);
                BlockEntity blockentity = blockstate1.hasBlockEntity() ? level.getBlockEntity(blockpos2) : null;
                dropResources(blockstate1, level, blockpos2, blockentity);
                level.setBlock(blockpos2, Blocks.AIR.defaultBlockState(), 18);
                level.gameEvent(GameEvent.BLOCK_DESTROY, blockpos2, GameEvent.Context.of(blockstate1));
                if (!blockstate1.is(BlockTags.FIRE)) {
                    level.addDestroyBlockEffect(blockpos2, blockstate1);
                }

                ablockstate[j++] = blockstate1;
            }

            for(int l = list.size() - 1; l >= 0; --l) {
                BlockPos blockpos3 = list.get(l);
                BlockState blockstate5 = level.getBlockState(blockpos3);
                blockpos3 = blockpos3.relative(direction);
                map.remove(blockpos3);
                BlockState blockstate8 = Blocks.MOVING_PISTON.defaultBlockState().setValue(FACING, pumpDirection);
                level.setBlock(blockpos3, blockstate8, 68);
                level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockpos3, blockstate8, list1.get(l), pumpDirection, b, false));
                ablockstate[j++] = blockstate5;
            }

            if (b) {
                PistonType pistontype = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
                BlockState blockstate4 = SUPERCHARGED_PISTON_HEAD_BLOCK.get().defaultBlockState().setValue(SuperchargedPistonHeadBlock.FACING, pumpDirection).setValue(SuperchargedPistonHeadBlock.TYPE, pistontype);
                BlockState blockstate6 = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, pumpDirection).setValue(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
                map.remove(blockpos);
                level.setBlock(blockpos, blockstate6, 68);
                level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(blockpos, blockstate6, blockstate4, pumpDirection, true, true));
            }

            BlockState blockstate3 = Blocks.AIR.defaultBlockState();

            for(BlockPos blockpos4 : map.keySet()) {
                level.setBlock(blockpos4, blockstate3, 82);
            }

            for(Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                BlockPos blockpos5 = entry.getKey();
                BlockState blockstate2 = entry.getValue();
                blockstate2.updateIndirectNeighbourShapes(level, blockpos5, 2);
                blockstate3.updateNeighbourShapes(level, blockpos5, 2);
                blockstate3.updateIndirectNeighbourShapes(level, blockpos5, 2);
            }

            j = 0;

            for(int i1 = list2.size() - 1; i1 >= 0; --i1) {
                BlockState blockstate7 = ablockstate[j++];
                BlockPos blockpos6 = list2.get(i1);
                blockstate7.updateIndirectNeighbourShapes(level, blockpos6, 2);
                level.updateNeighborsAt(blockpos6, blockstate7.getBlock());
            }

            for(int j1 = list.size() - 1; j1 >= 0; --j1) {
                level.updateNeighborsAt(list.get(j1), ablockstate[j++].getBlock());
            }

            if (b) {
                level.updateNeighborsAt(blockpos, SUPERCHARGED_PISTON_HEAD_BLOCK.get());
            }

            return true;
        }
    }

    @Override
    public void setPlacedBy(Level p_60172_, BlockPos p_60173_, BlockState p_60174_, LivingEntity p_60175_, ItemStack p_60176_) {
        if (!p_60172_.isClientSide) {
            checkIfExtend(p_60172_, p_60173_, p_60174_);
        }
    }

    @Override
    public void neighborChanged(BlockState p_60198_, Level p_60199_, BlockPos p_60200_, Block p_60201_, BlockPos p_60202_, boolean p_60203_) {
        if (!p_60199_.isClientSide) {
            checkIfExtend(p_60199_, p_60200_, p_60198_);
        }
    }

    @Override
    public void onPlace(BlockState p_60225_, Level p_60226_, BlockPos p_60227_, BlockState p_60228_, boolean p_60229_) {
        if (!p_60228_.is(p_60225_.getBlock())) {
            if (!p_60226_.isClientSide && p_60226_.getBlockEntity(p_60227_) == null) {
                checkIfExtend(p_60226_, p_60227_, p_60225_);
            }
        }
    }



    private void checkIfExtend(Level p_60168_, BlockPos p_60169_, BlockState p_60170_) {
        Direction direction = p_60170_.getValue(FACING);
        boolean flag = getNeighborSignal(p_60168_, p_60169_, direction);
        if (flag && !p_60170_.getValue(EXTENDED)) {
            if ((new SuperchargedPistonStructureResolver(p_60168_, p_60169_, direction, true)).resolve()) {
                p_60168_.blockEvent(p_60169_, this, 0, direction.get3DDataValue());
            }
        } else if (!flag && p_60170_.getValue(EXTENDED)) {
            BlockPos blockpos = p_60169_.relative(direction, 2);
            BlockState blockstate = p_60168_.getBlockState(blockpos);
            int i = 1;
            if (blockstate.is(Blocks.MOVING_PISTON) && blockstate.getValue(FACING) == direction) {
                BlockEntity blockentity = p_60168_.getBlockEntity(blockpos);
                if (blockentity instanceof PistonMovingBlockEntity) {
                    PistonMovingBlockEntity pistonmovingblockentity = (PistonMovingBlockEntity)blockentity;
                    if (pistonmovingblockentity.isExtending() && (pistonmovingblockentity.getProgress(0.0F) < 0.5F || p_60168_.getGameTime() == pistonmovingblockentity.getLastTicked() || ((ServerLevel)p_60168_).isHandlingTick())) {
                        i = 2;
                    }
                }
            }

            p_60168_.blockEvent(p_60169_, this, i, direction.get3DDataValue());
        }

    }

    private boolean getNeighborSignal(Level p_60178_, BlockPos p_60179_, Direction p_60180_) {
        for(Direction direction : Direction.values()) {
            if (direction != p_60180_ && p_60178_.hasSignal(p_60179_.relative(direction), direction)) {
                return true;
            }
        }

        if (p_60178_.hasSignal(p_60179_, Direction.DOWN)) {
            return true;
        } else {
            BlockPos blockpos = p_60179_.above();

            for(Direction direction1 : Direction.values()) {
                if (direction1 != Direction.DOWN && p_60178_.hasSignal(blockpos.relative(direction1), direction1)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean triggerEvent(BlockState baseState, Level level, @NotNull BlockPos basePos, int extensionFlag, int direction) {
        Direction pistonDirection = baseState.getValue(FACING);
        if (!level.isClientSide) {
            boolean flag = getNeighborSignal(level, basePos, pistonDirection);
            if (flag && (extensionFlag == 1 || extensionFlag == 2)) {
                level.setBlock(basePos, baseState.setValue(EXTENDED, Boolean.valueOf(true)), 2);
                return false;
            }

            if (!flag && extensionFlag == 0) {
                return false;
            }
        }

        if (extensionFlag == 0) {
            if (ForgeEventFactory.onPistonMovePre(level, basePos, pistonDirection, true)) return false;
            if (!moveBlocks(level, basePos, pistonDirection, true)) {
                return false;
            }

            level.setBlock(basePos, baseState.setValue(EXTENDED, Boolean.valueOf(true)), 67);
            level.playSound((Player)null, basePos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.6F);
            level.gameEvent((Entity)null, GameEvent.PISTON_EXTEND, basePos);
        } else if (extensionFlag == 1 || extensionFlag == 2) {
            if (ForgeEventFactory.onPistonMovePre(level, basePos, pistonDirection, false)) return false;
            BlockEntity blockentity1 = level.getBlockEntity(basePos.relative(pistonDirection));
            if (blockentity1 instanceof PistonMovingBlockEntity) {
                ((PistonMovingBlockEntity)blockentity1).finalTick();
            }

            BlockState blockstate = Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, pistonDirection).setValue(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
            level.setBlock(basePos, blockstate, 20);
            level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(basePos, blockstate, defaultBlockState().setValue(FACING, Direction.from3DDataValue(direction & 7)), pistonDirection, false, true));
            level.blockUpdated(basePos, blockstate.getBlock());
            blockstate.updateNeighbourShapes(level, basePos, 2);
            if (this.isSticky) {
                BlockPos blockpos = basePos.offset(pistonDirection.getStepX() * 2, pistonDirection.getStepY() * 2, pistonDirection.getStepZ() * 2);
                BlockState blockstate1 = level.getBlockState(blockpos);
                boolean flag1 = false;
                if (blockstate1.is(Blocks.MOVING_PISTON)) {
                    BlockEntity blockentity = level.getBlockEntity(blockpos);
                    if (blockentity instanceof PistonMovingBlockEntity) {
                        PistonMovingBlockEntity pistonmovingblockentity = (PistonMovingBlockEntity)blockentity;
                        if (pistonmovingblockentity.getDirection() == pistonDirection && pistonmovingblockentity.isExtending()) {
                            pistonmovingblockentity.finalTick();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1) {
                    if (extensionFlag != 1 || blockstate1.isAir() || !isPushable(blockstate1, level, blockpos, pistonDirection.getOpposite(), false, pistonDirection) || blockstate1.getPistonPushReaction() != PushReaction.NORMAL && !blockstate1.is(Blocks.PISTON) && !blockstate1.is(Blocks.STICKY_PISTON)) {
                        level.removeBlock(basePos.relative(pistonDirection), false);
                    } else {
                        moveBlocks(level, basePos, pistonDirection, false);
                    }
                }
            } else {
                level.removeBlock(basePos.relative(pistonDirection), false);
            }

            level.playSound((Player)null, basePos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.15F + 0.6F);
            level.gameEvent((Entity)null, GameEvent.PISTON_CONTRACT, basePos);
        }

        ForgeEventFactory.onPistonMovePost(level, basePos, pistonDirection, (extensionFlag == 0));
        return true;
    }
}
