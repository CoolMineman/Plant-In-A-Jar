package io.github.coolmineman.plantinajar.fake;

import java.util.OptionalLong;
import java.util.function.Predicate;

import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.WorldTickScheduler;

public class FakeServerWorld extends ServerWorld {
    private static final ObjectInstantiator<FakeServerWorld> FACTORY = (new ObjenesisStd()).getInstantiatorOf(FakeServerWorld.class);
    private static final DimensionType DIMENSION_TYPE = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 0, 256, 256, BlockTags.INFINIBURN_OVERWORLD, new Identifier("plantinajar", "fakenews"), 0.0F);

    private Long2ObjectOpenHashMap<BlockState> states;
    private ServerChunkManager chunkManager;
    private FakeServerTickScheduler fakeServerTickScheduler;
    private RegistryEntry<Biome> biome;
    private DynamicRegistryManager registryManager;

    private int bottomY;
    private int height;
    private int topYInclusive;

    private FakeServerWorld() {
        super(null, null, null, null, null, null, null, null, false, 0, null, false);
    }

    public static FakeServerWorld create(RegistryEntry<Biome> biome, DynamicRegistryManager registryManager) {
        FakeServerWorld thiz = FACTORY.newInstance();
        thiz.init();
        thiz.biome = biome;
        thiz.registryManager = registryManager;
        return thiz;
    }

    private void init() {
        this.chunkManager = FakeServerChunkManager.create(this);
        this.states = new Long2ObjectOpenHashMap<>();
        fakeServerTickScheduler = new FakeServerTickScheduler();
        this.height = DIMENSION_TYPE.getHeight();
        this.bottomY = DIMENSION_TYPE.getMinimumY();
        this.topYInclusive = this.bottomY + this.height - 1;
    }

    @Override
    public WorldProperties getLevelProperties() {
        return FakeWorldProperties.INSTANCE;
    }

    public Long2ObjectMap<BlockState> getBackingMap() {
        return states;
    }

    @Override
    public ServerChunkManager getChunkManager() {
        return chunkManager;
    }

    @Override
    public WorldTickScheduler<Block> getBlockTickScheduler() {
        return fakeServerTickScheduler;
    }

    public BlockState getBlockState(long pos) {
        return states.getOrDefault(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return getBlockState(pos.asLong());
    }

    public void setBlockState(long pos, BlockState state) {
        if (state.isAir()) {
            states.remove(pos);
        } else {
            states.put(pos, state);
        }
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        setBlockState(pos.asLong(), state);
        return true;
    }

    @Override
    public DimensionType getDimension() {
        return DIMENSION_TYPE;
    }

    @Override
    public int getTopY(Type heightmap, int x, int z) {
        int result = 0;
        Predicate<BlockState> predicate = heightmap.getBlockPredicate();
        for (long l : states.keySet()) {
            int px = BlockPos.unpackLongX(l);
            int py = BlockPos.unpackLongY(l);
            int pz = BlockPos.unpackLongZ(l);
            if (x == px && z == pz && predicate.test(states.get(l))) {
                result = Math.max(result, py);
            }
        }
        return result;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.getDefaultState();
    }

    @Override
    public RegistryEntry<Biome> getBiome(BlockPos pos) {
        return biome;
    }
    
    @Override
    public DynamicRegistryManager getRegistryManager() {
        return registryManager;
    }

    // See https://github.com/CaffeineMC/lithium-fabric/blob/fd862889af1c3b39606d6047c1fb61fbd0d12aa7/src/main/java/me/jellysquid/mods/lithium/mixin/world/inline_height/WorldMixin.java
    // Needed for lithium compat

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getBottomY() {
        return this.bottomY;
    }

    @Override
    public int countVerticalSections() {
        return ((this.topYInclusive >> 4) + 1) - (this.bottomY >> 4);
    }

    @Override
    public int getBottomSectionCoord() {
        return this.bottomY >> 4;
    }

    @Override
    public int getTopSectionCoord() {
        return (this.topYInclusive >> 4) + 1;
    }

    @Override
    public boolean isOutOfHeightLimit(BlockPos pos) {
        int y = pos.getY();
        return (y < this.bottomY) || (y > this.topYInclusive);
    }

    @Override
    public boolean isOutOfHeightLimit(int y) {
        return (y < this.bottomY) || (y > this.topYInclusive);
    }

    @Override
    public int getSectionIndex(int y) {
        return (y >> 4) - (this.bottomY >> 4);
    }

    @Override
    public int sectionCoordToIndex(int coord) {
        return coord - (this.bottomY >> 4);

    }

    @Override
    public int sectionIndexToCoord(int index) {
        return index + (this.bottomY >> 4);
    }

    @Override
    public int getTopY() {
        return this.topYInclusive + 1;
    }
}
