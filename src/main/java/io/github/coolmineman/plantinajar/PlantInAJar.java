package io.github.coolmineman.plantinajar;

import java.util.Random;

import io.github.coolmineman.plantinajar.compat.CompatManager;
import io.github.coolmineman.plantinajar.config.AutoConfigurater;
import io.github.coolmineman.plantinajar.fake.FakeServerWorld;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class PlantInAJar implements ModInitializer {

	public static final AutoConfigurater CONFIG;

	static {
		AutoConfig.register(AutoConfigurater.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(AutoConfigurater.class).getConfig();
	}

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
		PLANT_JAR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "plantinajar:plant_jar", BlockEntityType.Builder.create(JarBlockEntity::new, PLANT_JAR).build(null));
		EPIC_SCREEN_HAND_YES = ScreenHandlerRegistry.registerSimple(new Identifier("plantinajar", "plant_jar"), (syncId, inventory) -> new JarGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));
		CompatManager.init();
		System.out.println("You Can Put Your Plants In Jars Now!");
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			FakeServerWorld fakeServerWorld = FakeServerWorld.create();
			fakeServerWorld.setBlockState(new BlockPos(0, 49, 0), Blocks.DIRT.getDefaultState());
			fakeServerWorld.setBlockState(new BlockPos(0, 50, 0), Blocks.ACACIA_SAPLING.getDefaultState().with(SaplingBlock.STAGE, 1));
			((SaplingBlock)Blocks.ACACIA_SAPLING).generate(fakeServerWorld, new BlockPos(0, 50, 0), Blocks.ACACIA_SAPLING.getDefaultState().with(SaplingBlock.STAGE, 1), new Random());
			System.out.println(fakeServerWorld);
		});
		
	}
}
