package io.github.coolmineman.plantinajar.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class RegexComputation {
    private RegexComputation(){}

    private static List<Entry<Pattern, Integer>> growthModifierRegexPatterns = new ArrayList<>();

    static void init(Map<String, Integer> yes) {
        for (Entry<String, Integer> e : yes.entrySet()) {
            growthModifierRegexPatterns.add(new ImmutablePair<>(Pattern.compile(e.getKey()), e.getValue()));
        }
    }

    public static int getGrowthModifier(String i) {
        int result = 0;
        for (Entry<Pattern, Integer> e : growthModifierRegexPatterns) {
            if (e.getKey().matcher(i).find()) result += e.getValue();
        }
        return result;
    }
}