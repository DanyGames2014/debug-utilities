package net.danygames2014.debugutilities.item;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.template.item.TemplateSwordItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugSwordItem extends TemplateSwordItem {
    public DebugSwordItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
        this.setMaxDamage(0);
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack stack, String originalTooltip) {
        return new String[]{
                originalTooltip,
                Formatting.RED + "Right-click to kill all loaded entities",
                Formatting.DARK_RED + "Shift Right-click to kill all loaded entities and items",
        };
    }
}
