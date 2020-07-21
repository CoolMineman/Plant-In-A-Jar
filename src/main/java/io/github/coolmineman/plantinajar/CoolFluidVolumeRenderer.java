package io.github.coolmineman.plantinajar;

import java.util.List;

import alexiil.mc.lib.attributes.fluid.render.DefaultFluidVolumeRenderer;
import alexiil.mc.lib.attributes.fluid.render.FluidRenderFace;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;

public class CoolFluidVolumeRenderer extends DefaultFluidVolumeRenderer {
    public static final DefaultFluidVolumeRenderer INSTANCE = new CoolFluidVolumeRenderer();
    
    @Override
    public void render(
        FluidVolume fluid, List<FluidRenderFace> faces, VertexConsumerProvider vcp, MatrixStack matrices
    ) {
        Sprite[] sprites = getSprites(fluid);
        if (MinecraftClient.isFabulousGraphicsOrBetter())
            renderSimpleFluid(faces, vcp.getBuffer(RenderLayer.getTranslucentMovingBlock()), matrices, sprites[0], sprites[1], fluid.getRenderColor());
        else
            renderSimpleFluid(faces, vcp.getBuffer(RenderLayer.getTranslucentNoCrumbling()), matrices, sprites[0], sprites[1], fluid.getRenderColor());
    }
}