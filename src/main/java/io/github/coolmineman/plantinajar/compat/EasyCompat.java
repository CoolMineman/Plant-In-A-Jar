package io.github.coolmineman.plantinajar.compat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EasyCompat implements Compat {

    private final HashMap<String, TreeBlocks> saplingToTreeBlocks = new HashMap<>();

    public class TreeBlocks {
        final String logBlock;
        final String leavesBlock;

        public TreeBlocks(String logBlock, String leavesBlock) {
            this.logBlock = logBlock;
            this.leavesBlock = leavesBlock;
        }
    }

    public EasyCompat addTree(String sapling, String logBlock, String leavesBlock) {
        this.saplingToTreeBlocks.put(sapling, new TreeBlocks(logBlock, leavesBlock));
        return this;
    }

    @Override
    public BlockState getTreeBlockWood(BlockState sappling) {
        String id = Registry.BLOCK.getId(sappling.getBlock()).toString();
        return saplingToTreeBlocks.containsKey(id)
                ? Registry.BLOCK.get(new Identifier(saplingToTreeBlocks.get(id).logBlock)).getDefaultState()
                : null;
    }

    @Override
    public BlockState getTreeBlockLeaf(BlockState sappling) {
        String id = Registry.BLOCK.getId(sappling.getBlock()).toString();
        return saplingToTreeBlocks.containsKey(id)
                ? Registry.BLOCK.get(new Identifier(saplingToTreeBlocks.get(id).leavesBlock)).getDefaultState()
                : null;
    }

    @Override
    public List<ItemStack> getExtraDrops(ItemStack plant) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Boolean> isTree(BlockState plant) {
        return saplingToTreeBlocks.containsKey(Registry.BLOCK.getId(plant.getBlock()).toString()) ? Optional.of(true)
                : Optional.empty();
    }
}