package io.github.coolmineman.plantinajar;

import io.github.cottonmc.cotton.gui.*;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class JarGuiDescription extends SyncedGuiDescription {

    public JarGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PlantInAJar.EPIC_SCREEN_HAND_YES, syncId, playerInventory, getBlockInventory(context, 2), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        root.add(itemSlot, 4, 1);
        WItemSlot itemSlot2 = WItemSlot.of(blockInventory, 1);
        root.add(itemSlot2, 4, 2);

        root.add(this.createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
    }
}