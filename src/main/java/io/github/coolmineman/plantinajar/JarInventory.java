package io.github.coolmineman.plantinajar;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class JarInventory extends SimpleInventory implements SidedInventory {
    public JarInventory() {
        super(2);
    }

    // @Override
    // public boolean isValid(int slot, ItemStack stack) {
    //     if (slot == 0)
    //         return super.isValid(slot, stack) && GoodIdeaDetector.isGoodIdeaPlant(stack);
    //     if (slot == 1)
    //         return super.isValid(slot, stack) && GoodIdeaDetector.isGoodIdeaBase(stack);
    //     return false;
    // }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

}