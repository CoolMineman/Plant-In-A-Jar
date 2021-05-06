package io.github.coolmineman.plantinajar;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.coolmineman.plantinajar.tree.Tree;
import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.RootsBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SproutsBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Direction;

public class JarBlockEntityRenderer extends BlockEntityRenderer<JarBlockEntity> {

    private static final FluidVolume WATER_VOLUME = FluidKeys.WATER.withAmount(FluidAmount.BUCKET);
    private static final List<FluidRenderFace> RENDER_FACES;

    public enum CoolTreeEnum {
        WOOD,
        LEAVES,
        AIR
    }

    public static final CoolTreeEnum[][][] tree;
    public static final CoolTreeEnum[][][] chorus;
    public static final CoolTreeEnum[][][] RED_MUSHROOM;
    public static final CoolTreeEnum[][][] BROWN_MUSHROOM;

    static {
        String[][] treePattern = {
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                " lll ",
                "lllll",
                "llwll",
                "lllll",
                " lll "
            },
            {
                " lll ",
                "lllll",
                "llwll",
                "lllll",
                " lll "
            },
            {
                "     ",
                "  l  ",
                " lwl ",
                "  l  ",
                "     "
            },
            {
                "     ",
                "  l  ",
                " lll ",
                "  l  ",
                "     "
            }
        };
        
        tree = doTreeEpic(treePattern);

