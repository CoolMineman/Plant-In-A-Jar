package io.github.coolmineman.plantinajar.config;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.annotations.Expose;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class AutoConfigurater {
    boolean dropItems = true;
    int growthTime = 30;
    ConcurrentHashMap<String, Integer> perItemGrowthTimes = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> growthModifierRegexPatterns = new ConcurrentHashMap<>();
    Set<String> blackList = ConcurrentHashMap.newKeySet();
    public transient Set<String> forceBlackList = ConcurrentHashMap.newKeySet();

    public boolean shouldDropItems() {
        return this.dropItems;
    }

    public int getGrowthTime(Identifier i) {
        if (i.equals(Registries.BLOCK.getId(Blocks.AIR))) return growthTime;
        boolean needsResave = false;
        if (perItemGrowthTimes.get(i.toString()) == null) {
            needsResave = true;
        }
        Integer perItemGrowthTime = perItemGrowthTimes.computeIfAbsent(i.toString(), k -> -1);
        if (needsResave) ConfigHolder.INSTANCE.save();
        int result = perItemGrowthTime;
        if (result <= 0) result = growthTime;
        result += RegexComputation.getGrowthModifier(i.toString());
        if (result <= 0) return 1;
        return result;
    }

    public void postLoad() {
        RegexComputation.init(growthModifierRegexPatterns);
    }

    public boolean isBlacklisted(String blockId) {
        return (blackList.contains(blockId) || forceBlackList.contains(blockId)) && !blockId.equals("minecraft:cherry_sapling");
    }
}