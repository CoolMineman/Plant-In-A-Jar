package io.github.coolmineman.plantinajar;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface GrowsMultiblockPlantBlock {
    public void doTheGrow(ServerWorld world, BlockPos pos, BlockState state, Random random); //avoid name collisions
}
