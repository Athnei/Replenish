package com.atthnei.replenish.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer.GlobalData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestConfig extends GlobalData {
    @ConfigEntry.Category("main_config")
    @ConfigEntry.Gui.TransitiveObject
    public MainConfig mainConfig = new MainConfig();

    @ConfigEntry.Category("ignore_config")
    @ConfigEntry.Gui.TransitiveObject
    public IgnoreConfig ignoreConfig = new IgnoreConfig();
}

@Config(name = "main_config")
class MainConfig implements ConfigData {
    public boolean ignoreItemsWithHarmfulEffects = false;
    public boolean includePotions = false;
}

@Config(name = "ignore_config")
class IgnoreConfig implements ConfigData {
    public boolean isIgnoreListAllowed = true;

    @ConfigEntry.Gui.Tooltip
    public List<String> ignoreList = new ArrayList<>(
            Arrays.asList("minecraft:rotten_flesh", "minecraft:chicken")
    );
}
