package io.github.coolmineman.plantinajar;

import io.github.coolmineman.plantinajar.tree.QuadWithColor;
import io.github.coolmineman.plantinajar.tree.Tree;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.Direction;
public class JarBlockEntityRenderer implements BlockEntityRenderer<JarBlockEntity> {
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
        // light -= 12;
        matrices.push();
        BlockState base = entity.getBase();
        double fluidHeight = 0;
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
            renderFluid(matrices, vertexConsumers, FluidVariant.of(Fluids.WATER), entity, 1f, light, overlay);
        } else if (FluidStorage.ITEM.find(entity.getBaseItemStack(), ContainerItemContext.withInitial(entity.getBaseItemStack())) != null) {
            matrices.translate(0d, -1d/32d, 0d);
            scaleCenterAligned(matrices, 0.999f, 0.999f, 0.999f);
            Storage<FluidVariant> fs = FluidStorage.ITEM.find(entity.getBaseItemStack(), ContainerItemContext.withInitial(entity.getBaseItemStack()));
            if (fs != null) {
                try (Transaction t = Transaction.openOuter()) {
                    StorageView<FluidVariant> bsv = null;
                    for (StorageView<FluidVariant> sv : fs.iterable(t)) {
                        if (bsv == null || sv.getAmount() > bsv.getAmount()) bsv = sv;
                    }
                    if (bsv != null) {
                        fluidHeight = Math.min(bsv.getAmount(), 81000)/81000.0;
                        renderFluid(matrices, vertexConsumers, bsv.getResource(), entity, Math.min(bsv.getAmount(), 81000)/81000f, light, overlay);
                    }
                }
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
            if (FluidStorage.ITEM.find(entity.getBaseItemStack(), ContainerItemContext.withInitial(entity.getBaseItemStack())) != null) {
                matrices.translate(0d, fluidHeight, 0d);
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

    public void renderFluid(MatrixStack matrices, VertexConsumerProvider vertexConsumers, FluidVariant fv, BlockEntity entity, float height, int light, int overlay) {
        VertexConsumer c = MinecraftClient.isFabulousGraphicsOrBetter() ? vertexConsumers.getBuffer(RenderLayer.getTranslucentMovingBlock()) : vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        Sprite sprite = FluidVariantRendering.getSprite(fv);
        // FluidRenderer
        int color = FluidVariantRendering.getColor(fv, entity.getWorld(), entity.getPos());
        float r = ((color >> 16) & 255) / 256f;
        float g = ((color >> 8) & 255) / 256f;
        float b = (color & 255) / 256f;

        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        for (Direction direction : Direction.values()) {
            QuadEmitter emitter = renderer.meshBuilder().getEmitter();

            if (direction.getAxis().isVertical()) {
                emitter.square(direction, 0, 0, 1, 1, direction == Direction.UP ? 1 - height : 0);
            } else {
                emitter.square(direction, 0, 0, 1 , height, 0);
            }

            emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.spriteColor(0, -1, -1, -1, -1);
            c.quad(matrices.peek(), emitter.toBakedQuad(0, sprite, false), r, g, b, light, OverlayTexture.DEFAULT_UV);
        }
    }
}