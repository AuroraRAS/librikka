package rikka.librikka.model;

import java.util.function.Function;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.minecraft.util.RandomSource;

import java.util.List;

/**
 * An invisible model
 *
 * @author Rikka0_0
 */
@OnlyIn(Dist.CLIENT)
public class GhostModel extends CodeBasedModel {
    private final ResourceLocation texture;
    private TextureAtlasSprite loadedTexture;

    public GhostModel() {
        texture = this.registerTexture("minecraft:block/iron_block");
    }
    
    public GhostModel(String particleTexture) {
        texture = this.registerTexture(particleTexture);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData) {
        return emptyQuadList;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.loadedTexture;
    }

    @Override
    protected void bake(Function<ResourceLocation, TextureAtlasSprite> registry) {
        loadedTexture = registry.apply(this.texture);
    }
}
