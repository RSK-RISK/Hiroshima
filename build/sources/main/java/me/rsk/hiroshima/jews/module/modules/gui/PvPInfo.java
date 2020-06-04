package me.rsk.hiroshima.jews.module.modules.gui;

import java.awt.Color;
import java.awt.Font;

import me.rsk.hiroshima.jews.gui.font.CFontRenderer;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.jews.module.ModuleManager;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;
import me.rsk.hiroshima.gasda.util.ColourUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@Module.Info(name="PvPInfo", category=Module.Category.GUI)
public class PvPInfo extends Module {
    private Setting<Float> x = this.register(Settings.f("InfoX", 0.0f));
    private Setting<Float> y = this.register(Settings.f("InfoY", 200.0f));
    private Setting<Boolean> rainbow = this.register(Settings.b("Rainbow", false));
    private Setting<Boolean> notitle = this.register(Settings.b("NoTitle", false));
    private Setting<Integer> red = this.register(Settings.integerBuilder("Red").withRange(0, 255).withValue(0).build());
    private Setting<Integer> green = this.register(Settings.integerBuilder("Green").withRange(0, 255).withValue(170).build());
    private Setting<Integer> blue = this.register(Settings.integerBuilder("Blue").withRange(0, 255).withValue(170).build());
    private Setting<Integer> Lred = this.register(Settings.integerBuilder("TitleRed").withRange(0, 255).withValue(170).build());
    private Setting<Integer> Lgreen = this.register(Settings.integerBuilder("TitleGreen").withRange(0, 255).withValue(170).build());
    private Setting<Integer> Lblue = this.register(Settings.integerBuilder("TitleBlue").withRange(0, 255).withValue(170).build());
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Segoe UI", 0, 18), true, false);

    @Override
    public ResourceLocation onRender() {
        int drgb;
        float yCount = this.y.getValue().floatValue();
        int ared = this.red.getValue();
        int bgreen = this.green.getValue();
        int cblue = this.blue.getValue();
        int color = drgb = ColourUtils.toRGBA(ared, bgreen, cblue, 255);
        int Lred = this.Lred.getValue();
        int Lgreen = this.Lgreen.getValue();
        int Lblue = this.Lblue.getValue();
        int color2 = drgb = ColourUtils.toRGBA(Lred, Lgreen, Lblue, 255);
        int color3 = drgb = ColourUtils.toRGBA(85, 255, 255, 255);
        int totems = PvPInfo.mc.player.inventory.mainInventory.stream().filter(itemStack -> {
            if (itemStack.getItem() != Items.TOTEM_OF_UNDYING) return false;
            return true;
        }).mapToInt(ItemStack::getCount).sum();
        if (PvPInfo.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++totems;
        }
        if (this.rainbow.getValue().booleanValue()) {
            int argb;
            float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
            int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            int red = rgb >> 16 & 255;
            int green = rgb >> 8 & 255;
            int blue = rgb & 255;
            color = argb = ColourUtils.toRGBA(red, green, blue, 255);
            color2 = argb = ColourUtils.toRGBA(red, green, blue, 255);
            color3 = argb = ColourUtils.toRGBA(red, green, blue, 255);
        }
        if (this.notitle.getValue().booleanValue()) {
            int argb;
            float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
            int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            int Pred = 255;
            int Pgreen = 255;
            int Pblue = 255;
            color2 = argb = ColourUtils.toRGBA(Pred, Pgreen, Pblue, 0.1);
        }
        {
            this.cFontRenderer.drawStringWithShadow("HIROSHIMA", this.x.getValue().floatValue(), yCount - (float) this.cFontRenderer.getHeight() - 1.0f, color2);
            this.cFontRenderer.drawStringWithShadow("AT: " + this.getAutoTrap(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);
            this.cFontRenderer.drawStringWithShadow("HF: " + this.getHoleFiller(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);
            this.cFontRenderer.drawStringWithShadow("SU: " + this.getSurround(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);
            this.cFontRenderer.drawStringWithShadow("CA: " + this.getCaura(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);
            this.cFontRenderer.drawStringWithShadow("KA: " + this.getKA(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);

            return null;
        }
    }

    private String getAutoTrap() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("AutoTrap") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("AutoTrap").isEnabled()).toUpperCase();
    }

    private String getSurround() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("Surround") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("Surround").isEnabled()).toUpperCase();
    }

    private String getCaura() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("HiroshimaCA") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("HiroshimaCA").isEnabled()).toUpperCase();
    }

    private String getKA() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("Aura") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("Aura").isEnabled()).toUpperCase();
    }

    private String getHoleFiller() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("HoleFiller") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("HoleFiller").isEnabled()).toUpperCase();
    }

}




