package rikka.librikka.blockentity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;

public abstract class BlockEntityBase extends BlockEntity {
	public BlockEntityBase(BlockEntityType<?> teType, BlockPos pos, BlockState blockState) {
		super(teType, pos, blockState);
	}

    @Override
    public void onChunkUnloaded() {
        this.setRemoved();
    }

    protected void markTileEntityForS2CSync() {
    	setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @OnlyIn(Dist.CLIENT)
    protected void markForRenderUpdate() {
    	requestModelDataUpdate();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
    }


    // When the world loads from disk, the server needs to send the BlockEntity information to the client
    //  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
    //  getUpdatePacket() and onDataPacket() are used for one-at-a-time BlockEntity updates
    //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet

    //Sync
    public void prepareS2CPacketData(CompoundTag nbt) {
    }

    @OnlyIn(Dist.CLIENT)
    public void onSyncDataFromServerArrived(CompoundTag nbt) {
    }

    @Override
    public final ClientboundBlockEntityDataPacket getUpdatePacket() {
    	CompoundTag tagCompound = new CompoundTag();
        this.prepareS2CPacketData(tagCompound);
        return ClientboundBlockEntityDataPacket.create(this, (be, provider) -> tagCompound);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public final void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        if (this.level.isClientSide) {
            this.onSyncDataFromServerArrived(pkt.getTag());
        }
    }

    /**
     * LevelChunk Sync
     */
    @Override
    public final CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    	CompoundTag nbt = super.getUpdateTag(registries);
        this.prepareS2CPacketData(nbt);
        return nbt;
    }

    /**
     * Called when the chunk's TE update tag, gotten from {@link #getUpdateTag(HolderLookup.Provider)}, is received on the client.
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);

        if (this.level.isClientSide) {
            this.onSyncDataFromServerArrived(tag);
        }
    }

    protected void collectModelData(ModelData.Builder builder) {

    }

    @Override
    public final ModelData getModelData() {
    	ModelData.Builder builder = ModelData.builder();
    	collectModelData(builder);
    	return builder.build();
    }
}
