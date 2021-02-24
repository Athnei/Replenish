package com.atthnei.replenish.config;

import com.atthnei.replenish.Replenish;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Replenish.MOD_ID)
public class ReplenishConfig implements ConfigData {
    boolean toggleA = true;
    boolean toggleB = false;
}
