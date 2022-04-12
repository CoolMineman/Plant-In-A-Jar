package io.github.coolmineman.plantinajar.fake;

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
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.WorldTickScheduler;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.util.OptionalLong;
import java.util.function.Predicate;

public class FakeServerWorld extends ServerWorld {
    private static final ObjectInstantiator<FakeServerWorld> FACTORY = (new ObjenesisStd()).getInstantiatorOf(FakeServerWorld.class);
    private static final DimensionType DIMENSION_TYPE = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 0, 256, 256, BlockTags.INFINIBURN_OVERWORLD, new Identifier("plantinajar", "fakenews"), 0.0F);

    private Long2ObjectOpenHashMap<BlockState> states;
    private ServerChunkManager chunkManager;
    private WorldTickScheduler fakeServerTickScheduler;
    private RegistryEntry<Biome> biome;

    private WorldProperties properties;

    private FakeServerWorld() {
        super(null, null, null, null, null, null, null, null, false, 0, null, false);
    }

    public static FakeServerWorld create(RegistryEntry<Biome> biome, WorldProperties properties) {
        FakeServerWorld thiz = FACTORY.newInstance();
        thiz.init();
        thiz.biome = biome;
        thiz.properties = properties;
        return thiz;
    }

    private void init() {
        this.chunkManager = FakeServerChunkManager.create();
        this.states = new Long2ObjectOpenHashMap<>();
        fakeServerTickScheduler = new FakeServerTickScheduler(this);
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
    public WorldProperties getLevelProperties() {
        return properties;
    }
}
