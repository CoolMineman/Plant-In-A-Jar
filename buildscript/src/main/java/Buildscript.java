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
        return Minecraft.getVersion("1.18.2");
    }

    @Override
    public int getJavaVersion() {
        return 17;
    }

    @Override
    public MappingTree createMappings() {
        return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn("1.18.2+build.3")).tree;
    }

    @Override
    public FabricLoader getLoader() {
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.14.7"));
    }

    @Override
    public void getModDependencies(ModDependencyCollector d) {
        String[][] fapiModules = new String[][] {
            {"fabric-registry-sync-v0", "0.9.9+0d9ab37260"},
            {"fabric-api-base", "0.4.4+d7c144a860"},
            {"fabric-lifecycle-events-v1", "2.0.4+d8d7804a60"},
            {"fabric-networking-api-v1", "1.0.22+e6b169eb60"},
            {"fabric-rendering-v1", "1.10.7+54e5b2ec60"},
            {"fabric-screen-handler-api-v1", "1.2.1+1f6558e860"},
            {"fabric-object-builder-api-v1", "2.1.2+032c981d60"},
            {"fabric-blockrenderlayer-v1", "1.1.12+3ac43d9560"},
            {"fabric-transfer-api-v1", "1.6.2+f4563ac860"},
            {"fabric-api-lookup-api-v1", "1.6.1+2373a54560"},
            {"fabric-renderer-api-v1", "0.4.13+d882b91560"},
            {"fabric-rendering-fluids-v1", "2.0.2+54e5b2ec60"},
            {"fabric-renderer-indigo", "0.5.0+7faf0d8860"},
            {"fabric-resource-loader-v0", "0.5.1+e747827960"}
        };
        for (String[] module : fapiModules) {
            d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", module[0], module[1]), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        }
        jij(d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("org.objenesis:objenesis:3.2"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
        jij(d.addMaven(COTTONMC_MAVEN, new MavenId("io.github.cottonmc:LibGui:5.4.2+1.18.2"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
        d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("io.github.juuxel:libninepatch:1.1.0"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
        d.addMaven(COTTONMC_MAVEN, new MavenId("io.github.cottonmc:Jankson-Fabric:4.0.0+j1.2.0"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
        d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("blue.endless:jankson:1.2.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
    }
    
}
