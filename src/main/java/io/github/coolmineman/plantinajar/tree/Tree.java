package io.github.coolmineman.plantinajar.tree;

import java.util.ArrayList;
import java.util.List;

import io.github.coolmineman.plantinajar.fake.FakeServerWorld;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public final class Tree {
    private static final Direction[] DIRECTIONS = {Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, null};

    public final Long2ObjectMap<BlockState> blocks;

    public Tree(Long2ObjectMap<BlockState> blocks) {
        this.blocks = blocks;
    }
    

    public static Tree read(int[] t) {
        Long2ObjectArrayMap<BlockState> r = new Long2ObjectArrayMap<>(t.length / 3);
        for (int i = 0; i < t.length; i += 3) {
            r.put((((long) t[i]) << 32) | (t[i + 1] & 0xffffffffL), Block.getStateFromRawId(t[i + 2]));
        }
        return new Tree(r);
    }

    public int[] write() {
        int[] r = new int[blocks.size() * 3];
        int p = 0;
        for (Long2ObjectMap.Entry<BlockState> e : blocks.long2ObjectEntrySet()) {
            long pos = e.getLongKey();
            r[p] = (int) (pos >> 32);
            r[p + 1] = (int) (pos);
            r[p + 2] = Block.getRawIdFromState(e.getValue());
            p += 3;
        }
        return r;
    }

    public List<BlockState> getDrops() {
        ArrayList<BlockState> drops = new ArrayList<>();
        for (long l : blocks.keySet()) {
            int y = BlockPos.unpackLongY(l);

            if (y >= 50) {
                BlockState attempt = blocks.get(l);
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
        return drops;
    }

    @Environment(EnvType.CLIENT)
    public QuadWithColor[][][][] toQuads(World w, RegistryEntry<Biome> biome) {
        int minx = 0;
        int miny = 50;
        int minz = 0;
        int maxx = 0;
        int maxy = 0;
        int maxz = 0;
        for (long l : blocks.keySet()) {
            int x = BlockPos.unpackLongX(l);
            int y = BlockPos.unpackLongY(l);
            int z = BlockPos.unpackLongZ(l);
            if (x < minx) minx = x;
            if (z < minz) minz = z;
            if (x > maxx) maxx = x;
            if (y > maxy) maxy = y;
            if (z > maxz) maxz = z;
        }
        FakeServerWorld world = FakeServerWorld.create(biome, w);
        world.setBackingMap(blocks);
        BlockRenderManager brm = MinecraftClient.getInstance().getBlockRenderManager();
        BlockColors bcs = MinecraftClient.getInstance().getBlockColors();
        QuadWithColor[][][][] treeArray = new QuadWithColor[maxx - minx + 1][maxy - miny + 1][maxz - minz + 1][0];
        ArrayList<QuadWithColor> ql = new ArrayList<>();
        Random random = Random.create(42);
        BlockPos.Mutable pos = new BlockPos.Mutable();
        BlockPos.Mutable cullTestPos = new BlockPos.Mutable();
        for (long l : blocks.keySet()) {
            int x = BlockPos.unpackLongX(l);
            int y = BlockPos.unpackLongY(l);
            int z = BlockPos.unpackLongZ(l);
            if (y >= 50) {
                BlockState s = blocks.get(l);
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
        return treeArray;
    }
}
