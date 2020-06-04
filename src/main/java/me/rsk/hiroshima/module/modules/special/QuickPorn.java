package me.rsk.hiroshima.module.modules.special;

import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import me.rsk.hiroshima.module.Module;

import java.net.URI;

/**
 * RSK 16/05/2020
 * EZZZ PORN
 */

@Module.Info(name = "Quick Porn", category = Module.Category.SPECIAL)
public class QuickPorn extends Module {

    private Setting<Mode> mode = register(Settings.e("Mode", Mode.HENTAI));

    public enum Mode {
        STRAIGHT, GAY, LESBIAN, HENTAI
    }

    @Override
    public void onEnable() {
        if (mode.getValue() == Mode.HENTAI) {
            try {

                java.awt.Desktop.getDesktop().browse(URI.create("https://www.pornhub.com/video/search?search=hentai"));
            } catch (Exception e) {}
        }
        else if (mode.getValue().equals(Mode.LESBIAN)) {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://www.pornhub.com/video/search?search=lesbian"));
            } catch (Exception e) {}
        }
        else if (mode.getValue().equals(Mode.GAY)) {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://www.pornhub.com/video/search?search=gay"));
            } catch (Exception e) {}
        }
        else {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://www.pornhub.com/video/search?search=straight"));
            } catch (Exception e) {}
        }
        this.disable();


    }

}