package com.swill.hitbox.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import java.util.Random;

public class BypassHelper {
    private static final Random RANDOM = new Random();
    
    public static double bypassAAC(double expand, Entity target) {
        // AAC не любит резкие изменения, добавляем плавность
        double smoothFactor = 0.85 + (RANDOM.nextDouble() * 0.1);
        return expand * smoothFactor;
    }
    
    public static double bypassGrim(double expand, Entity target) {
        // Grim проверяет consistency, делаем размер динамическим
        MinecraftClient client = MinecraftClient.getInstance();
        double distance = client.player != null ? client.player.distanceTo(target) : 5.0;
        
        if (distance < 2.0) {
            return expand * 0.3;
        } else if (distance < 4.0) {
            return expand * 0.7;
        }
        return expand;
    }
    
    public static double bypassVulcan(double expand, Entity target) {
        // Vulcan проверяет через трассировку лучей, добавляем offset
        double offset = (RANDOM.nextDouble() - 0.5) * 0.2;
        double result = expand + offset;
        return Math.max(0.1, Math.min(2.0, result));
    }
    
    public static Vec3d getClosestPointOnOriginalBox(Entity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return null;
        
        Vec3d eyePos = client.player.getEyePos();
        Box originalBox = entity.getBoundingBox(); // Оригинальный бокс без расширения
        
        // Находим ближайшую точку на боксе
        double x = Math.max(originalBox.minX, Math.min(eyePos.x, originalBox.maxX));
        double y = Math.max(originalBox.minY, Math.min(eyePos.y, originalBox.maxY));
        double z = Math.max(originalBox.minZ, Math.min(eyePos.z, originalBox.maxZ));
        
        return new Vec3d(x, y, z);
    }
    
    public static boolean shouldBypassCheck(Entity target) {
        // Рандомная задержка для обхода проверок на скорость
        return RANDOM.nextInt(100) > 95;
    }
}
