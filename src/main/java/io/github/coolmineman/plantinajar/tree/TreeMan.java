package io.github.coolmineman.plantinajar.tree;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import io.github.coolmineman.plantinajar.PlantInAJar;
import io.github.coolmineman.plantinajar.fake.FakeServerWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TreeMan {
    private TreeMan() { }

    private static final BlockPos genPos = new BlockPos(0, 50, 0);
    private static final long x0y49z0 = BlockPos.asLong(0, 49, 0);
    private static final long x1y49z0 = BlockPos.asLong(1, 49, 0);
    private static final long x0y49z1 = BlockPos.asLong(0, 49, 1);
    private static final long x1y49z1 = BlockPos.asLong(1, 49, 1);
    private static final long x0y50z0 = BlockPos.asLong(0, 50, 0);
    private static final long x1y50z0 = BlockPos.asLong(1, 50, 0);
    private static final long x0y50z1 = BlockPos.asLong(0, 50, 1);
    private static final long x1y50z1 = BlockPos.asLong(1, 50, 1);

    public static @Nullable Tree genTree(World w, GrowsMultiblockPlantBlock block, BlockState jarBase, RegistryEntry<Biome> biome, Random random) {
        Block block2 = ((Block)block);
        try {
            // ChorusFlowerBlock
            FakeServerWorld world = FakeServerWorld.create(biome, w);
            world.setBlockState(x0y49z0, jarBase);
            BlockState state = block2.getDefaultState().contains(SaplingBlock.STAGE) ? block2.getDefaultState().with(SaplingBlock.STAGE, 1) : block2.getDefaultState();
            world.setBlockState(x0y50z0, state);
            block.doTheGrow(world, genPos, state, random);
            if (world.getBlockState(x0y50z0) == state) { //Try with jar base block
                world.setBlockState(x0y49z0, jarBase);
                block.doTheGrow(world, genPos, state, random);
            }
            if (world.getBlockState(x0y50z0) == state) { // Try as double tree
                world.setBlockState(x0y49z0, jarBase);
                world.setBlockState(x1y49z0, jarBase);
                world.setBlockState(x0y49z1, jarBase);
                world.setBlockState(x1y49z1, jarBase);
                world.setBlockState(x1y50z0, state);
                world.setBlockState(x0y50z1, state);
                world.setBlockState(x1y50z1, state);
                block.doTheGrow(world, genPos, state, random);
            }
            if (world.getBlockState(x0y50z0) == state) { // Yeet
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        world.setBlockState(BlockPos.asLong(x, 50, z), state);
                    }
                }
                for (int x = -5; x <= 5; x++) {
                    for (int z = -5; z <= 5; z++) {
                        world.setBlockState(BlockPos.asLong(x, 49, z), jarBase);
                    }
                }
                block.doTheGrow(world, genPos, state, random);
                BlockState air = Blocks.AIR.getDefaultState();
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        if (world.getBlockState(BlockPos.asLong(x, 50, z)) == state) {
                            world.setBlockState(BlockPos.asLong(x, 50, z), air);
                        }
                    }
                }
                for (int x = -5; x <= 5; x++) {
                    for (int z = -5; z <= 5; z++) {
                        if (world.getBlockState(BlockPos.asLong(x, 49, z)) == jarBase) {
                            world.setBlockState(BlockPos.asLong(x, 49, z), air);
                        }
                    }
                }
            }
            if (world.getBackingMap().isEmpty()) {
                return null; // Give up
            }
            return new Tree(world.getBackingMap());
        } catch (Exception e) {
            String id = Registries.BLOCK.getId(block2).toString();
            PlantInAJar.CONFIG.autoConfigurater.forceBlackList.add(id);
            System.err.println("Error generating tree for block: " + id);
            e.printStackTrace();
            return null;
        }
    }
}
