package com.glowbrick.printingpress.block.entity.block;

import javax.annotation.Nullable;

import com.glowbrick.printingpress.block.entity.ModBlockEntities;
import com.glowbrick.printingpress.item.ModItems;
import com.glowbrick.printingpress.screen.custom.PrintingPressMenu;

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
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class PrintingPressBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            switch(slot) {
                case 0:
                    if(Items.BOOK != stack.getItem()) {
                        return false;
                    }
                    break;
                case 1:
                    if(ModItems.TYPE_BLOCK.get() != stack.getItem()) {
                        return false;
                    }
                    break;
                case 2:
                    if(ModItems.MAGIC_INK_BOTTLE.get() != stack.getItem()) {
                        return false;
                    }
                    break;
                case 3:
                    return false;
            }
            return true;
        }
    };

    private static final int BLANK_TEMPLATE_SLOT = 0;
    private static final int MOVABLE_TYPE_SLOT = 1;
    private static final int INK_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private final int DEFAULT_MAX_PROGRESS = 200;
    private int inkLevel = 0;
    private int maxInkLevel = 1000;
    private final int DEFAULT_MAX_INK_LEVEL = 1000;
    private int currentInkMode = 0;
    private final int INK_MODE = 1;
    private final int MAGIC_INK_MODE = 2;

    public PrintingPressBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PRINTINGPRESS_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> PrintingPressBlockEntity.this.progress;
                    case 1 -> PrintingPressBlockEntity.this.maxProgress;
                    case 2 -> PrintingPressBlockEntity.this.inkLevel;
                    case 3 -> PrintingPressBlockEntity.this.maxInkLevel;
                    case 4 -> PrintingPressBlockEntity.this.currentInkMode;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch(pIndex) {
                    case 0: PrintingPressBlockEntity.this.progress = pValue;
                    case 1: PrintingPressBlockEntity.this.maxProgress = pValue;
                    case 2: PrintingPressBlockEntity.this.inkLevel = pValue;
                    case 3: PrintingPressBlockEntity.this.maxInkLevel = pValue;
                    case 4: PrintingPressBlockEntity.this.currentInkMode = pValue;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.printingpress.printing_press");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new PrintingPressMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("printing_press.progress", progress);
        pTag.putInt("printing_press.max_progress", maxProgress);
        pTag.putInt("printing_press.ink_level", inkLevel);
        pTag.putInt("printing_press.max_ink_level", maxInkLevel);
        pTag.putInt("printing_press.current_ink_mode", currentInkMode);

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("printing_press.progress");
        maxProgress = pTag.getInt("printing_press.max_progress");
        inkLevel = pTag.getInt("printing_press.ink_level");
        maxInkLevel = pTag.getInt("printing_press.max_ink_level");
        currentInkMode = pTag.getInt("printing_press.current_ink_mode");
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    // Printing Press Logic
    public void tick(Level level, BlockPos pPos, BlockState pState) {
        // Ink Deposit Logic
        ItemStack inkItem = new ItemStack(ModItems.INK_BOTTLE.get());
        ItemStack magicInkItem = new ItemStack(ModItems.MAGIC_INK_BOTTLE.get());
        ItemStack inkInput = this.itemHandler.getStackInSlot(INK_SLOT);

        if(inkInput.getItem() == inkItem.getItem() && currentInkMode != MAGIC_INK_MODE && inkLevel < maxInkLevel) {
            System.out.println("Ink Found");
            currentInkMode = INK_MODE;
            inkLevel = inkLevel + 200;
            itemHandler.extractItem(INK_SLOT, 1, false);
        }

        if(inkInput.getItem() == magicInkItem.getItem() && currentInkMode != INK_MODE && inkLevel < maxInkLevel) {
            System.out.println("Magic Ink Found");
            currentInkMode = MAGIC_INK_MODE;
            inkLevel = inkLevel + 200;
            itemHandler.extractItem(INK_SLOT, 1, false);
        }

        if(inkLevel > maxInkLevel) {
            inkLevel = maxInkLevel;
        }

        if(inkLevel == 0) {
            currentInkMode = 0;
        }

        // Crafting Logic
        if(hasRecipe() && isOutputSlotEmptyOrReceivable()) {
            increaseCraftingProgress();
            setChanged(level, pPos, pState);

            if(hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = DEFAULT_MAX_PROGRESS;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void craftItem() {
        ItemStack typeBlock = this.itemHandler.getStackInSlot(MOVABLE_TYPE_SLOT);
        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);

        inkLevel = inkLevel - 200;
      
        ItemEnchantments itemEnchantments = typeBlock.get(DataComponents.STORED_ENCHANTMENTS);
        output.set(DataComponents.STORED_ENCHANTMENTS, itemEnchantments);
        
        itemHandler.setStackInSlot(OUTPUT_SLOT, output);

        itemHandler.extractItem(BLANK_TEMPLATE_SLOT, 1, false);
    }

    private boolean hasRecipe() {
        ItemStack templateInput = new ItemStack(Items.BOOK);
        ItemStack movableTypeInput = new ItemStack(ModItems.TYPE_BLOCK.get());
        ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);

        if(currentInkMode == MAGIC_INK_MODE) {
            return canInsertAmountIntoOutputSlot(output.getCount()) && canInsertItemIntoOutputSlot(output) &&
                this.itemHandler.getStackInSlot(BLANK_TEMPLATE_SLOT).getItem() == templateInput.getItem() &&
                this.itemHandler.getStackInSlot(MOVABLE_TYPE_SLOT).getItem() == movableTypeInput.getItem() &&
                inkLevel > 0;
        }
        return false;
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getItem() == output.getItem();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }
}
