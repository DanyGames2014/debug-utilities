package net.danygames2014.debugutilities.item;

import net.danygames2014.debugutilities.DebugUtilities;
import net.danygames2014.uniwrench.item.WrenchBase;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugStickItem extends WrenchBase implements CustomTooltipProvider {
    public DebugStickItem(Identifier identifier) {
        super(identifier);
        this.addWrenchMode(DebugUtilities.stateCycleMode);
        this.addWrenchMode(DebugUtilities.inspectorMode);
        this.setUsageDelay(5);
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip) {
        return new String[]{
                originalTooltip,
                Formatting.GREEN + "Left-click to select property",
                Formatting.AQUA + "Right-click to change selected property"
        };
    }
}
