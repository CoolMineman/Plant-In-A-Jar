package io.github.coolmineman.plantinajar.fake;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class FakeChunkGenerator extends ChunkGenerator {
    final FakeServerWorld world;

    public FakeChunkGenerator(FakeServerWorld world) {
        super(null, null, null);
        this.world = world;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return null;
    }

    @Override
    public ChunkGenerator withSeed(long var1) {
        return this;
    }

    @Override
    public MultiNoiseSampler getMultiNoiseSampler() {
        return null;
    }

    @Override
    public void carve(ChunkRegion var1, long var2, BiomeAccess var4, StructureAccessor var5, Chunk var6, Carver var7) {
        // noop
    }

    @Override
    public void buildSurface(ChunkRegion var1, StructureAccessor var2, Chunk var3) {
        // noop
    }

    @Override
    public void populateEntities(ChunkRegion var1) {
        // noop
    }

    @Override
    public int getWorldHeight() {
        return world.getHeight();
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor var1, Blender var2, StructureAccessor var3, Chunk var4) {
        return null;
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public int getMinimumY() {
        return world.getBottomY();
    }

    @Override
    public int getHeight(int var1, int var2, Type var3, HeightLimitView var4) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int var1, int var2, HeightLimitView var3) {
        return null;
    }

    @Override
    public void getDebugHudText(List<String> var1, BlockPos var2) {
        // noop
    }
    
}
