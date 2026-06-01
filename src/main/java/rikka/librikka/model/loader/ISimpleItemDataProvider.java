package rikka.librikka.model.loader;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;

/**
 * Implemented by super classes of net.neoforged.neoforge.client.model.generators.BlockModelBuilder,
 * Provides an easier way to generate JSON models for simple items
 * @author Rikka0w0
 */
public interface ISimpleItemDataProvider {
	/*
	 * These are implemented by net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
	 */
	BlockModelProvider models();
	ResourceLocation mcLoc(String textureName);
	ResourceLocation modLoc(String textureName);
	
	/*
	 * Utils
	 */

	default void registerSimpleItem(Block... blocks) {
		for (Block block: blocks)
			registerSimpleItem(block.asItem());
	}
	
	default void registerSimpleItem(Block block, String textureName) {
		registerSimpleItem(block.asItem(), textureName);
	}
	
	default void registerSimpleItem(Block block, ResourceLocation texture) {
		registerSimpleItem(block.asItem(), texture);
	}
	
	default void registerSimpleItem(Item... items) {
		for (Item item: items) {
			ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
			registerSimpleItem(item, "item/" + key.getPath());
		}
	}
	
	default void registerSimpleItem(Item item, String textureName) {
		registerSimpleItem(item, modLoc(textureName));
	}
	
	default void registerSimpleItem(Item item, ResourceLocation texture) {
		ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
		String itemModelPath = "item/" + key.getPath();
		BlockModelBuilder itemModelBuilder = models().getBuilder(itemModelPath);
		itemModelBuilder.parent(new ModelFile.ExistingModelFile(mcLoc("item/generated"), models().existingFileHelper));
		itemModelBuilder.texture("layer0", texture);
	}
}
