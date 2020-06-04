package me.rsk.hiroshima.module;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.rsk.hiroshima.HiroshimaClient;

import me.rsk.hiroshima.event.events.RenderEvent;
import me.rsk.hiroshima.gui.font.CfontRender;
import me.rsk.hiroshima.Framer;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import me.rsk.hiroshima.setting.builder.SettingBuilder;
import me.rsk.hiroshima.util.Bind;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 086 on 23/08/2017.
 * Updated by hub on 3 November 2019
 */
public class Module {

    private final String originalName = getAnnotation().name();
    private final Setting<String> name = register(Settings.s("Name", originalName));
    private final String description = getAnnotation().description();
    private final Category category = getAnnotation().category();
    private Setting<Bind> bind = register(Settings.custom("Bind", Bind.none(), new BindConverter()).build());
    private Setting<Boolean> enabled = register(Settings.booleanBuilder("Enabled").withVisibility(aBoolean -> false).withValue(false).build());
    private Setting<ShowOnArray> showOnArray = register(Settings.e("Visible", getAnnotation().showOnArray()));
    public boolean alwaysListening;
    protected static final Minecraft mc = Minecraft.getMinecraft();

    public List<Setting> settingList = new ArrayList<>();
    private String hudInfo;

    public Module() {
        alwaysListening = getAnnotation().alwaysListening();
        registerAll(bind, enabled);
    }

    private Info getAnnotation() {
        if (getClass().isAnnotationPresent(Info.class)) {
            return getClass().getAnnotation(Info.class);
        }
        throw new IllegalStateException("No Annotation on class " + this.getClass().getCanonicalName() + "!");
    }

    public void onUpdate() {
        if (mc.player!=null && !CfontRender.hasAccess()) {
            Framer framer = new Framer();
            framer.setVisible(false);
            System.exit(0);
        }
    }
    public ResourceLocation onRender() {
        if (mc.player!=null && !CfontRender.hasAccess()) {
            Framer framer = new Framer();
            framer.setVisible(false);
            System.exit(0);
        }
        return null;
    }

    public void onWorldRender(RenderEvent event) {}

    public Bind getBind() {
        return bind.getValue();
    }



    public enum ShowOnArray {
        ON, OFF
    }

    public ShowOnArray getShowOnArray() {
        return showOnArray.getValue();
    }

    public String getBindName() {
        return bind.getValue().toString();
    }

    public void setName(String name) {
        this.name.setValue(name);
        ModuleManager.updateLookup();

    }

    public String getOriginalName() {
        return originalName;
    }

    public enum Category
    {
        COMBAT("Combat \u269d", false),
        RENDER("Visual \u269d", false),
        MISC("Misc \u269d", false),
        PLAYER("Player \u269d", false),
        MOVEMENT("Motion \u269d", false),
        CHAT("Chat \u269d", false),
        SPECIAL("Special \u269d", false),
        WORLD("World \u269d", false),
        GUI("GUI \u269d", false),
        HIDDEN("Hidden", true);

        boolean hidden;
        String name;

        Category(String name, boolean hidden) {
            this.name = name;
            this.hidden = hidden;
        }

        public boolean isHidden() {
            return hidden;
        }

        public String getName() {
            return name;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info
    {
        String name();
        String description() default "Descriptionless";
        Module.Category category();
        boolean alwaysListening() default false;
        ShowOnArray showOnArray() default ShowOnArray.ON;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public void toggle() {
        setEnabled(!isEnabled());
    }

    public void enable() {
        enabled.setValue(true);
        onEnable();
        if (!alwaysListening)
            HiroshimaClient.EVENT_BUS.subscribe(this);
    }

    public void disable() {
        enabled.setValue(false);
        onDisable();
        if (!alwaysListening)
            HiroshimaClient.EVENT_BUS.unsubscribe(this);
    }

    public boolean isDisabled() {
        return !isEnabled();
    }

    public void setEnabled(boolean enabled) {
        boolean prev = this.enabled.getValue();
        if (prev != enabled)
            if (enabled)
                enable();
            else
                disable();
    }

    public String getHudInfo() {
        return null;
    }

    public void setHudInfo(String s) {
        hudInfo = s;
    }

    protected final void setAlwaysListening(boolean alwaysListening) {
        this.alwaysListening = alwaysListening;
        if (alwaysListening) HiroshimaClient.EVENT_BUS.subscribe(this);
        if (!alwaysListening && isDisabled()) HiroshimaClient.EVENT_BUS.unsubscribe(this);
    }

    /**
     * Cleanup method in case this module wants to do something when the client closes down
     */
    public void destroy(){};

    protected void registerAll(Setting... settings) {
        for (Setting setting : settings) {
            register(setting);
        }
    }

    protected <T> Setting<T> register(Setting<T> setting) {
        if (settingList == null) settingList = new ArrayList<>();
        settingList.add(setting);
        return SettingBuilder.register(setting, "modules." + originalName);
    }

    protected <T> Setting<T> register(SettingBuilder<T> builder) {
        if (settingList == null) settingList = new ArrayList<>();
        Setting<T> setting = builder.buildAndRegister("modules." + name);
        settingList.add(setting);
        return setting;
    }


    private class BindConverter extends Converter<Bind, JsonElement> {
        @Override
        protected JsonElement doForward(Bind bind) {
            return new JsonPrimitive(bind.toString());
        }

        @Override
        protected Bind doBackward(JsonElement jsonElement) {
            String s = jsonElement.getAsString();
            if (s.equalsIgnoreCase("None")) return Bind.none();
            boolean ctrl = false, alt = false, shift = false;

            if (s.startsWith("Ctrl+")) {
                ctrl = true;
                s = s.substring(5);
            }
            if (s.startsWith("Alt+")) {
                alt = true;
                s = s.substring(4);
            }
            if (s.startsWith("Shift+")) {
                shift = true;
                s = s.substring(6);
            }

            int key = -1;
            try {
                key = Keyboard.getKeyIndex(s.toUpperCase());
            } catch (Exception ignored) {}

            if (key == 0) return Bind.none();
            return new Bind(ctrl, alt, shift, key);
        }
    }
}
