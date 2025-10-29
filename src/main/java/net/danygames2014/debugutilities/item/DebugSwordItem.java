package net.danygames2014.debugutilities.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateSwordItem;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class DebugSwordItem extends TemplateSwordItem implements CustomTooltipProvider {
    public DebugSwordItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, int blockId, int x, int y, int z, LivingEntity miner) {
        return true;
    }

    @Override
    public int getAttackDamage(Entity attackedEntity) {
        return 9001;
    }

    @Override
    public ItemStack use(ItemStack stack, World world, PlayerEntity player) {
        if (world.isRemote) {
            return stack;
        }

        for (int i = 0; i < world.entities.size(); i++) {
            Entity entity = (Entity) world.entities.get(i);
            if (!(entity instanceof PlayerEntity) && !(!player.isSneaking() && entity instanceof ItemEntity)) {
                entity.damage(player, 9000);
            }
        }
        return super.use(stack, world, player);
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
