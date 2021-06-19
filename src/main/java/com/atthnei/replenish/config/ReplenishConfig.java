package com.atthnei.replenish.config;

import com.atthnei.replenish.Replenish;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config(name = Replenish.MOD_ID)
public class ReplenishConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean ignoreItemsWithHarmfulEffects = false;

    @ConfigEntry.Gui.Tooltip
    public boolean includePotions = false;

    @ConfigEntry.Category("ignore_effects_category")
    @ConfigEntry.Gui.Tooltip
    public List<String> ignoreList = new ArrayList<>(
            Arrays.asList("minecraft:rotten_flesh", "minecraft:chicken")
    );

    @ConfigEntry.Category("clear_effects_category")
    @ConfigEntry.Gui.Tooltip
    public List<String> clearList = new ArrayList<>(
            Arrays.asList("minecraft:milk_bucket", "minecraft:honey_bottle")
    );
}
