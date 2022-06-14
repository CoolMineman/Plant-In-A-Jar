package io.github.coolmineman.plantinajar.fake;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;

public class FakeServerTickScheduler extends WorldTickScheduler<Block> {

    public FakeServerTickScheduler() {
        super(thing -> false, null);
    }

    @Override
    public boolean isTicking(BlockPos pos, Block type) {
        return false;
    }

    @Override
    public void scheduleTick(OrderedTick<Block> orderedTick) {
        // noop
    }

    @Override
    public boolean isQueued(BlockPos pos, Block type) {
        return false;
    }

    @Override
    public int getTickCount() {
        return 0;
    }
    
}
