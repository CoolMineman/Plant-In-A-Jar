package io.github.coolmineman.plantinajar.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigHolder {
    private static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("plantinajar.json");
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static final ConfigHolder INSTANCE = new ConfigHolder();

    private ConfigHolder() {
        try {
            load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public AutoConfigurater autoConfigurater;

    public void load() throws IOException {
        if (Files.exists(CONFIG_FILE)) {
            try (BufferedReader r = Files.newBufferedReader(CONFIG_FILE)) {
                autoConfigurater = GSON.fromJson(r, AutoConfigurater.class);
            }
        } else {
            autoConfigurater = new AutoConfigurater();
            rawsave();
        }
        autoConfigurater.postLoad();
    }

    public void save() {
        new Thread(() -> {
            LOCK.lock();
            try {
                rawsave();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                LOCK.unlock();
            }
        })
        .start();
    }

    public void rawsave() throws IOException {
        Path temp = Files.createTempFile(FabricLoader.getInstance().getConfigDir(), "plantinajar", "tmp");
        try {
            try (BufferedWriter w = Files.newBufferedWriter(temp)) {
                GSON.toJson(autoConfigurater, w);
            }
            Files.move(temp, CONFIG_FILE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            try {
                Files.delete(temp);
            } catch (Exception e0) {
                // noop
            }
            throw e;
        }
    }
}
