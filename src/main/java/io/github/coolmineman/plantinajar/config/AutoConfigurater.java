package io.github.coolmineman.plantinajar.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = "plantinajar")
public class AutoConfigurater implements ConfigData {
    boolean dropItems = true;
    int growthTime = 10;

    public boolean shouldDropItems() {
        return this.dropItems;
    }

    public int getGrowthTime() {
        return this.growthTime;
    }

}