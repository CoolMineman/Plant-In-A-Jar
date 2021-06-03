package io.github.coolmineman.plantinajar;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.mixin.api.IBucketItem;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import io.github.coolmineman.plantinajar.tree.QuadWithColor;
import io.github.coolmineman.plantinajar.tree.Tree;
import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.CoralBlockBlock;
import net.minecraft.block.CoralParentBlock;
import net.minecraft.block.GourdBlock;
import net.minecraft.block.RootsBlock;
import net.minecraft.block.SproutsBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Direction;

public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity> {

    private static final FluidVolume WATER_VOLUME = FluidKeys.WATER.withAmount(FluidAmount.BUCKET);
    private static final List<FluidRenderFace> RENDER_FACES;

    static {
        List<FluidRenderFace> a = new ArrayList<>();
        FluidRenderFace.appendCuboid(0d, 0d, 0d, 1d, 1d, 1d, 1d, EnumSet.allOf(Direction.class), a);
        RENDER_FACES = a;
    }

    public static float getScaleFactor(JarBlockEntity e) {
        return 1f / (e.getGrowthTime());
    }

    public void renderTreeNew(GrowsMultiblockPlantBlock sapling, JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.treeCacheKey != sapling) return;
        Tree treeWrapper = entity.tree;
        if (treeWrapper == null) return;
        QuadWithColor[][][][] tree = treeWrapper.quads;
        if (tree.length == 0 || tree[0].length == 0 || tree[0][0].length == 0) return;
        float scale = 0.9f / Math.max(tree.length, Math.max(tree[0].length, tree[0][0].length));
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
                    QuadWithColor[] qc = tree[i][j][k];
                    if (qc != null) {
                        matrices.translate(i, j, k);
                        for (QuadWithColor c : qc) {
                            vertexConsumers.getBuffer(c.renderLayer).quad(matrices.peek(), c.quad, c.r, c.g, c.b, light, overlay);
                        }
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
        light = light - 2;
        matrices.push();
        BlockState base = entity.getBase();
        matrices.translate(0d, 1d/32d, 0d);
        if (entity.getPlant().getBlock() instanceof VineBlock) {
            matrices.translate(0d, 1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f + 1f/3f, 1f/3f);
            scaleBottomAligned(matrices, 1f/3f);
            renderBlockAsEntity(entity, base, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.WEEPING_VINES_PLANT)) {
            matrices.translate(0d, -1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f + 2f/3f, 0f);
            scaleBottomAligned(matrices, 1f/3f);
            renderBlockAsEntity(entity, base, matrices, vertexConsumers, light, overlay);
        } else if (base.isOf(Blocks.JUNGLE_LOG)) {
            matrices.translate(0d, 1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 1f, 0.999f);
            matrices.translate(0f, -.5f, -0.25f);
            scaleBottomAligned(matrices, .5f);
            renderBlockAsEntity(entity, base, matrices, vertexConsumers, light, overlay);
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
            renderBlockAsEntity(entity, base, matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0d, 1d/16d, 0d);
        scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
        if (entity.getPlant().getBlock() instanceof GrowsMultiblockPlantBlock) {
            renderTreeNew((GrowsMultiblockPlantBlock)entity.getPlant().getBlock(), entity, tickDelta, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isIn(BlockTags.FLOWERS)) {
            matrices.translate(0, -0.5f, 0);
            if (entity.getPlant().getBlock() instanceof TallPlantBlock) {
                float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity) * 0.5f;
                if (scalefactor > 1) scalefactor = 1;
                scaleBottomAligned(matrices, scalefactor);
                renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
                matrices.translate(0, 1, 0);
                renderBlockAsEntity(entity, entity.getPlant().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER), matrices, vertexConsumers, light, overlay);
            } else {
                float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
                if (scalefactor > 1) scalefactor = 1;
                scaleBottomAligned(matrices, scalefactor);
                renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
            }
        } else if (entity.getPlant().getBlock() instanceof VineBlock) {
            matrices.translate(0f, -.5f, 0f);
            scaleBottomAligned(matrices, 1f/3f);
            matrices.translate(0f, 1f, 0f);
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
            matrices.translate(0f, -1f, 0f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            matrices.translate(scalefactor * -.5f + 0.5f, scalefactor * -1f + 1f, 0f);
            matrices.scale(scalefactor, scalefactor, 1f);
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.WEEPING_VINES_PLANT)) {
            matrices.translate(0f, -0.5f, 0f);
            scaleBottomAligned(matrices, 1f/3f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            matrices.translate(scalefactor * -.5f + .5f, scalefactor * -2f + 1.75f + 1f/16f, scalefactor * -.5f + .5f);
            matrices.scale(scalefactor, scalefactor, scalefactor);
            matrices.translate(0f, 1f, 0f);
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
            matrices.translate(0f, -1f, 0f);
            renderBlockAsEntity(entity, Blocks.WEEPING_VINES.getDefaultState(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.COCOA)) {
            matrices.translate(0f, -.5f, 0.25f);
            scaleBottomAligned(matrices, .5f);
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().getBlock() instanceof GourdBlock || 
                entity.getPlant().getBlock() instanceof CactusBlock || 
                entity.getPlant().getBlock() instanceof BambooBlock || 
                entity.getPlant().getBlock() instanceof SugarCaneBlock ||
                entity.getPlant().isOf(Blocks.TWISTING_VINES_PLANT) ||
                entity.getPlant().getBlock() instanceof SproutsBlock ||
                entity.getPlant().getBlock() instanceof RootsBlock ||
                entity.getPlant().getBlock() instanceof CoralBlockBlock
            )
        {
            matrices.translate(0, -0.5f, 0);
            scaleCenterAligned(matrices, 2f/3f, 2f/3f, 2f/3f);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            BlockState plant = entity.getPlant().isOf(Blocks.TWISTING_VINES_PLANT) ? Blocks.TWISTING_VINES.getDefaultState() : entity.getPlant();
            renderBlockAsEntity(entity, plant, matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.SEAGRASS) || entity.getPlant().isOf(Blocks.KELP) || entity.getPlant().isOf(Blocks.SEA_PICKLE) || entity.getPlant().getBlock() instanceof CoralParentBlock) {
            matrices.translate(0, -0.5f, 0);
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else if (entity.getPlant().isOf(Blocks.LILY_PAD)) {
            matrices.translate(0, -0.5f + -1f/16f, 0);
            if (entity.getBaseItemStack().getItem() instanceof IBucketItem) {
                IBucketItem item = (IBucketItem)entity.getBaseItemStack().getItem();
                matrices.translate(0d, item.libblockattributes__getFluidVolumeAmount().asInexactDouble(), 0d);
            }
            float scalefactor = (entity.getTickyes() + tickDelta) * getScaleFactor(entity);
            if (scalefactor > 1) scalefactor = 1;
            scaleBottomAligned(matrices, scalefactor);
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
        } else {
            renderBlockAsEntity(entity, entity.getPlant(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();
    }

    public void renderBlockAsEntity(JarBlockEntity entity, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay) {
        // MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(state, matrices, vertexConsumer, light, overlay);
        BlockRenderType blockRenderType = state.getRenderType();
        if (blockRenderType == BlockRenderType.MODEL) {
            BakedModel bakedModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(state); 
            int color = MinecraftClient.getInstance().getBlockColors().getColor(state, entity.getWorld(), entity.getPos(), 0);
            float r = (color >> 16 & 255) / 300f;
            float g = (color >> 8 & 255) / 300f;
            float b = (color & 255) / 300f;
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumer.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, bakedModel, r, g, b, light, overlay);
        }
    }            
}