package com.w1z4r_d.noboringarrows;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/** Налаштування моду, зберігаються в config/noboringarrows.json. */
public final class StuckConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("noboringarrows.json");
    private static StuckConfig instance;

    /** -1 = без обмеження (як у ванілі), 0 = не рендерити зовсім, n = не більше n штук. */
    public static final int UNLIMITED = -1;
    public static final int MAX_SLIDER = 20;

    /** На кому діють обмеження. */
    public enum Scope {
        ALL, SELF, OTHERS;

        public boolean appliesTo(boolean isLocalPlayer) {
            return switch (this) {
                case ALL -> true;
                case SELF -> isLocalPlayer;
                case OTHERS -> !isLocalPlayer;
            };
        }

        public Scope next() {
            return values()[(ordinal() + 1) % values().length];
        }

        public String translationKey() {
            return "noboringarrows.scope." + name().toLowerCase();
        }
    }

    /** Головний перемикач, його змінює клавіша з налаштувань керування. */
    public boolean enabled = true;
    public Scope scope = Scope.ALL;
    public int maxArrows = 0;
    public int maxStingers = 0;

    public static StuckConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static StuckConfig load() {
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                StuckConfig loaded = GSON.fromJson(reader, StuckConfig.class);
                if (loaded != null) {
                    loaded.sanitize();
                    return loaded;
                }
            } catch (Exception e) {
                NoBoringArrowsClient.LOGGER.warn("Не вдалося прочитати конфіг, використовую стандартні значення", e);
            }
        }
        StuckConfig fresh = new StuckConfig();
        fresh.save();
        return fresh;
    }

    private void sanitize() {
        if (scope == null) scope = Scope.ALL;
        maxArrows = clampSetting(maxArrows);
        maxStingers = clampSetting(maxStingers);
    }

    private static int clampSetting(int value) {
        return value < 0 ? UNLIMITED : Math.min(value, MAX_SLIDER);
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            NoBoringArrowsClient.LOGGER.warn("Не вдалося зберегти конфіг", e);
        }
    }
}
