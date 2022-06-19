package io.github.coolmineman.plantinajar.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.plantinajar.GrowsMultiblockPlantBlock;
import io.github.coolmineman.plantinajar.PlantInAJar;
import io.github.coolmineman.plantinajar.fake.FakeServerWorld;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class TreeMan {
    private TreeMan() { }

    private static final BlockState dirt = Blocks.DIRT.getDefaultState();
    private static final BlockPos genPos = new BlockPos(0, 50, 0);
    private static final long x0y49z0 = BlockPos.asLong(0, 49, 0);
    private static final long x1y49z0 = BlockPos.asLong(1, 49, 0);
    private static final long x0y49z1 = BlockPos.asLong(0, 49, 1);
    private static final long x1y49z1 = BlockPos.asLong(1, 49, 1);
    private static final long x0y50z0 = BlockPos.asLong(0, 50, 0);
    private static final long x1y50z0 = BlockPos.asLong(1, 50, 0);
    private static final long x0y50z1 = BlockPos.asLong(0, 50, 1);
    private static final long x1y50z1 = BlockPos.asLong(1, 50, 1);
    private static final Direction[] DIRECTIONS = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, null};

    public static @Nullable Tree genTree(DynamicRegistryManager rm, GrowsMultiblockPlantBlock block, BlockState jarBase, RegistryEntry<Biome> biome, Random random, boolean server) {
        Block block2 = ((Block)block);
        try {
            // ChorusFlowerBlock
            FakeServerWorld world = FakeServerWorld.create(biome, rm);
            world.setBlockState(x0y49z0, dirt);
            BlockState state = block2.getDefaultState().contains(SaplingBlock.STAGE) ? block2.getDefaultState().with(SaplingBlock.STAGE, 1) : block2.getDefaultState();
            world.setBlockState(x0y50z0, state);
            block.doTheGrow(world, genPos, state, random);
            if (world.getBlockState(x0y50z0) == state) { //Try with jar base block
                world.setBlockState(x0y49z0, jarBase);
                block.doTheGrow(world, genPos, state, random);
            }
            if (world.getBlockState(x0y50z0) == state) { // Try as double tree
                world.setBlockState(x0y49z0, dirt);
                world.setBlockState(x1y49z0, dirt);
                world.setBlockState(x0y49z1, dirt);
                world.setBlockState(x1y49z1, dirt);
                world.setBlockState(x1y50z0, state);
                world.setBlockState(x0y50z1, state);
                world.setBlockState(x1y50z1, state);
                block.doTheGrow(world, genPos, state, random);
            }
            if (world.getBlockState(x0y50z0) == state) {
                return null; // Give up
            }
            return collectWorldToTree(world, server);
        } catch (Exception e) {
            String id = Registry.BLOCK.getId(block2).toString();
            PlantInAJar.CONFIG.autoConfigurater.forceBlackList.add(id);
            System.err.println("Error generating tree for block: " + id);
            e.printStackTrace();
            return null;
        }
    }

    private static Tree collectWorldToTree(FakeServerWorld world, boolean server) {
        int minx = 0;
        int miny = 50;
        int minz = 0;
        int maxx = 0;
        int maxy = 0;
        int maxz = 0;
        Long2ObjectMap<BlockState> worldMap = world.getBackingMap();
        ArrayList<BlockState> drops = new ArrayList<>();
        for (long l : worldMap.keySet()) {
            int x = BlockPos.unpackLongX(l);
            int y = BlockPos.unpackLongY(l);
            int z = BlockPos.unpackLongZ(l);
            if (x < minx) minx = x;
            if (z < minz) minz = z;
            if (x > maxx) maxx = x;
            if (y > maxy) maxy = y;
            if (z > maxz) maxz = z;

            if (y >= 50) {
                BlockState attempt = worldMap.get(l);
                boolean exists = false;
                for (BlockState existing : drops) {
                    if (existing.getBlock() == attempt.getBlock()) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    drops.add(attempt);
                }
            }
        }
        if (server) {
            worldMap.clear();
            return new Tree(null, drops);
        } else {
            return getClientTree(world, drops, minx, miny, minz, maxx, maxy, maxz);
        }
    }

    @Environment(EnvType.CLIENT)
    private static Tree getClientTree(FakeServerWorld world, ArrayList<BlockState> drops, int minx, int miny, int minz, int maxx, int maxy, int maxz) {
        Long2ObjectMap<BlockState> worldMap = world.getBackingMap();
        BlockRenderManager brm = MinecraftClient.getInstance().getBlockRenderManager();
        BlockColors bcs = MinecraftClient.getInstance().getBlockColors();
        QuadWithColor[][][][] treeArray = new QuadWithColor[maxx - minx + 1][maxy - miny + 1][maxz - minz + 1][0];
        ArrayList<QuadWithColor> ql = new ArrayList<>();
        Random random = new Random(42);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        BlockPos.Mutable cullTestPos = new BlockPos.Mutable();
        for (long l : worldMap.keySet()) {
            int x = BlockPos.unpackLongX(l);
            int y = BlockPos.unpackLongY(l);
            int z = BlockPos.unpackLongZ(l);
            if (y >= 50) {
                BlockState s = worldMap.get(l);
                BakedModel bm = brm.getModel(s);
                for (Direction d : DIRECTIONS) {
                    pos.set(x, y, z);
                    if (d == null || Block.shouldDrawSide(s, world, pos, d, cullTestPos.set(pos, d))) {
                        List<BakedQuad> bmq = bm.getQuads(s, d, random);
                        random.setSeed(42);
                        for (BakedQuad q : bmq) {
                            float r = 1;
                            float g = 1;
                            float b = 1;
                            if (q.hasColor()) {
                                int color = bcs.getColor(s, world, pos, 0);
                                r = MathHelper.clamp((color >> 16 & 255) / 255f, 0f, 1f);
                                g = MathHelper.clamp((color >> 8 & 255) / 255f, 0f, 1f);
                                b = MathHelper.clamp((color & 255) / 255f, 0f, 1f);
                            }
                            ql.add(new QuadWithColor(q, RenderLayers.getEntityBlockLayer(s, false), r, g, b));
                        }
                        
                    }
                }
                treeArray[x - minx][y - miny][z - minz] = ql.toArray(new QuadWithColor[ql.size()]);
                ql.clear();
            }
        }
        world.getBackingMap().clear();
        return new Tree(treeArray, drops);
    }
}
