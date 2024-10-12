package com.glowbrick.printingpress.block.entity.block;

import com.glowbrick.printingpress.block.entity.ModBlockEntities;
import com.glowbrick.printingpress.item.ModItems;
import com.glowbrick.printingpress.screen.custom.TypesetterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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

    private static final int TYPE_ITEM_SLOT = 0;
    private static final int TOBECOPIED_ITEM_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;
    private final int DEFAULT_MAX_PROGRESS = 72;

    //CompoundTag comptag = new CompoundTag();


    public TypesetterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
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
        Map<Holder<Enchantment>, Integer> enchants = getEnchantment();

        ItemStack output = new ItemStack(ModItems.TYPE_BLOCK.get());

        //output.enchant(enchats);

        itemHandler.extractItem(TYPE_ITEM_SLOT, 1,false);
        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem()));
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

    private Map<Holder<Enchantment>, Integer> getEnchantment() {

        ItemStack itemStack = this.itemHandler.getStackInSlot(TOBECOPIED_ITEM_SLOT);
        ItemEnchantments enchantments = itemStack.getTagEnchantments();
        Holder<Enchantment> enchantList[] = (Holder<Enchantment>[]) enchantments.keySet().toArray();
        Map<Holder<Enchantment>, Integer> enchantments1  = new HashMap<Holder<Enchantment>, Integer>();

        for (int i= 0; i < enchantList.length; i++){
            enchantments1.put(enchantList[i],itemStack.getEnchantmentLevel(enchantList[i]));
        }

        return enchantments1;
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
