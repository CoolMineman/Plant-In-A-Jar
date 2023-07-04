import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag;
import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.cfr.CfrDecompiler;
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
        return Minecraft.getVersion("1.20.1");
    }

    @Override
    public int getJavaVersion() {
        return 17;
    }

    @Override
    public MappingTree createMappings() {
        return Yarn.ofMaven(FabricMaven.URL, FabricMaven.yarn("1.20.1+build.8")).tree;
    }

    @Override
    public FabricLoader getLoader() {
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.14.21"));
    }

    @Override
    public void getModDependencies(ModDependencyCollector d) {
        String[][] fapiModules = new String[][] {
            {"fabric-registry-sync-v0", "2.2.6+b3afc78b77"},
            {"fabric-api-base", "0.4.29+b04edc7a77"},
            {"fabric-lifecycle-events-v1", "2.2.20+b3afc78b77"},
            {"fabric-networking-api-v1", "1.3.8+b3afc78b77"},
            {"fabric-rendering-v1", "3.0.6+b3afc78b77"},
            {"fabric-screen-handler-api-v1", "1.3.27+b3afc78b77"},
            {"fabric-object-builder-api-v1", "11.0.6+b3afc78b77"},
            {"fabric-blockrenderlayer-v1", "1.1.39+b3afc78b77"},
            {"fabric-transfer-api-v1", "3.2.2+b3afc78b77"},
            {"fabric-api-lookup-api-v1", "1.6.34+4d8536c977"},
            {"fabric-renderer-api-v1", "3.1.0+c154966e77"},
            {"fabric-rendering-fluids-v1", "3.0.26+b3afc78b77"},
            {"fabric-renderer-indigo", "1.4.0+c154966e77"},
            {"fabric-resource-loader-v0", "0.11.7+f7923f6d77"}
        };
        for (String[] module : fapiModules) {
            d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", module[0], module[1]), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        }
        jij(d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("org.objenesis:objenesis:3.2"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
        jij(d.addMaven(COTTONMC_MAVEN, new MavenId("io.github.cottonmc:LibGui:8.0.1+1.20"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
        d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("io.github.juuxel:libninepatch:1.2.0"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
        d.addMaven(COTTONMC_MAVEN, new MavenId("io.github.cottonmc:Jankson-Fabric:5.0.1+j1.2.2"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
        d.addMaven(Maven.MAVEN_CENTRAL, new MavenId("blue.endless:jankson:1.2.2"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME); // jij'd in libgui
    }
    
    @Override
    public BrachyuraDecompiler decompiler() {
        return new CfrDecompiler(true);
    }
}
