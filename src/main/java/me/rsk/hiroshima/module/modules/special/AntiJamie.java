package me.rsk.hiroshima.module.modules.special;

import me.rsk.hiroshima.module.Module;

import java.net.URI;

/**
 * RSK 16/05/2020
 */
@Module.Info(name = "QuickStress", category = Module.Category.SPECIAL)
public class AntiJamie extends Module{
    public void onEnable() {

 try {

     java.awt.Desktop.getDesktop().browse(URI.create("https://freestresser.to/"));
 } catch (Exception e) {

        }
    }
}
