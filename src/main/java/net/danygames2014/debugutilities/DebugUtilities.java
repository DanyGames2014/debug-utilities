package net.danygames2014.debugutilities;

import net.danygames2014.debugutilities.item.StateCycleMode;
import net.danygames2014.debugutilities.item.StateStickItem;
import net.danygames2014.uniwrench.api.WrenchMode;
import net.danygames2014.uniwrench.api.event.WrenchModeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class DebugUtilities {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static WrenchMode stateCycleMode;
    
    public static Item stateStickItem;

    @EventListener
    public void registerWrenchModes(WrenchModeRegistryEvent event) {
        stateCycleMode = new StateCycleMode(NAMESPACE.id("state_cycle"));
    }
    
    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        stateStickItem = new StateStickItem(NAMESPACE.id("state_stick")).setTranslationKey(NAMESPACE.id("state_stick"));
    }
}
