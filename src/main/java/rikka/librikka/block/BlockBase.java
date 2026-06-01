package rikka.librikka.block;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import rikka.librikka.item.ItemBlockBase;

public abstract class BlockBase extends Block {
	@Nullable
	private final ItemBlockBase itemBlock;

	public BlockBase(String regName, Block.Properties props, Item.Properties itemProps) {
		this(regName, props, ItemBlockBase::new, itemProps);
	}

	@Deprecated
	public BlockBase(String regName, Block.Properties props, net.minecraft.world.item.CreativeModeTab group) {
		this(regName, props, new Item.Properties());
	}

    public BlockBase(String regName, Block.Properties props, ItemBlockBase.Constructor itemBlockProvider, Item.Properties itemProps) {
        super(props);
        this.itemBlock = itemBlockProvider == null ? null : itemBlockProvider.create(this, itemProps);
    }

    @Override
    public Item asItem() {
    	return this.itemBlock;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && state.getBlock() != newState.getBlock()) {
           worldIn.removeBlockEntity(pos);
        }
    }

    @Override
	@Nullable
	public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos) {
		if (!(this instanceof EntityBlock))
			return null;

		BlockEntity blockEntity = level.getBlockEntity(pos);
		return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
	}
}