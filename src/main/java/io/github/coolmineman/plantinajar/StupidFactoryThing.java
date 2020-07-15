package io.github.coolmineman.plantinajar;

import static net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.Factory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class StupidFactoryThing implements Factory<JarGuiDescription, JarBlockScreen> {

    @Override
    public JarBlockScreen create(JarGuiDescription handler, PlayerInventory inventory, Text title) {
        return new JarBlockScreen(handler, inventory.player, title);
    }

}