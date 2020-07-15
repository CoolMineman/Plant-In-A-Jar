package io.github.coolmineman.plantinajar;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;

public class JarBlockEntity extends BlockEntity implements Tickable {
    public int tickyes = 0;

    public JarBlockEntity() {
        super(PlantInAJar.PLANT_JAR_ENTITY);
    }

    @Override
    public void tick() {
        if (++tickyes >= 8 * 20) tickyes = 0;
    }
    
}