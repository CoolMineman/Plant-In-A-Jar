package io.github.coolmineman.plantinajar.fake;

import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class FakeServerWorld extends ServerWorld {
    private static final ObjectInstantiator<FakeServerWorld> FACTORY = (new ObjenesisStd()).getInstantiatorOf(FakeServerWorld.class);

    private Long2ObjectOpenHashMap<BlockState> states;
    private ServerChunkManager chunkManager;
    private ServerTickScheduler fakeServerTickScheduler;

    private FakeServerWorld() {
        super(null, null, null, null, null, null, null, null, false, 0, null, false);
    }

    public static FakeServerWorld create() {
        FakeServerWorld thiz = FACTORY.newInstance();
        thiz.init();
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
    public ServerTickScheduler<Block> getBlockTickScheduler() {
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
    
}
