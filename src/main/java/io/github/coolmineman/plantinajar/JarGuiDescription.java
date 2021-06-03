package io.github.coolmineman.plantinajar;

import java.util.function.Supplier;

import io.github.cottonmc.cotton.gui.*;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerContext;

public class JarGuiDescription extends SyncedGuiDescription {

    public JarGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(PlantInAJar.EPIC_SCREEN_HAND_YES, syncId, playerInventory, getBlockInventory1(context, 2), getBlockPropertyDelegate(context));

        WGridPanel root = new WGridPanel();
		root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);
        WItemSlot itemSlot = WItemSlotHack.of1(blockInventory, 0);
        root.add(itemSlot, 4, 1);
        WItemSlot itemSlot2 = WItemSlotHack.of1(blockInventory, 1);
        root.add(itemSlot2, 4, 2);

        root.add(this.createPlayerInventoryPanel(), 0, 3);

        root.validate(this);
	}
	

    public static Inventory getBlockInventory1(ScreenHandlerContext ctx, int size) {
		return getBlockInventory2(ctx, () -> new SimpleInventory(size));
	}

	private static Inventory getBlockInventory2(ScreenHandlerContext ctx, Supplier<Inventory> fallback) {
		return ctx.get((world, pos) -> {
			BlockState state = world.getBlockState(pos);
			// Block b = state.getBlock();

			// if (b instanceof InventoryProvider) {
			// 	Inventory inventory = ((InventoryProvider)b).getInventory(state, world, pos);
			// 	if (inventory != null) {
			// 		return inventory;
			// 	}
			// }

			BlockEntity be = world.getBlockEntity(pos);
			if (be!=null) {
				if (be instanceof InventoryProvider) {
					Inventory inventory = ((InventoryProvider)be).getInventory(state, world, pos);
					if (inventory != null) {
						return inventory;
					}
				} else if (be instanceof Inventory) {
					return (Inventory)be;
				}
			}

			return fallback.get();
		}).orElseGet(fallback);
	}
}