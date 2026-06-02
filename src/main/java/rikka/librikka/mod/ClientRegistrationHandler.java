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

	@EventBusSubscriber(value = Dist.CLIENT, modid = LibRikka.MODID, bus = EventBusSubscriber.Bus.GAME)
	public static class GameBusEvents {
		@SubscribeEvent
		public static void onBlockHighLight(net.neoforged.neoforge.client.event.RenderHighlightEvent.Block event) {
			rikka.librikka.block.ICustomBoundingBox.onBlockHighLight(event);
		}
	}
}
