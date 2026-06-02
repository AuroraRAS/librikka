package rikka.librikka.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHelper<T extends BlockEntity> {
	private final BlockEntityType<T> beType;
	private final BEConstructor<T> beConstructor;

	private BlockEntityHelper(BEConstructor<T> beConstructor, Block... validBlocks) {
		this.beType = BlockEntityType.Builder.of(this::create, validBlocks).build(null);
		this.beConstructor = beConstructor;
	}

	public T create(BlockPos pos, BlockState state) {
		return beConstructor.create(beType, pos, state);
	}

	@FunctionalInterface
	public static interface BEConstructor<T extends BlockEntity> {
		T create(BlockEntityType<?> beType, BlockPos pos, BlockState state);
	}

	/**
	 * @param beClass
	 * @return a generated registry name for a class (e.g. BlockEntity) based on its classpath
	 */
	public static String getRegistryName(Class<?> beClass) {
		return beClass.getName().toLowerCase().replace('$', '.');
	}

	/**
	 * Create a BlockEntityType.
	 */
	public static <T extends BlockEntity> BlockEntityType<T> of(Class<T> beClass,
			BEConstructor<T> beConstructor, Block... validBlocks) {
		return (new BlockEntityHelper<T>(beConstructor, validBlocks)).beType;
	}

	/**
	 * @param <T>
	 * @param namespace e.g. {@code ModLoadingContext.get().getActiveNamespace()}
	 * @param beClass
	 * @return BlockEntityType from {@code beClass}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> BlockEntityType<T> getBEType(String namespace, Class<T> beClass) {
		String clsName = BlockEntityHelper.getRegistryName(beClass);
		ResourceLocation res = ResourceLocation.fromNamespaceAndPath(namespace, clsName);
		return (BlockEntityType<T>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(res);
	}
}
