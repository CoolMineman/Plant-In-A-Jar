package io.github.coolmineman.plantinajar;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class JarOutputInventory extends SimpleInventory implements SidedInventory {

    public JarOutputInventory(int i) {
        super(i);
	}

	@Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            int[] returnvalue = new int[this.size() - 1];
            for (int i = 0; i < returnvalue.length; i++) {
                returnvalue[i] = i;
            }
            return returnvalue;
        } else return new int[0];
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN;
    }
    
}