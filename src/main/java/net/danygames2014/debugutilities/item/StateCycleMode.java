package net.danygames2014.debugutilities.item;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.danygames2014.uniwrench.api.WrenchMode;
import net.danygames2014.uniwrench.util.HotbarTooltipHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.state.property.Property;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Collection;
import java.util.Iterator;

public class StateCycleMode extends WrenchMode {
    public StateCycleMode(Identifier identifier) {
        super(identifier);
    }
    
    @Override
    public boolean wrenchLeftClick(ItemStack stack, PlayerEntity player, boolean isSneaking, World world, int x, int y, int z, int side, WrenchMode wrenchMode) {
        if (world.isRemote) {
            return super.wrenchLeftClick(stack, player, isSneaking, world, x, y, z, side, wrenchMode);
        }
        
        cycleSelectedProperty(stack, world.getBlockState(x,y,z));
        
        String selectedProperty = getSelectedProperty(stack, world.getBlockState(x,y,z));
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            HotbarTooltipHelper.setTooltip("Selected Property: " + selectedProperty, 40);   
        } else {
            player.sendMessage("Selected Property: " + selectedProperty);
        }
        
        return true;
    }

    @Override
    public boolean wrenchRightClick(ItemStack stack, PlayerEntity player, boolean isSneaking, World world, int x, int y, int z, int side, WrenchMode wrenchMode) {
        if (world.isRemote) {
            return super.wrenchLeftClick(stack, player, isSneaking, world, x, y, z, side, wrenchMode);
        }

        // Get the selected property, if there is none, default to the first one
        String selectedPropertyName = getSelectedProperty(stack, world.getBlockState(x, y, z));
        
        if (selectedPropertyName.isBlank()) {
            return false;
        }

        if (selectedPropertyName.equals("meta")) {
            // Meta Cycle
            int meta = world.getBlockMeta(x, y, z);
            
            if (isSneaking) {
                meta = meta <= 0 ? 15 : meta - 1;                
            } else {
                meta = meta >= 15 ? 0 : meta + 1;
            }
            
            // Set the meta
            world.setBlockMeta(x, y, z, meta);
            world.blockUpdateEvent(x,y,z);

            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                HotbarTooltipHelper.setTooltip("Set property meta to " + meta, 40);
            } else {
                player.sendMessage("Set property meta to " + meta);
            }
            
        } else {
            // State Cycle
            BlockState state = world.getBlockState(x, y, z);
            Property<? extends Comparable<?>> property = findProperty(state, selectedPropertyName);
            state = state.cycle(property);
            
            // Set the block state back into world
            if (isSneaking) {
                world.setBlockStateWithoutNotifyingNeighbors(x, y, z, state);
            } else {
                world.setBlockState(x, y, z, state);
            }
            
            if (property != null) {
                if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                    HotbarTooltipHelper.setTooltip("Set property " + property.getName() + " to " + state.get(property).toString(), 40);
                } else {
                    player.sendMessage("Set property " + property.getName() + " to " + state.get(property).toString());
                }
            }
        }

        return true;
    }

    private Property<?> findProperty(BlockState state, String propertyName) {
        Collection<Property<?>> properties = state.getProperties();
        for (Property<?> property : properties) {
            if (propertyName.equals(property.getName())) {
                return property;
            }
        }

        return null;
    }

    private String getSelectedProperty(ItemStack stack, BlockState state) {
        NbtCompound selectedProperties = stack.getStationNbt().getCompound("selectedProperties");

        Identifier blockId = BlockRegistry.INSTANCE.getId(state.getBlock());

        // If we couldnt fetch the ID, return
        if (blockId == null) {
            return "";
        }

        // Get the selected property, if there is none, default to the first one
        String selectedPropertyName = selectedProperties.getString(blockId.toString());
        if (selectedPropertyName.isBlank()) {
            selectedPropertyName = selectFirstProperty(state);
            setSelectedProperty(stack, state, selectedPropertyName);
        }
        
        return selectedPropertyName;
    }
    
    private void setSelectedProperty(ItemStack stack, BlockState state, String propertyName) {
        NbtCompound selectedProperties = stack.getStationNbt().getCompound("selectedProperties");

        Identifier blockId = BlockRegistry.INSTANCE.getId(state.getBlock());

        // If we couldnt fetch the ID, return
        if (blockId == null) {
            return;
        }
        
        selectedProperties.putString(blockId.toString(), propertyName);
        stack.getStationNbt().put("selectedProperties", selectedProperties);
    }

    private void cycleSelectedProperty(ItemStack stack, BlockState state) {
        String currentName = getSelectedProperty(stack, state);
        String nextName = selectNextProperty(state, currentName);
        
        setSelectedProperty(stack, state, nextName);
    }
    
    private String selectFirstProperty(BlockState state) {
        Collection<Property<?>> props = state.getProperties();

        if (props.isEmpty()) {
            return "meta";
        } else {
            return props.iterator().next().getName();
        }
    }

    private String selectNextProperty(BlockState state, String currentName) {
        Collection<Property<?>> props = state.getProperties();

        if (props.isEmpty()) {
            return "meta";
        } else {
            Iterator<Property<?>> iter = props.iterator();
            
            while (iter.hasNext()) {
                Property<?> property = iter.next();
                if (property.getName().equals(currentName)) {
                    if (iter.hasNext()) {
                        return iter.next().getName();
                    } else {
                        return "meta";
                    }
                }
            }
            
            return props.iterator().next().getName();
        }
    }
}
