package io.github.coolmineman.plantinajar;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class JarBlockEntity extends BlockEntity implements Tickable, NamedScreenHandlerFactory, InventoryProvider, BlockEntityClientSerializable {
    private final JarInventory inventory = new JarInventory();
    public int tickyes = 0;

    public JarBlockEntity() {
        super(PlantInAJar.PLANT_JAR_ENTITY);
        inventory.addListener(no -> {
            if (!world.isClient) {
                sync();
            }
        });
    }

    @Override
    public void tick() {
        if (++tickyes >= 8 * 20)
            tickyes = 0;
    }

    public BlockState getBase() {
        BlockState a;
        try {
            a = ((BlockItem)inventory.getStack(1).getItem()).getBlock().getDefaultState();
        } catch (Exception no) {
            return Blocks.AIR.getDefaultState();
        }
        
        if (a.isOf(Blocks.DIRT)) {
            a = Blocks.FARMLAND.getDefaultState();
        }
        if (a.isOf(Blocks.FARMLAND)) {
            a = a.with(FarmlandBlock.MOISTURE, 7);
        }
        return a;
    }

    public BlockState getPlant() {
        BlockState a;
        try {
            a = ((BlockItem)inventory.getStack(0).getItem()).getBlock().getDefaultState();
        } catch (Exception no) {
            return Blocks.AIR.getDefaultState();
        }
        return a;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        DefaultedList<ItemStack> e = DefaultedList.ofSize(2, ItemStack.EMPTY);
        Inventories.fromTag(tag, e);
        inventory.setStack(0, e.get(0));
        inventory.setStack(1, e.get(1));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        DefaultedList<ItemStack> e = DefaultedList.ofSize(2, ItemStack.EMPTY);
        e.set(0, inventory.getStack(0));
        e.set(1, inventory.getStack(1));
        Inventories.toTag(tag, e);
        return tag;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new JarGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return (SidedInventory)inventory;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(null, tag);
        System.out.println(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }
    
}