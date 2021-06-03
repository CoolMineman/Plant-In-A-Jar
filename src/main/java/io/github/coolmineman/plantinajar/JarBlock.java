package io.github.coolmineman.plantinajar;

import java.util.List;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class JarBlock extends BlockWithEntity implements InventoryProvider {

    public JarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, PlantInAJar.PLANT_JAR_ENTITY, (w, p, s, e) -> e.tick());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JarBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        BlockEntity e = world.getBlockEntity(pos);
        if (e instanceof JarBlockEntity) {
            return ((JarBlockEntity)e).getOutput();
        }
        return null;
    }
    
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, Builder builder) {
        List<ItemStack> tmp = super.getDroppedStacks(state, builder);
        BlockEntity e = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
        if (e instanceof JarBlockEntity) {
            for (ItemStack a : ((SimpleInventory)((JarBlockEntity)e).getInventory(null, null, null)).clearToList()) {
               tmp.add(a);
            }
        }
        return tmp;
    }
}