package io.github.coolmineman.plantinajar;

import io.github.coolmineman.plantinajar.mixin.CropBlockAccess;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class JarBlockEntity extends BlockEntity implements Tickable, NamedScreenHandlerFactory, InventoryProvider, BlockEntityClientSerializable {
    private static final int GROWTH_TIME = 10 * 20; 

    private final JarInventory inventory = new JarInventory();
    private final JarOutputInventory output = new JarOutputInventory(10);
    private boolean hasOutputed = false;
    private int tickyes = 0;

    public JarBlockEntity() {
        super(PlantInAJar.PLANT_JAR_ENTITY);
        inventory.addListener(no -> {
            if (world != null && !world.isClient) { //Do I really have to null check here mr. game?
                tickyes = 0;
                hasOutputed = false;
                output.clear();
                sync();
            }
        });
    }

    @Override
    public void tick() {
        if (canGrow(getRawPlant(), getBase())) {
            if (tickyes < GROWTH_TIME) {
                tickyes++;
            } else {
                if (!world.isClient && !hasOutputed) {
                    for (ItemStack stack : getPlant().getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.POSITION, pos).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                        output.addStack(stack);
                    }
                    hasOutputed = true;
                }
                if (output.isEmpty() && tickyes >= GROWTH_TIME) {
                    hasOutputed = false;
                    tickyes = 0;
                }
            }
        }
        
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

    public boolean canGrow(BlockState plant, BlockState base) {
        if (plant.getBlock() instanceof CropBlock) {
            try {
                return ((CropBlockAccess)plant.getBlock()).callCanPlantOnTop(base, null, null);
            } catch (Exception e) {
                System.out.println("Epic Hacky Code Failed Scream At ThatTrollzer in the Fabric Discord If You See This");
                e.printStackTrace();
            }
        }

        return false;
    }

    public BlockState getPlant() {
        BlockState rawState = getRawPlant();

        if (rawState.getBlock() instanceof CropBlock) {
            CropBlock c = ((CropBlock)rawState.getBlock());
            int age = (int) ( ((float)tickyes) / ((float)GROWTH_TIME ) * (c.getMaxAge() + 1));
            if (age > c.getMaxAge()) age = c.getMaxAge();
            return c.withAge(age);
        }
        
        return rawState;
    }

    public BlockState getRawPlant() {
        try {
            return ((BlockItem)inventory.getStack(0).getItem()).getBlock().getDefaultState();
        } catch (Exception no) {
            return Blocks.AIR.getDefaultState();
        }
    }

    public JarOutputInventory getOutput() {
        return output;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        DefaultedList<ItemStack> e = DefaultedList.ofSize(2, ItemStack.EMPTY);
        Inventories.fromTag(tag, e);
        inventory.setStack(0, e.get(0));
        inventory.setStack(1, e.get(1));
        tickyes = tag.getInt("tickyes");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        DefaultedList<ItemStack> e = DefaultedList.ofSize(2, ItemStack.EMPTY);
        e.set(0, inventory.getStack(0));
        e.set(1, inventory.getStack(1));
        Inventories.toTag(tag, e);
        tag.putInt("tickyes", tickyes);
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
        return inventory;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(null, tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }
    
}