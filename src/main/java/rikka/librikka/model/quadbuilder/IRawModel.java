package rikka.librikka.model.quadbuilder;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IRawModel<T extends IRawModel<?>> extends ITransformable<T>, IBakeable {
	T clone();
}
