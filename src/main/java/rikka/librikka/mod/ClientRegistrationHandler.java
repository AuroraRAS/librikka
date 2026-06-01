package rikka.librikka.mod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import rikka.librikka.model.GeneratedModelLoader;

@EventBusSubscriber(value = Dist.CLIENT, modid = LibRikka.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistrationHandler {
	@SubscribeEvent
	public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register(GeneratedModelLoader.id, GeneratedModelLoader.instance);
	}
}
