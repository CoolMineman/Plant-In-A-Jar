package io.github.coolmineman.plantinajar;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
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

        List<FluidRenderFace> a = new ArrayList<>();
        FluidRenderFace.appendCuboid(0d, 0d, 0d, 1, 31d/32d, 1d, 1d, EnumSet.allOf(Direction.class), a);
        RENDER_FACES = a;
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
        matrices.translate(0d, 1d/32d, 0d);
        BlockState base = entity.getBase();
        if (base.isOf(Blocks.JUNGLE_LOG)) {
            matrices.translate(0d, 1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f, -0.25f);
            scaleBottomAligned(matrices, .5f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(base, matrices, vertexConsumers, light, overlay);
        } else if (base.isOf(Blocks.WATER)) {
            scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
            WATER_VOLUME.getRenderer().render(WATER_VOLUME, RENDER_FACES, vertexConsumers, matrices);
        } else {
            matrices.translate(0.0005f, 0f, 0.0005f);
            matrices.scale(0.999f, 1f/32f, 0.999f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(base, matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0d, 1d/16d, 0d);
        scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
        if (JarBlockEntity.isTree(entity.getPlant())) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/7f, 1f/7f, 1f/7f);
            float scalefactor = (entity.getTickyes() + tickDelta) * 0.005f;
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderTree(JarBlockEntity.getTreeBlockWood(entity.getPlant()), JarBlockEntity.getTreeBlockLeaf(entity.getPlant()), tree, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.COCOA)) {
            matrices.translate(0f, -.5f, 0.25f);
            scaleBottomAligned(matrices, .5f);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.CHORUS_FLOWER)) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/17f, 1f/17f, 1f/17f);
            float scalefactor = (entity.getTickyes() + tickDelta) * 0.005f;
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderTree(Blocks.CHORUS_PLANT.getDefaultState().with(ConnectingBlock.DOWN, true).with(ConnectingBlock.UP, true).with(ConnectingBlock.NORTH, true).with(ConnectingBlock.SOUTH, true).with(ConnectingBlock.WEST, true).with(ConnectingBlock.EAST, true), Blocks.CHORUS_FLOWER.getDefaultState(), chorus, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().getBlock() instanceof GourdBlock || 
                entity.getPlant().getBlock() instanceof CactusBlock || 
                entity.getPlant().getBlock() instanceof BambooBlock || 
                entity.getPlant().getBlock() instanceof SugarCaneBlock
            )
        {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 2f/3f, 2f/3f, 2f/3f);
            float scalefactor = (entity.getTickyes() + tickDelta) * 0.005f;
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else {
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();
    }

}