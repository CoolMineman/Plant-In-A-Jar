package io.github.coolmineman.plantinajar.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(MushroomPlantBlock.class)
public class MushroomPlantBlockMixin implements GrowsMultiblockPlantBlock {
    @Override
    public void doTheGrow(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        ((MushroomPlantBlock)(Object)this).trySpawningBigMushroom(world, pos, state, random);
    }
}
