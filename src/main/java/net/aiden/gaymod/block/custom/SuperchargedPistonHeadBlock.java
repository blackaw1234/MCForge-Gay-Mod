package net.aiden.gaymod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

import static net.aiden.gaymod.block.ModBlocks.SUPERCHARGED_PISTON_BASE_BLOCK;

public class SuperchargedPistonHeadBlock extends PistonHeadBlock {

    public SuperchargedPistonHeadBlock(Properties properties) {
        super(properties);
    }

    private boolean isFittingBase(BlockState headState, BlockState baseState) {
        //Checks if type of block under matches, then if the base is extended, and then if it's aligned with the head
        //IMPORTANT: Checks MUST be performed in such a way to prevent checking for extended value in a block that
        //doesn't have it (e.g. checking a moving piston during contraction)
        return baseState.is(SUPERCHARGED_PISTON_BASE_BLOCK.get()) && baseState.getValue(SuperchargedPistonBaseBlock.EXTENDED) && baseState.getValue(FACING) == headState.getValue(FACING);
    }

    public void playerWillDestroy(Level p_60265_, BlockPos p_60266_, BlockState p_60267_, Player p_60268_) {
        if (!p_60265_.isClientSide && p_60268_.getAbilities().instabuild) {
            BlockPos blockpos = p_60266_.relative(p_60267_.getValue(FACING).getOpposite());
            if (this.isFittingBase(p_60267_, p_60265_.getBlockState(blockpos))) {
                p_60265_.destroyBlock(blockpos, false);
            }
        }

        super.playerWillDestroy(p_60265_, p_60266_, p_60267_, p_60268_);
    }

    public void onRemove(BlockState headState, Level p_60283_, BlockPos p_60284_, BlockState p_60285_, boolean p_60286_) {
        if (!headState.is(p_60285_.getBlock())) {
            super.onRemove(headState, p_60283_, p_60284_, p_60285_, p_60286_);
            BlockPos blockpos = p_60284_.relative(headState.getValue(FACING).getOpposite());
            if (this.isFittingBase(headState, p_60283_.getBlockState(blockpos))) {
                p_60283_.destroyBlock(blockpos, true);
            }
        }
    }

    public boolean canSurvive(BlockState headState, LevelReader levelReader, BlockPos headPos) {
        BlockState baseState = levelReader.getBlockState(headPos.relative(headState.getValue(FACING).getOpposite()));
        boolean isFittingBase = this.isFittingBase(headState, baseState);
        return  isFittingBase || baseState.is(Blocks.MOVING_PISTON) && baseState.getValue(FACING) == headState.getValue(FACING);
    }
}
