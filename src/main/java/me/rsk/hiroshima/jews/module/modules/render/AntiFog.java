package me.rsk.hiroshima.jews.module.modules.render;

import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;

/**
 * Created by 086 on 9/04/2018.
 */
@Module.Info(name = "NoFog", description = "Disables or reduces fog", category = Module.Category.RENDER)
public class AntiFog extends Module {

    public static Setting<VisionMode> mode = Settings.e("Mode", VisionMode.NOFOG);
    private static AntiFog INSTANCE = new AntiFog();

    public AntiFog() {
        INSTANCE = this;
        register(mode);
    }

    public static boolean enabled() {
        return INSTANCE.isEnabled();
    }

    public enum VisionMode {
        NOFOG, AIR
    }

}
