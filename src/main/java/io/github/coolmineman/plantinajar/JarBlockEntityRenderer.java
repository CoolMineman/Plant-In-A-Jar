package io.github.coolmineman.plantinajar;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class JarBlockEntityRenderer extends BlockEntityRenderer<JarBlockEntity> {

    public enum CoolTreeEnum {
        WOOD,
        LEAVES,
        AIR
    }

    public static final CoolTreeEnum[][][] tree;

    static {
        CoolTreeEnum[][][] tree1 = new CoolTreeEnum[5][6][5];
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
        tree = tree1;
    }

    public JarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    public static void renderTree(BlockState wood, BlockState leaves, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int offset = (tree.length - 1) / 2; //assume tree square
        matrices.translate(-offset, 0, -offset);
        for (int i = 0; i < tree.length; i++) {
            for (int j = 0; j < tree[i].length; j++) {
                for (int k = 0; k < tree[i][j].length; k++) {
                    if (tree[i][j][k] == CoolTreeEnum.WOOD) {
                        matrices.translate(i, j, k);
                        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(wood, matrices, vertexConsumers, light, overlay);
                        matrices.translate(-i, -j, -k);
                    } else if (tree[i][j][k] == CoolTreeEnum.LEAVES) {
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
        matrices.translate((1f-x) * .5, (1f-y) * .5, (1f-z) * .5);
        matrices.scale(x, y, z);
    }

    @Override
    public void render(JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0d, 1d/32d, 0d);
        matrices.translate(0.0005f, 0f, 0.0005f);
        matrices.scale(0.999f, 1f/32f, 0.999f);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getBase(), matrices, vertexConsumers, light, overlay);
        matrices.pop();

        matrices.push();
        matrices.translate(0d, 1d/16d, 0d);
        scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
        if (entity.getPlant().getBlock() instanceof SaplingBlock) {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 1f/6f, 1f/6f, 1f/6f);
            scaleCenterAligned(matrices, entity.getTickyes() * 0.005f, entity.getTickyes() * 0.005f, entity.getTickyes() * 0.005f);
            renderTree(Blocks.SAND.getDefaultState(), Blocks.ANVIL.getDefaultState(), matrices, vertexConsumers, light, overlay);
        } else {
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(entity.getPlant(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();
    }

}