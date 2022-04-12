package io.github.coolmineman.plantinajar.fake;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;

public class FakeServerTickScheduler extends WorldTickScheduler {

    public FakeServerTickScheduler(ServerWorld world) {
        super(null, null);
    }

    @Override
    public void scheduleTick(OrderedTick orderedTick) {

    }

    //@Override
    public void schedule(BlockPos pos, Object object, int delay, TickPriority priority) {
        // noop
    }

}
