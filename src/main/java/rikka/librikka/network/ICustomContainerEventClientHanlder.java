package rikka.librikka.network;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ICustomContainerEventClientHanlder {
	@OnlyIn(Dist.CLIENT)
    void onDataArrivedFromServer(Object[] data);
}
