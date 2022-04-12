package io.github.coolmineman.plantinajar.fake;

import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class FakeServerChunkManager extends ServerChunkManager {
    private static final ObjectInstantiator<FakeServerChunkManager> FACTORY = (new ObjenesisStd()).getInstantiatorOf(FakeServerChunkManager.class);

    private FakeServerChunkManager() {
        super(null, null, null, null, null, null, 0, 0, false, null, null, null);
    }

    public static FakeServerChunkManager create() {
        return FACTORY.newInstance();
    }

    @Override
    public ChunkGenerator getChunkGenerator() {
        return null;
    }
}
