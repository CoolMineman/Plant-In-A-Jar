package io.github.coolmineman.plantinajar;

import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.inventory.Inventory;

public class WItemSlotHack extends WItemSlot {

    public WItemSlotHack(Inventory inventory, int startIndex, int slotsWide, int slotsHigh, boolean big) {
        super(inventory, startIndex, slotsWide, slotsHigh, big);
    }


    public static WItemSlot of1(Inventory inventory, int index) {
		return new WItemSlotHack(inventory, index, 1, 1, false);
	}
    
    @Override
    protected ValidatedSlot createSlotPeer(Inventory inventory, int index, int x, int y) {
        return new ValidatedSlotHack(inventory, index, x, y);
    }
}