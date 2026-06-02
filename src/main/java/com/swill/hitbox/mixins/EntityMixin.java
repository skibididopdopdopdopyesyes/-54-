package com.swill.hitbox.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import com.swill.hitbox.Config;
import com.swill.hitbox.utils.BypassHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    
    @Shadow
    public abstract Box getBoundingBox();
    
    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void expandHitBox(CallbackInfoReturnable<Box> cir) {
        if (!Config.isEnabled()) return;
        
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        
        Entity self = (Entity)(Object)this;
        
        // Не расширяем себя
        if (self == client.player) return;
        
        // OnlyInCombat режим
        if (Config.isOnlyInCombat()) {
            if (client.player.getAttacking() == null && client.player.getAttackCooldownProgress(0) >= 0.9f) {
                // Не в бою
                return;
            }
        }
        
        Box original = cir.getReturnValue();
        double expand = Config.getHitBoxSize();
        
        // Обход античитов
        if (Config.getBypassLevel() >= 1) {
            expand = BypassHelper.bypassAAC(expand, self);
        }
        if (Config.getBypassLevel() >= 2) {
            expand = BypassHelper.bypassGrim(expand, self);
        }
        if (Config.getBypassLevel() >= 3) {
            expand = BypassHelper.bypassVulcan(expand, self);
        }
        
        Box expanded = original.expand(expand, expand, expand);
        cir.setReturnValue(expanded);
    }
    
    @Inject(method = "getVisibilityBoundingBox", at = @At("RETURN"), cancellable = true)
    private void expandVisibilityBox(CallbackInfoReturnable<Box> cir) {
        if (!Config.isEnabled()) return;
        
        Box original = cir.getReturnValue();
        double expand = Config.getHitBoxSize() * 0.5;
        
        cir.setReturnValue(original.expand(expand, expand, expand));
    }
}
