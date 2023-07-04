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
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

public class FakeChunkGenerator extends ChunkGenerator {
    final FakeServerWorld world;

    public FakeChunkGenerator(FakeServerWorld world) {
        super(null, null);
        this.world = world;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return null;
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
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public int getMinimumY() {
        return world.getBottomY();
    }

    @Override
    public void carve(ChunkRegion var1, long var2, NoiseConfig var4, BiomeAccess var5, StructureAccessor var6, Chunk var7, Carver var8) {
        // noop
    }

    @Override
    public void buildSurface(ChunkRegion var1, StructureAccessor var2, NoiseConfig var3, Chunk var4) {
        // noop
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Executor var1, Blender var2, NoiseConfig var3, StructureAccessor var4, Chunk var5) {
        return null;
    }

    @Override
    public int getHeight(int x, int z, Type var3, HeightLimitView var4, NoiseConfig var5) {
        return world.getTopY(var3, x, z);
    }

    @Override
    public VerticalBlockSample getColumnSample(int var1, int var2, HeightLimitView var3, NoiseConfig var4) {
        return null;
    }

    @Override
    public void getDebugHudText(List<String> var1, NoiseConfig var2, BlockPos var3) {
        // noop
    }
    
}
