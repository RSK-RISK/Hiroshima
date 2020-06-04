package me.rsk.hiroshima.module.modules.world;

import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;

@Module.Info(name = "Timer", category = Module.Category.WORLD)
public class Timer extends Module{
    private Setting<Float> speed = register(Settings.f("Speed", 4.0f));

    @Override
    protected void onDisable() {
        super.onDisable();
        mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        mc.timer.tickLength = 50.0f / speed.getValue();
    }
}
