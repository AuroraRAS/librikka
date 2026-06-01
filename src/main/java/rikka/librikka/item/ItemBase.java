package rikka.librikka.item;

import net.minecraft.world.item.Item;

public abstract class ItemBase extends Item {
    public ItemBase(String name, Item.Properties properties) {
    	super(properties);
    }
    
    @Deprecated
    public ItemBase(String name, net.minecraft.world.item.CreativeModeTab group) {
    	this(name, new Item.Properties());
    }
}
