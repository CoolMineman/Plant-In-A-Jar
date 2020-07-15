package io.github.coolmineman.plantinajar;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class JarBlockScreen extends CottonInventoryScreen<JarGuiDescription> {

    public JarBlockScreen(JarGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }
    
}