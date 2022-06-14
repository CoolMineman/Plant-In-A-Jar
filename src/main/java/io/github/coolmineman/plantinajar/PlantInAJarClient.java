package io.github.coolmineman.plantinajar;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;

public class PlantInAJarClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(), PlantInAJar.PLANT_JAR);
        BlockEntityRendererRegistry.register(PlantInAJar.PLANT_JAR_ENTITY, c -> new JarBlockEntityRenderer());
        ScreenRegistry.<JarGuiDescription, JarBlockScreen>register(PlantInAJar.EPIC_SCREEN_HAND_YES, new StupidFactoryThing());
    }

}