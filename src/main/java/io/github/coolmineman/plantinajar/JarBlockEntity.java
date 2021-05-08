package io.github.coolmineman.plantinajar;

import java.util.Random;

import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import io.github.coolmineman.plantinajar.mixin.PlantBlockAccess;
import io.github.coolmineman.plantinajar.tree.Tree;
import io.github.coolmineman.plantinajar.tree.TreeMan;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CoralBlockBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.RootsBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SproutsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class JarBlockEntity extends BlockEntity implements Tickable, NamedScreenHandlerFactory, InventoryProvider, BlockEntityClientSerializable {
    private Integer clientGrowthTime = null;
    public int getGrowthTime() {
        if (clientGrowthTime != null) return clientGrowthTime;
        return PlantInAJar.CONFIG.getGrowthTime(Registry.BLOCK.getId(getRawPlant().getBlock())) * 20;
    }

    private final JarInventory inventory = new JarInventory();
    private final JarOutputInventory output = new JarOutputInventory(10);
    private boolean hasOutputed = false;
    private int tickyes = 0;
    private Random random = new Random();
    private long seed = 0;
    public Block treeCacheKey;
    public Tree tree = null;

    public JarBlockEntity() {
        super(PlantInAJar.PLANT_JAR_ENTITY);
        inventory.addListener(no -> {
            if (world != null && !world.isClient) { //Do I really have to null check here mr. game?
                serverReset();
            }
        });
    }

    private void serverReset() {
        tickyes = 0;
        hasOutputed = false;
        output.clear();
        random.setSeed(seed);
        seed = random.nextLong();
        if (seed == 0) seed = 1;
        sync();
    }

    @Override
    public void setLocation(World world, BlockPos pos) {
        super.setLocation(world, pos);
        if (seed == 0) {
            seed = pos.asLong();
        }
    }

    public void shoveOutputDown() {
        Inventory down = HopperBlockEntity.getInventoryAt(world, this.pos.down());
        if (down != null) {
            for(int i = 0; i < this.getOutput().size(); ++i) {
                if (!this.getOutput().getStack(i).isEmpty()) {
                    ItemStack itemStack = HopperBlockEntity.transfer(this.getOutput(), down, this.getOutput().getStack(i), Direction.UP);
                   this.getOutput().setStack(i, itemStack);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (canGrow()) {
            BlockState rawPlant = getRawPlant();
            if (rawPlant.getBlock() instanceof SaplingBlock && treeCacheKey != rawPlant.getBlock()) {
                random.setSeed(seed);
                tree = TreeMan.genTree((SaplingBlock)rawPlant.getBlock(), getBase(), random, false);
                treeCacheKey = rawPlant.getBlock();
            }
            if (tickyes < getGrowthTime()) {
                tickyes++;
            } else {
                if (!world.isClient && !hasOutputed) {
                    if (PlantInAJar.CONFIG.shouldDropItems()) {
                        if (rawPlant.getBlock() instanceof SaplingBlock) {
                            random.setSeed(seed);
                            Tree serverTree = TreeMan.genTree((SaplingBlock)rawPlant.getBlock(), getBase(), random, true);
                            if (serverTree != null) {
                                for (BlockState state : serverTree.drops) {
                                    if (state.getBlock() instanceof LeavesBlock) {
                                        for (int i = 0; i < 5; i++) {
                                            for (ItemStack stack : state.getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                                                output.addStack(stack);
                                            }
                                        }
                                    } else {
                                        for (ItemStack stack : state.getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                                            output.addStack(stack);
                                        }
                                    }
                                }
                            }
                        } else if (isTreeLegacy(getPlant())) {
                            for (ItemStack stack : getTreeBlockWood(getPlant()).getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                                output.addStack(stack);
                            }
                            for (int i = 0; i < 5; i++) {
                                for (ItemStack stack : getTreeBlockLeaf(getPlant()).getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                                    output.addStack(stack);
                                }
                            }
                        } else if (getPlant().isOf(Blocks.CHORUS_FLOWER)) {
                            for (ItemStack stack : Blocks.CHORUS_PLANT.getDefaultState().getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                                output.addStack(stack);
                            }
                        } else if (getPlant().isOf(Blocks.SEAGRASS)) {
                            output.addStack(new ItemStack(Items.SEAGRASS));
                        } else if (getPlant().isOf(Blocks.VINE)) {
                            output.addStack(new ItemStack(Items.VINE));
                        } else if (getPlant().isOf(Blocks.NETHER_SPROUTS)) {
                            output.addStack(new ItemStack(Items.NETHER_SPROUTS));
                        } else if (getPlant().getBlock() instanceof CoralParentBlock || getPlant().getBlock() instanceof CoralBlockBlock) {
                            output.addStack(getPlant().getBlock().getPickStack(world, pos, getPlant()));
                        } else {
                            for (ItemStack stack : getPlant().getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY))) {
                                output.addStack(stack);
                            }
                        }
                        if ((getPlant().isOf(Blocks.CRIMSON_FUNGUS) || getPlant().isOf(Blocks.WARPED_FUNGUS)) && ThreadLocalRandom.current().nextInt(0, 99) < 20) {
                            output.addStack(Blocks.SHROOMLIGHT.getDefaultState().getDroppedStacks((new LootContext.Builder((ServerWorld)getWorld())).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, ItemStack.EMPTY)).get(0));
                        }
                    }
                    hasOutputed = true;
                }
                if (!world.isClient && PlantInAJar.CONFIG.shouldDropItems() && output.isEmpty() && tickyes >= getGrowthTime()) {
                    serverReset();
                }
            }
        }
        if (!world.isClient)
            shoveOutputDown();
    }

    public ItemStack getBaseItemStack() {
        return inventory.getStack(1);
    }

    public BlockState getBase() {
        if (getBaseItemStack().getItem().equals(Items.WATER_BUCKET)) {
            return Blocks.WATER.getDefaultState().with(FluidBlock.LEVEL, 15);
        }

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

    public boolean canGrow() {
        BlockState plant = getRawPlant();
        BlockState base = getBase();

        if (getPlant().getBlock().isIn(BlockTags.FLOWERS)) {
            return !getBase().isAir() && !getBase().isOf(Blocks.JUNGLE_LOG) && !(getBaseItemStack().getItem() instanceof IBucketItem);
        }
        if (getPlant().getBlock() instanceof GourdBlock) {
            return getBase().isOf(Blocks.FARMLAND);
        }
        if (getPlant().isOf(Blocks.CHORUS_FLOWER)) {
            return getBase().isOf(Blocks.END_STONE);
        }
        if (getPlant().isOf(Blocks.COCOA)) {
            return getBase().isOf(Blocks.JUNGLE_LOG);
        }
        if (getPlant().isOf(Blocks.SEAGRASS) || getPlant().isOf(Blocks.KELP) || getPlant().isOf(Blocks.SEA_PICKLE) || getPlant().getBlock() instanceof CoralParentBlock || getPlant().getBlock() instanceof CoralBlockBlock) {
            return getBase().isOf(Blocks.WATER);
        }
        if (getPlant().isOf(Blocks.LILY_PAD)) {
            if (!(getBaseItemStack().getItem() instanceof IBucketItem)) return false;
            IBucketItem item = (IBucketItem)getBaseItemStack().getItem();
            if (!item.libblockattributes__getFluid(getBaseItemStack()).equals(FluidKeys.WATER)) return false;
            double a = item.libblockattributes__getFluidVolumeAmount().asInexactDouble();
            return 0.8d > a && a > 0.2d;
        }
        if (plant.getBlock() instanceof CropBlock || plant.getBlock() instanceof NetherWartBlock || plant.getBlock() instanceof SweetBerryBushBlock) {
            try {
                return ((PlantBlockAccess)plant.getBlock()).callCanPlantOnTop(base, null, null);
            } catch (Exception e) {
                System.out.println("Epic Hacky Code Failed Scream At ThatTrollzer in the Fabric Discord If You See This");
                e.printStackTrace();
            }
        } else if (getPlant().getBlock() instanceof VineBlock || getPlant().isOf(Blocks.WEEPING_VINES_PLANT) || getPlant().isOf(Blocks.TWISTING_VINES_PLANT)) {
            return !getBase().isAir() && !(getBaseItemStack().getItem() instanceof IBucketItem);
        } else if (
            isTreeLegacy(plant) ||
            getPlant().getBlock() instanceof SaplingBlock ||
            getPlant().getBlock() instanceof CactusBlock || 
            getPlant().getBlock() instanceof BambooBlock || 
            getPlant().getBlock() instanceof SugarCaneBlock ||
            getPlant().isOf(Blocks.RED_MUSHROOM) ||
            getPlant().isOf(Blocks.BROWN_MUSHROOM) ||
            getPlant().getBlock() instanceof SproutsBlock ||
            getPlant().getBlock() instanceof RootsBlock
        ) {
            return !getBase().isAir() && !getBase().isOf(Blocks.JUNGLE_LOG) && !(getBaseItemStack().getItem() instanceof IBucketItem);
        }

        return false;
    }

    public BlockState getPlant() {
        BlockState rawState = getRawPlant();
        if (rawState.getBlock() instanceof StemBlock) {
            return ((StemBlock)rawState.getBlock()).getGourdBlock().getDefaultState();
        }
        if (rawState.getBlock() instanceof CropBlock) {
            CropBlock c = ((CropBlock)rawState.getBlock());
            int age = (int) ( ((float)tickyes) / ((float)getGrowthTime() ) * (c.getMaxAge() + 1));
            if (age > c.getMaxAge()) age = c.getMaxAge();
            return c.withAge(age);
        }
        if (rawState.getBlock() instanceof NetherWartBlock) {
            NetherWartBlock c = ((NetherWartBlock)rawState.getBlock());
            int maxAge = NetherWartBlock.AGE.getValues().size() - 1;
            int age = (int) ( ((float)tickyes) / ((float)getGrowthTime() ) * (maxAge + 1));
            if (age > maxAge) age = maxAge;
            return c.getDefaultState().with(NetherWartBlock.AGE, age);
        }
        if (rawState.getBlock() instanceof SweetBerryBushBlock) {
            SweetBerryBushBlock c = ((SweetBerryBushBlock)rawState.getBlock());
            int maxAge = SweetBerryBushBlock.AGE.getValues().size() - 1;
            int age = (int) ( ((float)tickyes) / ((float)getGrowthTime() ) * (maxAge + 1));
            if (age > maxAge) age = maxAge;
            return c.getDefaultState().with(SweetBerryBushBlock.AGE, age);
        }
        if (rawState.isOf(Blocks.COCOA)) {
            CocoaBlock c = ((CocoaBlock)rawState.getBlock());
            int maxAge = CocoaBlock.AGE.getValues().size() - 1;
            int age = (int) ( ((float)tickyes) / ((float)getGrowthTime() ) * (maxAge + 1));
            if (age > maxAge) age = maxAge;
            return c.getDefaultState().with(CocoaBlock.AGE, age);
        }
        if (rawState.isOf(Blocks.WEEPING_VINES)) {
            return Blocks.WEEPING_VINES_PLANT.getDefaultState();
        }
        if (rawState.isOf(Blocks.TWISTING_VINES)) {
            return Blocks.TWISTING_VINES_PLANT.getDefaultState();
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

    public static boolean isTreeLegacy(BlockState plant) {
        return plant.isOf(Blocks.CRIMSON_FUNGUS) || plant.isOf(Blocks.WARPED_FUNGUS);
    }

    public static BlockState getTreeBlockWood(BlockState sappling) {
        if (sappling.isOf(Blocks.CRIMSON_FUNGUS)) {
            return Blocks.CRIMSON_STEM.getDefaultState();
        } else if (sappling.isOf(Blocks.WARPED_FUNGUS)) {
            return Blocks.WARPED_STEM.getDefaultState();
        }
        return Blocks.AIR.getDefaultState();
    }

    public static BlockState getTreeBlockLeaf(BlockState sappling) {
        if (sappling.isOf(Blocks.CRIMSON_FUNGUS)) {
            return Blocks.NETHER_WART_BLOCK.getDefaultState();
        } else if (sappling.isOf(Blocks.WARPED_FUNGUS)) {
            return Blocks.WARPED_WART_BLOCK.getDefaultState();
        }
        return Blocks.AIR.getDefaultState();
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
        if (tag.contains("seed")) {
            seed = tag.getLong("seed");
        } else {
            seed = pos.asLong();
        }
        treeCacheKey = null;
        tree = null;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        DefaultedList<ItemStack> e = DefaultedList.ofSize(2, ItemStack.EMPTY);
        e.set(0, inventory.getStack(0));
        e.set(1, inventory.getStack(1));
        Inventories.toTag(tag, e);
        tag.putInt("tickyes", tickyes);
        tag.putLong("seed", seed);
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
        clientGrowthTime = tag.getInt("growthTime");
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        toTag(tag);
        tag.putInt("growthTime", getGrowthTime());
        return tag;
    }

    public int getTickyes() {
        return tickyes;
    }
    
}