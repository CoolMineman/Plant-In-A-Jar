package io.github.coolmineman.plantinajar.config;

import java.util.HashMap;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Config(name = "plantinajar")
public class AutoConfigurater implements ConfigData {
    boolean dropItems = true;
    int growthTime = 10;
    @ConfigEntry.Gui.Excluded
    HashMap<String, Integer> perItemGrowthTimes = new HashMap<>();
    @ConfigEntry.Gui.Excluded
    HashMap<String, Integer> growthModifierRegexPatterns = new HashMap<>();

    public boolean shouldDropItems() {
        return this.dropItems;
    }

    public int getGrowthTime(Identifier i) {
        if (i.equals(Registry.BLOCK.getId(Blocks.AIR))) return growthTime;
        boolean needsResave = false;
        if (perItemGrowthTimes.get(i.toString()) == null) {
            needsResave = true;
        }
        Integer perItemGrowthTime = perItemGrowthTimes.computeIfAbsent(i.toString(), k -> -1);
        if (needsResave) ((ConfigManager)AutoConfig.getConfigHolder(AutoConfigurater.class)).save();
        int result = perItemGrowthTime;
        if (result <= 0) result = growthTime;
        result += RegexComputation.getGrowthModifier(i.toString());
        if (result <= 0) return 1;
        return result;
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();
        RegexComputation.init(growthModifierRegexPatterns);
    }

}