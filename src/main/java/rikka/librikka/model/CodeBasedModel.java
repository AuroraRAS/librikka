package rikka.librikka.model;

import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.data.ModelData;
import rikka.librikka.model.loader.EasyTextureLoader;
import rikka.librikka.model.loader.ModelGeometryBakeContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An alternative to ISBRH in 1.7.10 and previous
 *
 * @author Rikka0_0
 */
public abstract class CodeBasedModel implements IDynamicBakedModel {
	public final static List<BakedQuad> emptyQuadList = ImmutableList.of();

    private final Map<ResourceLocation, Field> textures = new HashMap<>();

    protected CodeBasedModel() {
    	if (!skipLegacyTextureRegistration()) {
    		EasyTextureLoader.foreachMarker(this.getClass(), CodeBasedModel.class, (cls, field)-> {
    			String textureName = EasyTextureLoader.getMarkerValue(field);

    			// Skip all keys as they are not supported in the legacy texture registration scheme
    			if (!textureName.startsWith("#"))
    				this.textures.put(registerTexture(textureName), field);
    		});
    	}
    }
    
    protected boolean skipLegacyTextureRegistration() {
    	return false;
    }

    /**
     * @param texture file path, including domain
     * @return a key which can be used to retrieve the corresponding TextureAtlasSprite (like IIcon)
     */
    protected ResourceLocation registerTexture(String texture) {
    	return registerTexture(ResourceLocation.parse(texture));
    }

    protected ResourceLocation registerTexture(String namespace, String texture) {
        return registerTexture(ResourceLocation.fromNamespaceAndPath(namespace, texture));
    }
    
    protected ResourceLocation registerTexture(ResourceLocation resLoc) {
        this.textures.put(resLoc, null);
        return resLoc;
    }

    protected abstract void bake(Function<ResourceLocation, TextureAtlasSprite> textureRegistry);

    /**
     * To be called by {@link rikka.librikka.model.loader.ModelGeometryWrapper}
     * @param context An instance of {@link rikka.librikka.model.loader.ModelGeometryBakeContext}
     * @return the bakedmodel, usually be the current instance
     */
    public BakedModel bake(ModelGeometryBakeContext context) {
    	this.bake(context.textureGetter());
    	return this;
    }

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        return getQuads(state, side, rand, extraData);
    }

    public abstract List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData);
}
