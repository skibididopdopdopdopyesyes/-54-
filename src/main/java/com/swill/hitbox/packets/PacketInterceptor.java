package com.swill.hitbox.packets;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;
import com.swill.hitbox.Config;
import com.swill.hitbox.utils.BypassHelper;

public class PacketInterceptor {
    
    public static PlayerInteractEntityC2SPacket modifyAttackPacket(PlayerInteractEntityC2SPacket original, Entity target) {
        if (!Config.isEnabled() || Config.isSilentMode()) {
            return original;
        }
        
        // Подмена hitVec на точку внутри оригинального хитбокса
        Vec3d realHitPoint = BypassHelper.getClosestPointOnOriginalBox(target);
        if (realHitPoint != null) {
            return PlayerInteractEntityC2SPacket.interactAt(target, realHitPoint, original.getHand());
        }
        
        return original;
    }
}
