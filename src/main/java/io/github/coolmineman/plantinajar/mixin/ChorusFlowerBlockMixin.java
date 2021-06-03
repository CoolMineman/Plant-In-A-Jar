package io.github.coolmineman.plantinajar.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerBlockMixin implements GrowsMultiblockPlantBlock {
    @Override
    public void doTheGrow(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        ChorusFlowerBlock.generate(world, pos, random, 8);
    }
}
