package io.github.coolmineman.plantinajar.tree;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.BakedQuad;

public class QuadWithColor {
    public final BakedQuad quad;
    public final RenderLayer renderLayer;
    public final float r;
    public final float g;
    public final float b;
    
    public QuadWithColor(BakedQuad quad, RenderLayer renderLayer, float r, float g, float b) {
        this.quad = quad;
        this.renderLayer = renderLayer;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
