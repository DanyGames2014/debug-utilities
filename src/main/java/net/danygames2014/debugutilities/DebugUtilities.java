package net.danygames2014.debugutilities;

import net.danygames2014.debugutilities.item.DebugPickaxeItem;
import net.danygames2014.debugutilities.item.DebugSwordItem;
import net.danygames2014.debugutilities.item.StateCycleMode;
import net.danygames2014.debugutilities.item.StateStickItem;
import net.danygames2014.uniwrench.api.WrenchMode;
import net.danygames2014.uniwrench.api.event.WrenchModeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class DebugUtilities {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    public static WrenchMode stateCycleMode;
    
    public static Item stateStickItem;
    public static Item debugPickaxeItem;
    public static Item debugSwordItem;

    @EventListener
    public void registerWrenchModes(WrenchModeRegistryEvent event) {
        stateCycleMode = new StateCycleMode(NAMESPACE.id("state_cycle"));
    }
    
    @EventListener
    public void registerItems(ItemRegistryEvent event) {
        stateStickItem = new StateStickItem(NAMESPACE.id("state_stick")).setTranslationKey(NAMESPACE.id("state_stick"));

        ToolMaterial material = ToolMaterialFactory.create("debug", Integer.MAX_VALUE, Integer.MAX_VALUE, Float.MAX_VALUE, 9001);
        debugPickaxeItem = new DebugPickaxeItem(NAMESPACE.id("debug_pickaxe"), material).setTranslationKey(NAMESPACE.id("debug_pickaxe"));
        debugSwordItem = new DebugSwordItem(NAMESPACE.id("debug_sword"), material).setTranslationKey(NAMESPACE.id("debug_sword"));
    }
}
