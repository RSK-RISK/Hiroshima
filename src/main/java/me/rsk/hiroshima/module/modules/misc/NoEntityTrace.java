package me.rsk.hiroshima.module.modules.misc;

import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "NoEntityBlock", category = Module.Category.MISC, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module {

    private Setting<TraceMode> mode = register(Settings.e("Mode", TraceMode.DYNAMIC));

    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        NoEntityTrace.INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return INSTANCE.isEnabled() && (INSTANCE.mode.getValue() == TraceMode.STATIC || mc.playerController.isHittingBlock);
    }

    private enum TraceMode {
        STATIC, DYNAMIC
    }
}
