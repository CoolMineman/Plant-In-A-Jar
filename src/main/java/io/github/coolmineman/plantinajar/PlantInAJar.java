package io.github.coolmineman.plantinajar;

import io.github.coolmineman.plantinajar.config.ConfigHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PlantInAJar implements ModInitializer {

	public static final ConfigHolder CONFIG = ConfigHolder.INSTANCE;

	public static final Block PLANT_JAR = new JarBlock(FabricBlockSettings.copyOf(Blocks.GLASS));
	public static BlockEntityType<JarBlockEntity> PLANT_JAR_ENTITY;
	public static ScreenHandlerType<JarGuiDescription> EPIC_SCREEN_HAND_YES;
	

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.BLOCK, new Identifier("plantinajar", "plant_jar"), PLANT_JAR);
		Registry.register(Registry.ITEM, new Identifier("plantinajar", "plant_jar"), new BlockItem(PLANT_JAR, new Item.Settings().group(ItemGroup.MISC)));
		PLANT_JAR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "plantinajar:plant_jar", FabricBlockEntityTypeBuilder.create(JarBlockEntity::new, PLANT_JAR).build(null));
		EPIC_SCREEN_HAND_YES = ScreenHandlerRegistry.registerSimple(new Identifier("plantinajar", "plant_jar"), (syncId, inventory) -> new JarGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));
		System.out.println("You Can Put Your Plants In Jars Now!");
		
	}
}
