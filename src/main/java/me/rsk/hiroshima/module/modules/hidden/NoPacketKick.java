package me.rsk.hiroshima.module.modules.hidden;

import me.rsk.hiroshima.module.Module;

/**
 * @author 086
 * @see me.rsk.hiroshima.mixin.client.MixinNetworkManager
 */
@Module.Info(name = "NoPacketKick", category = Module.Category.MISC, description = "Prevent large packets from kicking you")
public class NoPacketKick {
    private static NoPacketKick INSTANCE;

    public NoPacketKick() {
        INSTANCE = this;
    }

    public static boolean isEnabled() {
        return INSTANCE.isEnabled();
    }

}
