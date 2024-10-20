package com.glowbrick.printingpress.block.entity.block;

import com.glowbrick.printingpress.block.entity.ModBlockEntities;
import com.glowbrick.printingpress.item.ModItems;
import com.glowbrick.printingpress.screen.custom.TypesetterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TypesetterBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(3){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
            }
        }
    };

    private static final int TYPE_ITEM_SLOT = 1;
    private static final int TOBECOPIED_ITEM_SLOT = 0;
    private static final int OUTPUT_SLOT = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;
    private final int DEFAULT_MAX_PROGRESS = 72;



    public TypesetterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TYPESETTER_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch(pIndex) {
                    case 0 -> TypesetterBlockEntity.this.progress;
                    case 1 -> TypesetterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0: TypesetterBlockEntity.this.progress = pValue;
                    case 1: TypesetterBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.printingpress.typesetter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new TypesetterMenu(i,inventory,this,this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag ptag, HolderLookup.Provider registries) {
        ptag.put("inventory", itemHandler.serializeNBT(registries));
        ptag.putInt("typesetter.progress", progress);
        ptag.putInt("typesetter.max_progress", maxProgress);

        super.saveAdditional(ptag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("typesetter.progress");
        maxProgress = tag.getInt("typesetter.max_progress");
    }

    public void drops(){
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i< itemHandler.getSlots();i++){
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public void tick(Level level, BlockPos pPos, BlockState pState){
        if(hasRecipe() && isOutputEmptyOrReceivable()){
            increaseCraftingProgress();
            setChanged(level, pPos, pState);

            if(hasCraftingFinished()){
                craftItem();
                resetProgress();
            }
        }else{
            resetProgress();
        }
    }



    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
    }

    private void craftItem() {
        ItemStack itemStack2 = this.itemHandler.getStackInSlot(TOBECOPIED_ITEM_SLOT);
        ItemStack itemStack1 = new ItemStack(ModItems.TYPE_BLOCK.get());

        if (hasRecipe()){
            ItemEnchantments itemenchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack2);

            itemStack1.set(DataComponents.STORED_ENCHANTMENTS, itemenchantments);
            itemHandler.setStackInSlot(OUTPUT_SLOT, itemStack1);
        }

        itemHandler.extractItem(TYPE_ITEM_SLOT, 1, false);
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean isOutputEmptyOrReceivable() {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();

    }

    private boolean hasRecipe() {
        ItemStack input1 = new ItemStack(ModItems.MOVABLE_TYPE.get());
        ItemStack input2 = new ItemStack(Items.ENCHANTED_BOOK);
        ItemStack output = new ItemStack(ModItems.TYPE_BLOCK.get());

        return canInsertItemIntoOutputSlot(output) &&
                this.itemHandler.getStackInSlot(TYPE_ITEM_SLOT).getItem() == input1.getItem() &&
                this.itemHandler.getStackInSlot(TOBECOPIED_ITEM_SLOT).getItem() == input2.getItem();
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
         return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithFullMetadata(registries);
    }
}
