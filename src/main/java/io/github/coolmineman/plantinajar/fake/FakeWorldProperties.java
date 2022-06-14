package io.github.coolmineman.plantinajar.fake;

import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldProperties;

public enum FakeWorldProperties implements WorldProperties {
    INSTANCE;

    @Override
    public int getSpawnX() {
        return 0;
    }

    @Override
    public int getSpawnY() {
        return 0;
    }

    @Override
    public int getSpawnZ() {
        return 0;
    }

    @Override
    public float getSpawnAngle() {
        return 0;
    }

    @Override
    public long getTime() {
        return 0;
    }

    @Override
    public long getTimeOfDay() {
        return 0;
    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public boolean isRaining() {
        return false;
    }

    @Override
    public void setRaining(boolean var1) {
        //noop
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public GameRules getGameRules() {
        return null;
    }

    @Override
    public Difficulty getDifficulty() {
        return null;
    }

    @Override
    public boolean isDifficultyLocked() {
        return false;
    }
    
}