        String[][] chorusPattern = {
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "  w  ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "  w  ",
                "     ",
                "     ",
                "     "
            },
            {
                "     ",
                " ww  ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                " w   ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                " w   ",
                "  ww ",
                "  w  ",
                "     "
            },
            {
                "     ",
                "www  ",
                "   w ",
                "  w  ",
                "     "
            },
            {
                "     ",
                "l w  ",
                "   w ",
                "  w  ",
                "     "
            },
            {
                "  w  ",
                " ww  ",
                "   l ",
                "  w  ",
                "  w  "
            },
            {
                "  l  ",
                " w   ",
                "     ",
                "     ",
                "  w  "
            },
            {
                "     ",
                " w   ",
                "     ",
                "     ",
                "  l  "
            },
            {
                "     ",
                " l   ",
                "     ",
                "     ",
                "     "
            }
        };

        chorus = doTreeEpic(chorusPattern);

        String[][] mushroomPattern1 = {
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                "     ",
                "     ",
                "  w  ",
                "     ",
                "     "
            },
            {
                " lll ",
                "l   l",
                "l w l",
                "l   l",
                " lll "
            },
            {
                " lll ",
                "l   l",
                "l w l",
                "l   l",
                " lll "
            },
            {
                " lll ",
                "l   l",
                "l w l",
                "l   l",
                " lll "
            },
            {
                "     ",
                " lll ",
                " lll ",
                " lll ",
                "     "
            },
        };
        RED_MUSHROOM = doTreeEpic(mushroomPattern1);

        String[][] mushroomPattern2 = {
            {
                "       ",
                "       ",
                "       ",
                "   w   ",
                "       ",
                "       ",
                "       "
            },
            {
                "       ",
                "       ",
                "       ",
                "   w   ",
                "       ",
                "       ",
                "       "
            },
            {
                "       ",
                "       ",
                "       ",
                "   w   ",
                "       ",
                "       ",
                "       "
            },
            {
                "       ",
                "       ",
                "       ",
                "   w   ",
                "       ",
                "       ",
                "       "
            },
            {
                "       ",
                "       ",
                "       ",
                "   w   ",
                "       ",
                "       ",
                "       "
            },
            {
                " lllll ",
                "lllllll",
                "lllllll",
                "lllllll",
                "lllllll",
                "lllllll",
                " lllll "
            },
        };
        
        BROWN_MUSHROOM = doTreeEpic(mushroomPattern2);

        List<FluidRenderFace> a = new ArrayList<>();
        FluidRenderFace.appendCuboid(0d, 0d, 0d, 1d, 1d, 1d, 1d, EnumSet.allOf(Direction.class), a);
        RENDER_FACES = a;
    }

    public static float getScaleFactor(JarBlockEntity e) {
        return 1f / (e.getGrowthTime());
    }

    public static CoolTreeEnum[][][] doTreeEpic(String[][] treePattern) {
        CoolTreeEnum[][][] tree1 = new CoolTreeEnum[treePattern[0].length][treePattern.length][treePattern[0][0].length()]; //Must be square
        for (int i = 0; i < treePattern.length; i++) { //I is y
            for (int j = 0; j < treePattern[i].length; j++) { //J is x
                char[] e = treePattern[i][j].toCharArray();
                for (int k = 0; k < e.length; k++) {
                    if (e[k] == 'w') {
                        tree1[j][i][k] = CoolTreeEnum.WOOD;
                    } else if (e[k] == 'l') {
                        tree1[j][i][k] =  CoolTreeEnum.LEAVES;
                    } else {
                        tree1[j][i][k] =  CoolTreeEnum.AIR;
                    }
                }
            }
        }
        return tree1;
    }

    public JarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    public static void renderTree(BlockState wood, BlockState leaves, CoolTreeEnum[][][] epictree, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int offset = (epictree.length - 1) / 2; //assume tree square
        matrices.translate(-offset, 0, -offset);
        for (int i = 0; i < epictree.length; i++) {
            for (int j = 0; j < epictree[i].length; j++) {
                for (int k = 0; k < epictree[i][j].length; k++) {
                    if (epictree[i][j][k] == CoolTreeEnum.WOOD) {
                        matrices.translate(i, j, k);
                        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(wood, matrices, vertexConsumers, light, overlay);
                        matrices.translate(-i, -j, -k);
                    } else if (epictree[i][j][k] == CoolTreeEnum.LEAVES) {
                        matrices.translate(i, j, k);
                        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(leaves, matrices, vertexConsumers, light, overlay);
                        matrices.translate(-i, -j, -k);
                    }
                }
            }
        }
        matrices.translate(offset, 0, offset);
    }

    public void renderTreeNew(SaplingBlock sapling, JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.treeCacheKey != sapling) return;
        Tree treeWrapper = entity.tree;
        if (treeWrapper == null) return;
        BlockState[][][] tree = treeWrapper.tree;
        float scale = 1f / (Math.max(tree.length, Math.max(tree[0].length, tree[0][0].length)) + 1);
        matrices.translate(0, -0.5f, 0);
        scaleCenterAligned(matrices, scale, scale, scale);
        float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
        if (scalefactor > 1) scalefactor = 1;
        scaleBottomAligned(matrices, scalefactor);
        int offsetx = (tree.length - 1) / 2;
        int offsetz = (tree[0][0].length - 1) / 2;
        matrices.translate(-offsetx, 0, -offsetz);
        for (int i = 0; i < tree.length; i++) {
            for (int j = 0; j < tree[i].length; j++) {
                for (int k = 0; k < tree[i][j].length; k++) {
                    BlockState bs = tree[i][j][k];
                    if (bs != null) {
                        matrices.translate(i, j, k);
                        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(bs, matrices, vertexConsumers, light, overlay);
                        matrices.translate(-i, -j, -k);
                    }
                }
            }
        }
        matrices.translate(offsetx, 0, offsetz);
    }

    public static void scaleCenterAligned(MatrixStack matrices, float x, float y, float z) {
        matrices.translate((1f-x) * .5, (1f-x) * .5, (1f-z) * .5);
        matrices.scale(x, y, z);
    }

    public static void scaleBottomAligned(MatrixStack matrices, float scalefactor) {
        matrices.translate(0d, scalefactor * 0.5d, 0d);
        scaleCenterAligned(matrices, scalefactor, scalefactor, scalefactor);
    }

    @Override
    public void render(JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        BlockState base = entity.getBase();
        matrices.translate(0d, 1d/32d, 0d);
        if (entity.getPlant().getBlock() instanceof VineBlock) {
            matrices.translate(0d, 1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f + 1f/3f, 1f/3f);
            scaleBottomAligned(matrices, 1f/3f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(base, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.WEEPING_VINES_PLANT)) {
            matrices.translate(0d, -1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f + 2f/3f, 0f);
            scaleBottomAligned(matrices, 1f/3f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(base, matrices, vertexConsumers, light, overlay);
        } else if (base.isOf(Blocks.JUNGLE_LOG)) {
            matrices.translate(0d, 1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f, -0.25f);
            scaleBottomAligned(matrices, .5f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(base, matrices, vertexConsumers, light, overlay);
        } else if (base.isOf(Blocks.WATER)) {
            matrices.translate(0d, -1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
            CoolFluidVolumeRenderer.INSTANCE.render(WATER_VOLUME, RENDER_FACES, vertexConsumers, matrices);
        } else if (entity.getBaseItemStack().getItem() instanceof IBucketItem) {
            matrices.translate(0d, -1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
            IBucketItem item = (IBucketItem)entity.getBaseItemStack().getItem();
            if (!item.libblockattributes__getFluid(entity.getBaseItemStack()).equals(FluidKeys.EMPTY)) {
                List<FluidRenderFace> a = new ArrayList<>();
                FluidRenderFace.appendCuboid(0d, 0d, 0d, 1d, item.libblockattributes__getFluidVolumeAmount().asInexactDouble(), 1d, 1d, EnumSet.allOf(Direction.class), a);
                CoolFluidVolumeRenderer.INSTANCE.render(item.libblockattributes__getFluid(entity.getBaseItemStack()).withAmount(FluidAmount.BUCKET), a, vertexConsumers, matrices);
            }
        } else {
            matrices.translate(0.0005f, 0f, 0.0005f);
            matrices.scale(0.999f, 1f/32f, 0.999f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(base, matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0d, 1d/16d, 0d);
        scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
        if (entity.getPlant().getBlock() instanceof SaplingBlock) {
            renderTreeNew((SaplingBlock)entity.getPlant().getBlock(), entity, tickDelta, matrices, vertexConsumers, light, overlay);
        } else if (JarBlockEntity.isTreeLegacy(entity.getPlant())) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/7f, 1f/7f, 1f/7f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderTree(JarBlockEntity.getTreeBlockWood(entity.getPlant()), JarBlockEntity.getTreeBlockLeaf(entity.getPlant()), tree, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isIn(BlockTags.FLOWERS)) {
            matrices.translate(0, -0.5f, 0);
            if (entity.getPlant().getBlock() instanceof TallPlantBlock) {
                float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity) * 0.5f;
                if (scalefactor > 1) scalefactor = 1;
                scaleBottomAligned(matrices, scalefactor);
                MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
                matrices.translate(0, 1, 0);
                MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER), matrices, vertexConsumers, light, overlay);
            } else {
                float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
                if (scalefactor > 1) scalefactor = 1;
                scaleBottomAligned(matrices, scalefactor);
                MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
            }
        } else if (entity.getPlant().getBlock() instanceof VineBlock) {
            matrices.translate(0f, -.5f, 0f);
            scaleBottomAligned(matrices, 1f/3f);
            matrices.translate(0f, 1f, 0f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
            matrices.translate(0f, -1f, 0f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            matrices.translate(scalefactor * -.5f + 0.5f, scalefactor * -1f + 1f, 0f);
            matrices.scale(scalefactor, scalefactor, 1f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.WEEPING_VINES_PLANT)) {
            matrices.translate(0f, -0.5f, 0f);
            scaleBottomAligned(matrices, 1f/3f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            matrices.translate(scalefactor * -.5f + .5f, scalefactor * -2f + 1.75f + 1f/16f, scalefactor * -.5f + .5f);
            matrices.scale(scalefactor, scalefactor, scalefactor);
            matrices.translate(0f, 1f, 0f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
            matrices.translate(0f, -1f, 0f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Blocks.WEEPING_VINES.getDefaultState(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.COCOA)) {
            matrices.translate(0f, -.5f, 0.25f);
            scaleBottomAligned(matrices, .5f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.CHORUS_FLOWER)) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/17f, 1f/17f, 1f/17f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderTree(Blocks.CHORUS_PLANT.getDefaultState().with(ConnectingBlock.DOWN, true).with(ConnectingBlock.UP, true).with(ConnectingBlock.NORTH, true).with(ConnectingBlock.SOUTH, true).with(ConnectingBlock.WEST, true).with(ConnectingBlock.EAST, true), Blocks.CHORUS_FLOWER.getDefaultState(), chorus, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.RED_MUSHROOM)) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/7f, 1f/7f, 1f/7f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderTree(Blocks.MUSHROOM_STEM.getDefaultState(), Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), RED_MUSHROOM, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.BROWN_MUSHROOM)) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/7f, 1f/7f, 1f/7f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderTree(Blocks.MUSHROOM_STEM.getDefaultState(), Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), BROWN_MUSHROOM, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().getBlock() instanceof GourdBlock || 
                entity.getPlant().getBlock() instanceof CactusBlock || 
                entity.getPlant().getBlock() instanceof BambooBlock || 
                entity.getPlant().getBlock() instanceof SugarCaneBlock ||
                entity.getPlant().isOf(Blocks.TWISTING_VINES_PLANT) ||
                entity.getPlant().getBlock() instanceof SproutsBlock ||
                entity.getPlant().getBlock() instanceof RootsBlock
            )
        {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 2f/3f, 2f/3f, 2f/3f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            BlockState plant = entity.getPlant().isOf(Blocks.TWISTING_VINES_PLANT) ? Blocks.TWISTING_VINES.getDefaultState() : entity.getPlant();
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(plant, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.SEAGRASS) || entity.getPlant().isOf(Blocks.KELP) || entity.getPlant().isOf(Blocks.SEA_PICKLE)) {
            matrices.translate(0, -0.5f, 0);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.LILY_PAD)) {
            matrices.translate(0, -0.5f + -1f/16f, 0);
            if (entity.getBaseItemStack().getItem() instanceof IBucketItem) {
                IBucketItem item = (IBucketItem)entity.getBaseItemStack().getItem();
                matrices.translate(0d, item.libblockattributes__getFluidVolumeAmount().asInexactDouble(), 0d);
            }
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else {
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();
    }

}