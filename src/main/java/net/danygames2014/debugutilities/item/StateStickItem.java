package net.danygames2014.debugutilities.item;

import net.danygames2014.debugutilities.DebugUtilities;
import net.danygames2014.uniwrench.item.WrenchBase;
import net.modificationstation.stationapi.api.util.Identifier;

public class StateStickItem extends WrenchBase {
    public StateStickItem(Identifier identifier) {
        super(identifier);
        this.addWrenchMode(DebugUtilities.stateCycleMode);
        this.setUsageDelay(5);
    }
}
