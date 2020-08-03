package io.github.coolmineman.plantinajar.compat;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class CompatManager {
    private static ArrayList<Compat> compats = new ArrayList<>();

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("terrestria")) {
            compats.add(new TerrestriaCompat());
        }
        if (FabricLoader.getInstance().isModLoaded("techreborn")) {
            compats.add(new TechRebornCompat());
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

    private CompatManager(){}
}