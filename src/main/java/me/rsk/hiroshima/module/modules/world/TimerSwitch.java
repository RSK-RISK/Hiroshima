package me.rsk.hiroshima.module.modules.world;

import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import org.lwjgl.input.Keyboard;

/*
 * Made by TBM
 */

@Module.Info(name = "   TBMTimerSwitch", description = "A thing to use fast timer for a short time and go fast", category = Module.Category.WORLD)
public class TimerSwitch extends Module {

    private int tickWait = 0;

    public Setting<Float> speedUsual = register(Settings.floatBuilder("Speed").withMinimum(1f).withMaximum(10f).withValue(4.2f));
    public Setting<Float> fastUsual = register(Settings.floatBuilder("Fast Speed").withMinimum(1f).withMaximum(1000f).withValue(10f));
    public Setting<Float> tickToFast = register(Settings.floatBuilder("Tick To Fast").withMinimum(0f).withMaximum(20f).withValue(4f));
    public Setting<Float> tickToNoFast = register(Settings.floatBuilder("Tick To Disable Fast").withMinimum(0f).withMaximum(20f).withValue(7f));

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50;
    }

    @Override
    public void onUpdate() {
        if (tickWait == tickToFast.getValue()) {
            mc.timer.tickLength = 50.0f / fastUsual.getValue();
            this.setHudInfo(fastUsual.getValue().toString());
        }
        if (tickWait >= tickToNoFast.getValue()) {
            tickWait = 0;
            mc.timer.tickLength = 50.0f / speedUsual.getValue();
            this.setHudInfo(speedUsual.getValue().toString());
        }
        tickWait++;
    }

}
