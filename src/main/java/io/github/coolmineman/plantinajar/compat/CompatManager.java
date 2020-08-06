package io.github.coolmineman.plantinajar.compat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class CompatManager {
    private static final List<Compat> compats = new ArrayList<>();

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("terrestria")) {
            compats.add(new TerrestriaCompat());
        }
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            compats.add(new TechRebornCompat());
        }
        if (FabricLoader.getInstance().isModLoaded("cinderscapes")) {
            compats.add(new CinderscapesCompat());
        }
        if (FabricLoader.getInstance().isModLoaded("traverse")) {
            compats.add(new TraverseCompat());
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

    public static List<ItemStack> getExtraDrops(ItemStack plant) {
        ArrayList<ItemStack> result = new ArrayList<>();
        for (Compat c : compats) {
            for (ItemStack i : c.getExtraDrops(plant)) {
                result.add(i);
            }
        }
        return result;
    }  

    public static Optional<Boolean> isTree(BlockState plant) {
        for (Compat c : compats) {
            Optional<Boolean> b = c.isTree(plant);
            if (b.isPresent()) return b;
        }
        return Optional.empty();
    }

    private CompatManager(){}
}