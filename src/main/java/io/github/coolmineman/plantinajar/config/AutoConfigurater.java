package io.github.coolmineman.plantinajar.config;

import java.util.HashMap;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import net.minecraft.util.Identifier;

@Config(name = "plantinajar")
public class AutoConfigurater implements ConfigData {
    boolean dropItems = true;
    int growthTime = 10;
    @ConfigEntry.Gui.Excluded
    HashMap<String, Integer> perItemGrowthTimes = new HashMap<>();

    public boolean shouldDropItems() {
        return this.dropItems;
    }

    public int getGrowthTime(Identifier i) {
        boolean needsResave = false;
        if (perItemGrowthTimes.get(i.toString()) == null) {
            needsResave = true;
        }
        Integer perItemGrowthTime = perItemGrowthTimes.computeIfAbsent(i.toString(), k -> -1);
        if (needsResave) ((ConfigManager)AutoConfig.getConfigHolder(AutoConfigurater.class)).save();
        return perItemGrowthTime <= 0 ? growthTime : perItemGrowthTime;
    }

}