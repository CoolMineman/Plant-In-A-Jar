package io.github.coolmineman.plantinajar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(CropBlock.class)
public interface CropBlockAccess {
    @Invoker public boolean callCanPlantOnTop(BlockState floor, BlockView world, BlockPos pos);
}