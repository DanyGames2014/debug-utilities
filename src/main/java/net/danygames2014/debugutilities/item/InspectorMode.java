package net.danygames2014.debugutilities.item;

import net.danygames2014.uniwrench.api.WrenchMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;

public class InspectorMode extends WrenchMode {
    public InspectorMode(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean wrenchRightClick(ItemStack stack, PlayerEntity player, boolean isSneaking, World world, int x, int y, int z, int side, WrenchMode wrenchMode) {
        if (world.isRemote) {
            return super.wrenchRightClick(stack, player, isSneaking, world, x, y, z, side, wrenchMode);
        }

        int blockId = world.getBlockId(x, y, z);
        int blockMeta = world.getBlockMeta(x, y, z);
        BlockState state = world.getBlockState(x, y, z);

        if (state == null) {
            return false;
        }

        player.sendMessage("--- Inspection of block at §7x: §f" + x + " §7y: §f" + y + " §7z: §f" + z + " ---");
        player.sendMessage(Formatting.GRAY + "Registry Name: " + Formatting.WHITE + BlockRegistry.INSTANCE.getId(state.getBlock()));
        player.sendMessage(Formatting.GRAY + "Block ID: " + Formatting.WHITE + blockId + ":" + blockMeta);
        player.sendMessage(Formatting.GRAY + "Block Class: " + Formatting.WHITE + state.getBlock().getClass().getName());
        player.sendMessage(Formatting.GRAY + "Translation Key: " + Formatting.WHITE + state.getBlock().getTranslationKey());
        player.sendMessage(Formatting.GRAY + "Translated Name: " + Formatting.WHITE + state.getBlock().getTranslatedName());
        player.sendMessage(Formatting.GRAY + "Is Full Cube: " + Formatting.WHITE + state.getBlock().isFullCube());
        player.sendMessage(Formatting.GRAY + "Is Opaque: " + Formatting.WHITE + state.getBlock().isOpaque());
        player.sendMessage(Formatting.GRAY + "Block Hardness: " + Formatting.WHITE + state.getHardness(world, new BlockPos(x, y, z)));
        if (!state.getProperties().isEmpty()) {
            player.sendMessage(Formatting.GRAY + "State Properties: ");
            for (var property : state.getProperties()) {
                player.sendMessage(Formatting.GRAY + "  " + property.getName() + ": " + Formatting.WHITE + state.get(property));
            }
        }
        player.sendMessage("--- End of Inspection ---");

        return true;
    }
}
