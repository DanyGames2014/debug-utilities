package net.danygames2014.debugutilities.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class DebugPickaxeItem extends TemplatePickaxeItem {
    public DebugPickaxeItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
        this.setMaxDamage(0);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        if (world.isRemote) {
            return super.useOnBlock(stack, user, world, x, y, z, side);
        }

        BlockState state = world.getBlockState(x, y, z);
        int meta = world.getBlockMeta(x, y, z);

        if (!state.isAir()) {
            state.getBlock().dropStacks(world, x, y, z, meta);
            world.setBlockStateWithNotify(x, y, z, States.AIR.get());
        }

        return false;
    }

    @Override
    public @NotNull String[] getTooltip(ItemStack itemStack, String originalTooltip) {
        return new String[]{
                originalTooltip,
                Formatting.GREEN + "Right-click on a block to insta-mine it",
        };
    }
}
