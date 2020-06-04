package me.rsk.hiroshima.jews.module.modules.misc;

import me.rsk.hiroshima.DiscordPresence;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;

@Module.Info(name = "DiscordRPC", category = Module.Category.MISC, description = "Discord Rich Presence")
public class DiscordRPCModule extends Module {

    public Setting<Boolean> startupGlobal = register(Settings.b("Enable Automatically", false));
    public Setting<Boolean> versionGlobal = register(Settings.b("Version", true));
    public Setting<Boolean> usernameGlobal = register(Settings.b("Username", true));
    public Setting<Boolean> coordsGlobal = register(Settings.b("Coordinates", false));
    public Setting<Boolean> hpGlobal = register(Settings.b("Health", true));
    public Setting<Boolean> ipGlobal = register(Settings.b("Server IP", true));

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    public void onDisable() { DiscordPresence.disable(); }
}
