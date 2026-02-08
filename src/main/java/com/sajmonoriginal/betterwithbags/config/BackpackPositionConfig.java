package com.sajmonoriginal.betterwithbags.config;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;

import java.io.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

public class BackpackPositionConfig {

    private static final String FILE_NAME = "config/backpack_position.properties";
    private static final long RELOAD_INTERVAL_MS = 1000;

    public static class TierTransform {
        public float translateX, translateY, translateZ;
        public float rotateX, rotateY, rotateZ;
        public float scale;

        public TierTransform(float tx, float ty, float tz, float rx, float ry, float rz, float scale) {
            this.translateX = tx;
            this.translateY = ty;
            this.translateZ = tz;
            this.rotateX = rx;
            this.rotateY = ry;
            this.rotateZ = rz;
            this.scale = scale;
        }

        public TierTransform copy() {
            return new TierTransform(translateX, translateY, translateZ, rotateX, rotateY, rotateZ, scale);
        }
    }

    private static final float S = 0.0625F;
    private static final Map<BackpackTier, TierTransform> DEFAULTS = new EnumMap<>(BackpackTier.class);

    static {
        // New shared Bedrock armor model (geometry.humanoid.armor.bag).
        // Model follows standard Bedrock body hierarchy — minimal transforms needed.
        // Tweak via config/backpack_position.properties while game is running.
        DEFAULTS.put(BackpackTier.SMALL,      new TierTransform(0.0F, 24.6F, 0.0F, 0.0F, 0.0F, 180.0F, S));
        DEFAULTS.put(BackpackTier.MEDIUM,     new TierTransform(0.0F, 24.6F, 0.0F, 0.0F, 0.0F, 180.0F, S));
        DEFAULTS.put(BackpackTier.LARGE,      new TierTransform(0.0F, 24.6F, 0.0F, 0.0F, 0.0F, 180.0F, S));
        DEFAULTS.put(BackpackTier.REINFORCED, new TierTransform(0.0F, 24.6F, 0.0F, 0.0F, 0.0F, 180.0F, S));
        DEFAULTS.put(BackpackTier.NETHER,     new TierTransform(0.0F, 24.6F, 0.0F, 0.0F, 0.0F, 180.0F, S));
    }

    private static final Map<BackpackTier, TierTransform> transforms = new EnumMap<>(BackpackTier.class);

    static {
        for (BackpackTier tier : BackpackTier.values()) {
            transforms.put(tier, DEFAULTS.get(tier).copy());
        }
    }

    private static long lastLoadTime = 0;
    private static long lastModified = 0;
    private static File configFile;

    public static TierTransform get(BackpackTier tier) {
        return transforms.get(tier);
    }

    public static void tick() {
        long now = System.currentTimeMillis();
        if (now - lastLoadTime < RELOAD_INTERVAL_MS) return;
        lastLoadTime = now;

        if (configFile == null) {
            configFile = new File(FILE_NAME);
        }

        if (!configFile.exists()) {
            writeDefaults();
            return;
        }

        long mod = configFile.lastModified();
        if (mod != lastModified) {
            lastModified = mod;
            load();
        }
    }

    private static void load() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("[BackpackConfig] Failed to read config: " + e.getMessage());
            return;
        }

        for (BackpackTier tier : BackpackTier.values()) {
            String prefix = tier.getName() + ".";
            TierTransform t = transforms.get(tier);
            TierTransform def = DEFAULTS.get(tier);
            t.translateX = getFloat(props, prefix + "translateX", def.translateX);
            t.translateY = getFloat(props, prefix + "translateY", def.translateY);
            t.translateZ = getFloat(props, prefix + "translateZ", def.translateZ);
            t.rotateX = getFloat(props, prefix + "rotateX", def.rotateX);
            t.rotateY = getFloat(props, prefix + "rotateY", def.rotateY);
            t.rotateZ = getFloat(props, prefix + "rotateZ", def.rotateZ);
            t.scale = getFloat(props, prefix + "scale", def.scale);
        }

        System.out.println("[BackpackConfig] Reloaded config");
    }

    private static void writeDefaults() {
        configFile.getParentFile().mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(configFile))) {
            pw.println("# Backpack 3D Model Position Config (per-tier)");
            pw.println("# Edit while game is running - reloads every second");
            pw.println("# translate = pixel space offset, rotate = degrees, scale = 0.0625 is 1/16");
            pw.println();

            for (BackpackTier tier : BackpackTier.values()) {
                String p = tier.getName();
                TierTransform def = DEFAULTS.get(tier);
                pw.println("# --- " + p.toUpperCase() + " ---");
                pw.println(p + ".translateX=" + def.translateX);
                pw.println(p + ".translateY=" + def.translateY);
                pw.println(p + ".translateZ=" + def.translateZ);
                pw.println(p + ".rotateX=" + def.rotateX);
                pw.println(p + ".rotateY=" + def.rotateY);
                pw.println(p + ".rotateZ=" + def.rotateZ);
                pw.println(p + ".scale=" + def.scale);
                pw.println();
            }
        } catch (IOException e) {
            System.err.println("[BackpackConfig] Failed to write defaults: " + e.getMessage());
        }
    }

    private static float getFloat(Properties props, String key, float fallback) {
        String val = props.getProperty(key);
        if (val == null) return fallback;
        try {
            return Float.parseFloat(val.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
