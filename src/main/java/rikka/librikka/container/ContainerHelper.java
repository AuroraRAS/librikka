package rikka.librikka.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import rikka.librikka.blockentity.BlockEntityHelper;

public class ContainerHelper {

	@SuppressWarnings("unchecked")
	public static <T extends AbstractContainerMenu> MenuType<T> getContainerType(String namespace, Class<T> containerClass) {
		String clsName = BlockEntityHelper.getRegistryName(containerClass);
		ResourceLocation res = ResourceLocation.fromNamespaceAndPath(namespace, clsName);

		return (MenuType<T>) BuiltInRegistries.MENU.get(res);
	}

	/**
	 * Create a MenuType with default feature flags
	 */
	public static <T extends AbstractContainerMenu> MenuType<T> of(Class<T> containerClass) {
		ConstructorSupplier<T> constructorSupplier = new ConstructorSupplier<T>(containerClass);
		return new MenuType<>(constructorSupplier, FeatureFlags.DEFAULT_FLAGS);
	}

    private static class ConstructorSupplier<T extends AbstractContainerMenu> implements MenuSupplier<T> {
    	private final Constructor<T> constructor;

		public ConstructorSupplier(Class<T> cClass) throws RuntimeException {
			try {
				this.constructor = cClass.getConstructor(int.class, Inventory.class);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("Failed to find the AbstractContainerMenu constructor");
			}
		}

		@Override
		public T create(int windowId, Inventory inv) {
			try {
				return constructor.newInstance(windowId, inv);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
    }
}
