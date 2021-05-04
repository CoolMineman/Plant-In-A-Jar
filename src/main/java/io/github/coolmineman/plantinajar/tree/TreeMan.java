package io.github.coolmineman.plantinajar.tree;

import java.util.HashMap;
import java.util.Random;

import io.github.coolmineman.plantinajar.fake.FakeServerWorld;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.DarkOakSaplingGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class TreeMan {
    private TreeMan() { }

    private static boolean loaded = false;
    private static final BlockPos y49 = new BlockPos(0, 49, 0);
    private static final BlockPos y50 = new BlockPos(0, 50, 0);
    private static final HashMap<Block, BlockState[][][]> treeMap = new HashMap<>();

    public static void initIfNeeded() {
        if (!loaded) {
            init();
            loaded = true;
        }
    }

    public static BlockState[][][] getTree(SaplingBlock sappling) {
        return treeMap.get(sappling);
    }

    @SuppressWarnings("all")
    private static void init() {
        FakeServerWorld world = FakeServerWorld.create();
        Random random = new Random(0);
        for (Block block : Registry.BLOCK) {
            if (block instanceof SaplingBlock) {
                SaplingBlock saplingBlock = (SaplingBlock)block;
                initTree(world, saplingBlock, random);
                world.getBackingMap().clear();
                random.setSeed(0);
            }
        }
    }

    private static void initTree(FakeServerWorld world, SaplingBlock block, Random random) {
        DarkOakSaplingGenerator
        world.setBlockState(y49, Blocks.DIRT.getDefaultState());
        BlockState state = block.getDefaultState().with(SaplingBlock.STAGE, 1);
        world.setBlockState(y50, state);
        block.generate(world, y50, state, random);
        int minx = 0;
        int miny = 50;
        int minz = 0;
        int maxx = 0;
        int maxy = 0;
        int maxz = 0;
        Long2ObjectMap<BlockState> worldMap = world.getBackingMap();
        for (long l : worldMap.keySet()) {
            int x = BlockPos.unpackLongX(l);
            int y = BlockPos.unpackLongY(l);
            int z = BlockPos.unpackLongZ(l);
            if (x < minx) minx = x;
            if (z < minz) minz = z;
            if (x > maxx) maxx = x;
            if (y > maxy) maxy = y;
            if (z > maxz) maxz = z;
        }
        BlockState[][][] treeArray = new BlockState[maxx - minx + 1][maxy - miny + 1][maxz - minz + 1];
        for (long l : worldMap.keySet()) {
            int x = BlockPos.unpackLongX(l);
            int y = BlockPos.unpackLongY(l);
            int z = BlockPos.unpackLongZ(l);
            if (y >= 50) {
                treeArray[x - minx][y - miny][z - minz] = worldMap.get(l);
            }
        }
        System.out.println(treeArray);
        treeMap.put(block, treeArray);
    }
}
