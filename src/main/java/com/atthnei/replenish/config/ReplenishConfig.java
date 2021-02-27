package com.atthnei.replenish.config;

import com.atthnei.replenish.Replenish;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Replenish.MOD_ID)
public class ReplenishConfig implements ConfigData {
    public boolean skipHarmfulEffects = false;
    public boolean skipPotions = true;
}
