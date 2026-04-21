package com.wecib.util;

import com.wecib.model.CardType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TypeChart {

    private static final Map<CardType, Map<CardType, Double>> CHART = new EnumMap<>(CardType.class);

    static {
        for (CardType attacker : CardType.values()) {
            CHART.put(attacker, new EnumMap<>(CardType.class));
            for (CardType defender : CardType.values()) {
                CHART.get(attacker).put(defender, 1.0);
            }
        }

        setMatchup(CardType.FIRE, CardType.EARTH, 2.0);
        setMatchup(CardType.FIRE, CardType.WIND, 2.0);
        setMatchup(CardType.FIRE, CardType.WATER, 0.5);
        setMatchup(CardType.FIRE, CardType.SHADOW, 0.5);

        setMatchup(CardType.WATER, CardType.FIRE, 2.0);
        setMatchup(CardType.WATER, CardType.EARTH, 2.0);
        setMatchup(CardType.WATER, CardType.ELECTRIC, 0.5);
        setMatchup(CardType.WATER, CardType.WIND, 0.5);

        setMatchup(CardType.EARTH, CardType.ELECTRIC, 2.0);
        setMatchup(CardType.EARTH, CardType.FIRE, 0.5);
        setMatchup(CardType.EARTH, CardType.WIND, 0.5);

        setMatchup(CardType.ELECTRIC, CardType.WATER, 2.0);
        setMatchup(CardType.ELECTRIC, CardType.WIND, 2.0);
        setMatchup(CardType.ELECTRIC, CardType.EARTH, 0.5);

        setMatchup(CardType.WIND, CardType.EARTH, 2.0);
        setMatchup(CardType.WIND, CardType.SHADOW, 2.0);
        setMatchup(CardType.WIND, CardType.ELECTRIC, 0.5);
        setMatchup(CardType.WIND, CardType.FIRE, 0.5);

        setMatchup(CardType.SHADOW, CardType.LIGHT, 2.0);
        setMatchup(CardType.SHADOW, CardType.WIND, 2.0);
        setMatchup(CardType.SHADOW, CardType.FIRE, 0.5);

        setMatchup(CardType.LIGHT, CardType.SHADOW, 2.0);
        setMatchup(CardType.LIGHT, CardType.FIRE, 2.0);
        setMatchup(CardType.LIGHT, CardType.EARTH, 0.5);

        setMatchup(CardType.AURA, CardType.SHADOW, 2.0);
        setMatchup(CardType.AURA, CardType.LIGHT, 2.0);
        setMatchup(CardType.AURA, CardType.ELECTRIC, 0.5);
        setMatchup(CardType.AURA, CardType.EARTH, 0.5);

        setMatchup(CardType.DIRT, CardType.ELECTRIC, 2.0);
        setMatchup(CardType.DIRT, CardType.FIRE, 2.0);
        setMatchup(CardType.DIRT, CardType.WATER, 0.5);
        setMatchup(CardType.DIRT, CardType.WIND, 0.5);

        setMatchup(CardType.STEEL, CardType.EARTH, 2.0);
        setMatchup(CardType.STEEL, CardType.LIGHT, 2.0);
        setMatchup(CardType.STEEL, CardType.FIRE, 0.5);
        setMatchup(CardType.STEEL, CardType.ELECTRIC, 0.5);
    }

    private static void setMatchup(CardType attacker, CardType defender, double multiplier) {
        CHART.get(attacker).put(defender, multiplier);
    }

    public static double getMultiplier(CardType attackType, CardType defenderType) {
        return CHART.get(attackType).get(defenderType);
    }

    public static String getEffectivenessText(CardType attackType, CardType defenderType) {
        double mult = getMultiplier(attackType, defenderType);
        if (mult > 1.0) return "Super effective!";
        if (mult < 1.0) return "Not very effective...";
        return "";
    }

    /**
     * Tooltip: which attack types hit this defending type for double or half damage.
     */
    public static String defensiveHint(CardType defending) {
        List<String> weakTo = new ArrayList<>();
        List<String> resists = new ArrayList<>();
        for (CardType attackType : CardType.values()) {
            double m = getMultiplier(attackType, defending);
            if (m > 1.0) weakTo.add(attackType.displayName());
            else if (m < 1.0) resists.add(attackType.displayName());
        }
        Collections.sort(weakTo);
        Collections.sort(resists);
        StringBuilder sb = new StringBuilder();
        sb.append(defending.displayName()).append(" (defending):\n");
        if (!weakTo.isEmpty()) {
            sb.append("Weak to: ").append(String.join(", ", weakTo));
        }
        if (!weakTo.isEmpty() && !resists.isEmpty()) sb.append("\n");
        if (!resists.isEmpty()) {
            sb.append("Resists: ").append(String.join(", ", resists));
        }
        return sb.toString();
    }

    /**
     * Tooltip: how this attack type fares against other types (offense).
     */
    public static String offensiveHint(CardType attackType) {
        List<String> strong = new ArrayList<>();
        List<String> weak = new ArrayList<>();
        for (CardType def : CardType.values()) {
            double m = getMultiplier(attackType, def);
            if (m > 1.0) strong.add(def.displayName());
            else if (m < 1.0) weak.add(def.displayName());
        }
        Collections.sort(strong);
        Collections.sort(weak);
        StringBuilder sb = new StringBuilder();
        sb.append(attackType.displayName()).append(" attacks:\n");
        if (!strong.isEmpty()) {
            sb.append("Strong vs: ").append(String.join(", ", strong));
        }
        if (!strong.isEmpty() && !weak.isEmpty()) sb.append("\n");
        if (!weak.isEmpty()) {
            sb.append("Weak vs: ").append(String.join(", ", weak));
        }
        return sb.toString();
    }
}
