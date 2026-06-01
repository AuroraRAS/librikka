package rikka.librikka.model.loader;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import rikka.librikka.model.CodeBasedModel;

public class ModelGeometryWrapper implements IUnbakedGeometry<ModelGeometryWrapper> {
	protected final Map<String, Material> textures = new HashMap<>();
	protected final Function<ModelGeometryBakeContext, BakedModel> bakedModelSupplier;
	protected final Map<Field, String> textureFields = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public ModelGeometryWrapper(
			@Nullable JsonObject textureJsonObj, 
			@Nullable Class<?> textureSupplier,
			Function<ModelGeometryBakeContext, BakedModel> bakedModelSupplier) {
		this(textureJsonObj, textureSupplier, CodeBasedModel.class, 
				TextureAtlas.LOCATION_BLOCKS, bakedModelSupplier);
	}
	
	/**
	 * An implementation of NeoForge's IUnbakedGeometry, for easier dynamic model loading
	 * @param textureJsonObj A Json Object consists of TextureKey-ResourceLocation pairs
	 * @param textureSupplier {@link rikka.librikka.model.loader.Mark}
	 * @param scanEndClass if textureSupplier is not null, this field indicates 
	 * when {@link rikka.librikka.model.loader.Mark} scan stops
	 * @param atlasLoc the location of the texture atlas
	 * @param bakedModelSupplier A functional interface which returns an instance of a BakedModel
	 */
	public ModelGeometryWrapper(
			@Nullable JsonObject textureJsonObj, 
			@Nullable Class<?> textureSupplier,
			Class<?> scanEndClass,
			ResourceLocation atlasLoc,
			Function<ModelGeometryBakeContext, BakedModel> bakedModelSupplier) {
		this.bakedModelSupplier = bakedModelSupplier;
		
		if (textureJsonObj != null) {
			for (Entry<String, JsonElement> entry: textureJsonObj.entrySet()) {
				String key = entry.getKey();
				String textureLoc = entry.getValue().getAsString();
				ResourceLocation textureResLoc = ResourceLocation.parse(textureLoc);
				this.textures.put(key, new Material(atlasLoc, textureResLoc));
			}
		}
		
		if (textureSupplier != null) {
			EasyTextureLoader.foreachMarker(textureSupplier, scanEndClass, (cls, field)->{
				String textureName = EasyTextureLoader.getMarkerValue(field);
				if (!textureName.startsWith("#")) {
					String key = "resloc#" + textureName.toString();
					ResourceLocation textureResLoc = ResourceLocation.parse(textureName);
					this.textures.put(key, new Material(atlasLoc, textureResLoc));
				}
				this.textureFields.put(field, textureName);
			});
		}
	}

	@Override
	public BakedModel bake(IGeometryBakingContext owner, ModelBaker bakery,
			Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform,
			ItemOverrides overrides) {

		final ModelGeometryBakeContext context = new ModelGeometryBakeContext(
				owner, bakery, spriteGetter, modelTransform, overrides, null,
				this.textures);

		final BakedModel model = bakedModelSupplier.apply(context);
		
		if (model instanceof CodeBasedModel) {
			this.textureFields.forEach((field, textureName)->{
				TextureAtlasSprite texture;
	    		if (textureName.startsWith("#")) {
	    			texture = context.getTextureByKey(textureName.substring(1));
	    		} else {
	    			texture = context.getTexture(ResourceLocation.parse(textureName));
	    		}
	    		EasyTextureLoader.applyTexture(model, field, texture);
			});

			return ((CodeBasedModel) model).bake(context);
		}

		return model;
	}
}
