package net.mindoth.shadowizardlib.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    public static final String KEY_TOGGLE = "shadowizardlib.toggle";
    public static final String KEY_CATEGORY_SHADOWIZARDLIB = "key.category.shadowizardlib";
    public static final KeyMapping TOGGLE = new KeyMapping(KEY_TOGGLE, GLFW.GLFW_KEY_I, KEY_CATEGORY_SHADOWIZARDLIB);
}
