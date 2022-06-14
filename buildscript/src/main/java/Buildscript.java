import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.SimpleFabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends SimpleFabricProject {
    public static final String COTTONMC_MAVEN = "https://server.bbkr.space/artifactory/libs-release";

    @Override
    public VersionMeta createMcVersion() {
        return Minecraft.getVersion("1.19");
    }

    @Override
    public int getJavaVersion() {
        return 17;
    }

    @Override
    public MappingTree createMappings() {
        return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn("1.19+build.2")).tree;
    }

    @Override
    public FabricLoader getLoader() {
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.14.7"));
    }

    @Override
    public void getModDependencies(ModDependencyCollector d) {
        String[][] fapiModules = new String[][] {
            {"fabric-registry-sync-v0", "0.9.16+92cf9a3ea9"},
            {"fabric-api-base", "0.4.9+e62f51a3a9"},
            {"fabric-lifecycle-events-v1", "2.1.0+33fbc738a9"},
            {"fabric-networking-api-v1", "1.0.27+7fe97409a9"},
            {"fabric-rendering-v1", "1.10.14+9ff28f40a9"},
            {"fabric-screen-handler-api-v1", "1.2.6+9ff28f40a9"},
            {"fabric-object-builder-api-v1", "4.0.5+9ff28f40a9"},
            {"fabric-blockrenderlayer-v1", "1.1.18+9ff28f40a9"},
            {"fabric-transfer-api-v1", "2.0.9+e62f51a3a9"},
            {"fabric-api-lookup-api-v1", "1.6.7+9ff28f40a9"},
            {"fabric-renderer-api-v1", "1.0.8+9ff28f40a9"},
            {"fabric-rendering-fluids-v1", "3.0.5+9ff28f40a9"},
            {"fabric-renderer-indigo", "0.6.6+9ff28f40a9"},
            {"fabric-resource-loader-v0", "0.5.3+9e7660c6a9"}
        };
        for (String[] module : fapiModules) {
            d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", module[0], module[1]), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        }
        jij(d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("org.objenesis:objenesis:3.2"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
        jij(d.addMaven(COTTONMC_MAVEN, new MavenId("io.github.cottonmc:LibGui:6.0.0-beta.5+1.19-pre1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
        d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("io.github.juuxel:libninepatch:1.1.0"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
        d.addMaven(COTTONMC_MAVEN, new MavenId("io.github.cottonmc:Jankson-Fabric:4.1.1+j1.2.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
        d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("blue.endless:jankson:1.2.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
    }
    
}
