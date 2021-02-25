package com.atthnei.replenish.config;

import com.atthnei.replenish.Replenish;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Replenish.MOD_ID)
public class ReplenishConfig implements ConfigData {
    public boolean skipHunger = false;
    public boolean skipHarmfulEffects = false;
}
