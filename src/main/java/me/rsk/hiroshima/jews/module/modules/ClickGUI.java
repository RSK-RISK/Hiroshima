package me.rsk.hiroshima.jews.module.modules;

import me.rsk.hiroshima.jews.gui.hiroshima.DisplayGuiScreen;
import me.rsk.hiroshima.jews.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * Created by 086 on 23/08/2017.
 */
@Module.Info(name = "clickGUI", description = "Opens the Click GUI", category = Module.Category.HIDDEN)
public class ClickGUI extends Module {

    public ClickGUI() {
        getBind().setKey(Keyboard.KEY_Y);
    }

    @Override
    protected void onEnable() {
        if (!(mc.currentScreen instanceof DisplayGuiScreen)) {
            mc.displayGuiScreen(new DisplayGuiScreen(mc.currentScreen));
        }
        disable();
    }

}
