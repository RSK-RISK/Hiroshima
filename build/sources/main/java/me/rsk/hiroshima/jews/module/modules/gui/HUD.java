package me.rsk.hiroshima.jews.module.modules.gui;

import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;

@Module.Info(
        name = "HUD",
        description = "",
        category = Module.Category.RENDER
)
public class HUD extends Module {
    private Setting r = this.register(Settings.integerBuilder("Main Red").withMinimum(1).withMaximum(255).withValue((int)63));
    private Setting g = this.register(Settings.integerBuilder("Main Green").withMinimum(1).withMaximum(255).withValue((int)0));
    private Setting b = this.register(Settings.integerBuilder("Main Blue").withMinimum(1).withMaximum(255).withValue((int)31));
    private Setting bgr = this.register(Settings.integerBuilder("Main Blue").withMinimum(1).withMaximum(255).withValue((int)31));
    private Setting bgg = this.register(Settings.integerBuilder("Main Blue").withMinimum(1).withMaximum(255).withValue((int)31));
    private Setting bgb = this.register(Settings.integerBuilder("Main Blue").withMinimum(1).withMaximum(255).withValue((int)31));
    private Setting amr = this.register(Settings.integerBuilder("ActiveModules Red").withMinimum(1).withMaximum(255).withValue((int)255));
    private Setting amg = this.register(Settings.integerBuilder("ActiveModules Green").withMinimum(1).withMaximum(255).withValue((int)0));
    private Setting amb = this.register(Settings.integerBuilder("ActiveModules Blue").withMinimum(1).withMaximum(255).withValue((int)255));
    private Setting back = this.register(Settings.b("Default", false));
    private static HUD INSTANCE = new HUD();

    public HUD() {
        INSTANCE = this;
    }

    public static int Mred() {
        return (Integer)INSTANCE.r.getValue();
    }

    public static int Mgreen() {
        return (Integer)INSTANCE.g.getValue();
    }

    public static int Mblue() {
        return (Integer)INSTANCE.b.getValue();
    }

    public static int AMred() {
        return (Integer)INSTANCE.amr.getValue();
    }

    public static int AMgreen() {
        return (Integer)INSTANCE.amg.getValue();
    }

    public static int AMblue() {
        return (Integer)INSTANCE.amg.getValue();
    }

    public static int BGred() {return (Integer)INSTANCE.bgr.getValue();}
    public static int BGgreen() {return (Integer)INSTANCE.bgg.getValue();}
    public static int BGblue() {return (Integer)INSTANCE.bgb.getValue();}

    public static float redF() {
        return ((Integer)INSTANCE.r.getValue()).floatValue();
    }

    public static float greenF() {
        return ((Integer)INSTANCE.g.getValue()).floatValue();
    }

    public static float blueF() {
        return ((Integer)INSTANCE.b.getValue()).floatValue();
    }

    public void onUpdate() {
        if ((Boolean)this.back.getValue()) {
            this.r.setValue(0);
            this.g.setValue(170);
            this.b.setValue(170);
            this.amr.setValue(0);
            this.amg.setValue(170);
            this.amb.setValue(170);
            this.back.setValue(false);
        }

    }

    protected void onDisable() {
        this.enable();
    }

    public static enum Pos {
        TopRight,
        TopLeft,
        BottomRight,
        BottomLeft,
        None;
    }
}
