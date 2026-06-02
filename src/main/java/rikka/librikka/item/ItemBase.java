package rikka.librikka.item;

import net.minecraft.world.item.Item;

public abstract class ItemBase extends Item {
    public final String registryName;

    public ItemBase(String name, Item.Properties properties) {
    	super(properties);
        this.registryName = name;
    }
    
    @Deprecated
    public ItemBase(String name, net.minecraft.world.item.CreativeModeTab group) {
    	this(name, new Item.Properties());
    }
}
