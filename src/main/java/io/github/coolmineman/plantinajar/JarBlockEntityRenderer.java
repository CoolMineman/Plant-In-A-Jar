package io.github.coolmineman.plantinajar;

import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class JarBlockEntityRenderer extends BlockEntityRenderer<JarBlockEntity> {

    public JarBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(JarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0d, 1d/32d, 0d);
        matrices.scale(0.999f, 1f/32f, 0.999f);
        matrices.translate(0.0005f, 0f, 0.0005f);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 7), matrices, vertexConsumers, light, overlay);
        matrices.pop();

        matrices.push();
        matrices.translate(0d, 1d/16d, 0d);
        matrices.scale(0.999f, 0.999f, 0.999f);
        matrices.translate(0.0005f, 0.0005f, 0.0005f);
        MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(Blocks.CARROTS.getDefaultState().with(CropBlock.AGE, entity.tickyes / 20), matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

}