package rikka.librikka.mod;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.api.distmarker.Dist;

@Mod(LibRikka.MODID)
public class LibRikka {
	public final static String MODID = "librikka";
	
	public static CommonProxy proxy = FMLEnvironment.dist == Dist.CLIENT ? new ClientProxy() : new CommonProxy();
	
	public LibRikka() {
		// Event subscriptions are handled automatically via annotations or event bus listeners.
	}
}
