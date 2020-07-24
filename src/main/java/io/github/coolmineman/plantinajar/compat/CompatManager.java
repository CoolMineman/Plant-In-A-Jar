package io.github.coolmineman.plantinajar.compat;

import java.util.ArrayList;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;

public class CompatManager {
    private static ArrayList<Compat> compats = new ArrayList<>();

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("terrestria")) {
            compats.add(new TerrestriaCompat());
        }
    }

    public static BlockState getTreeBlockWood(BlockState sappling) {
        for (Compat c : compats) {
            BlockState a = c.getTreeBlockWood(sappling);
            if (a != null) return a;
        }
        return null;
    }

    public static BlockState getTreeBlockLeaf(BlockState sappling) {
        for (Compat c : compats) {
            BlockState a = c.getTreeBlockLeaf(sappling);
            if (a != null) return a;
        }
        return null;
    }

    private CompatManager(){}
}