package com.swill.hitbox;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.swill.hitbox.Config;
import com.swill.hitbox.packets.PacketInterceptor;
import io.netty.channel.Channel;

public class HitBoxMod implements ModInitializer {
    public static final String MOD_ID = "hitboxexpander";
    private static KeyBinding toggleKey;
    private static KeyBinding increaseKey;
    private static KeyBinding decreaseKey;

    @Override
    public void onInitialize() {
        Config.load();
        
        // Регистрация клавиш
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hitboxexpander.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.hitboxexpander"
        ));
        
        increaseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hitboxexpander.increase",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            "category.hitboxexpander"
        ));
        
        decreaseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hitboxexpander.decrease",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_BRACKET,
            "category.hitboxexpander"
        ));
        
        // Обработка нажатий
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.wasPressed()) {
                Config.setEnabled(!Config.isEnabled());
                if (client.player != null) {
                    client.player.sendMessage(net.minecraft.text.Text.literal(
                        "§6[HitBoxExpander] §7" + (Config.isEnabled() ? "§aON" : "§cOFF")
                    ), true);
                }
            }
            
            if (increaseKey.wasPressed() && Config.isEnabled()) {
                double newSize = Config.getHitBoxSize() + 0.1;
                if (newSize <= 3.0) {
                    Config.setHitBoxSize(newSize);
                    if (client.player != null) {
                        client.player.sendMessage(net.minecraft.text.Text.literal(
                            "§6[HitBoxExpander] §7Size: §a" + String.format("%.1f", newSize)
                        ), true);
                    }
                }
            }
            
            if (decreaseKey.wasPressed() && Config.isEnabled()) {
                double newSize = Config.getHitBoxSize() - 0.1;
                if (newSize >= 0.1) {
                    Config.setHitBoxSize(newSize);
                    if (client.player != null) {
                        client.player.sendMessage(net.minecraft.text.Text.literal(
                            "§6[HitBoxExpander] §7Size: §a" + String.format("%.1f", newSize)
                        ), true);
                    }
                }
            }
        });
        
        System.out.println("[HitBoxExpander] Loaded - SWILL edition");
    }
}
