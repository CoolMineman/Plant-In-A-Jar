package io.github.coolmineman.plantinajar.fake;

import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;

public class FakeServerTickScheduler extends ServerTickScheduler {

    public FakeServerTickScheduler(ServerWorld world) {
        super(world, null, null, null);
    }

    @Override
    public void schedule(BlockPos pos, Object object, int delay, TickPriority priority) {
        // noop
    }
    
}
