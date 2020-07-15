package io.github.coolmineman.plantinajar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class JarBlock extends Block implements BlockEntityProvider {

    public JarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new JarBlockEntity();
    }

}