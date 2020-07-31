package io.github.coolmineman.plantinajar;

import io.github.cottonmc.cotton.gui.ValidatedSlot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ValidatedSlotHack extends ValidatedSlot {

    public ValidatedSlotHack(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (this.getInventoryIndex() == 0) {
            if (GoodIdeaDetector.isGoodIdeaPlant(stack)) {
                return super.canInsert(stack);
            } else {
                return false;
            }
        } else if (this.getInventoryIndex() == 1) {
            if (GoodIdeaDetector.isGoodIdeaBase(stack)) {
                return super.canInsert(stack);
            } else {
                return false;
            }
        } else {
            System.out.println(this.getInventoryIndex());
            return false;
        }
    }

    @Override
    public int getMaxStackAmount() {
        return 1;
    }

    @Override
    public int getMaxStackAmount(ItemStack stack) {
        return 1;
    }
    
}