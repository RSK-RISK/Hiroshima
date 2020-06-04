package me.rsk.hiroshima.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.event.events.PacketEvent;
import me.rsk.hiroshima.module.Module;
import me.rsk.hiroshima.module.ModuleManager;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Random;

/**
 * Created by 086 on 8/04/2018.
 * Updated by Darki on 12/12/19
 * Updated by TBM on 15/12/19
 * Updated by Asikesa on 1/9/2020
 */
@Module.Info(name = "ChatSuffix", category = Module.Category.CHAT, description = "Modifies your chat messages")
public class ChatAppend extends Module {

    private Setting<Boolean> commands = register(Settings.b("Commands", false));
    private Setting<Boolean> blue = register(Settings.b("Blue", false));
    private Setting<ChatAppend.Mode> mode = register(Settings.e("Mode", Mode.HIROSHIMA2));

	private final String HIROSHIMA_SFX_1 = " \u23D0 \u029c\u026a\u0280\u1d0f";
    private final String HIROSHIMA_SFX_2 = " \u23D0 \u029c\u026a\u0280\u1d0f\ua731\u029c\u026a\u1d0d\u1d00";
    private final String HIROSHIMA_SFX_3 = " \u23D0 \u1d34\u1d35\u1d3f\u1d3c\u02e2\u1d34\u1d35\u1d39\u1d2c";

    public enum Mode {
        HIROSHIMA, HIROSHIMA2, HIROSHIMA3, RANDOM
    }


    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (s.startsWith("/") && !commands.getValue()) return;
            switch (mode.getValue()) {
                case HIROSHIMA:
                    s+=doChatAppend(HIROSHIMA_SFX_1);
                    break;
                case HIROSHIMA2:
                    s+= doChatAppend(HIROSHIMA_SFX_2);
                    break;
                case HIROSHIMA3:
                    s+= doChatAppend(HIROSHIMA_SFX_3);
                    break;
                case RANDOM:
                    String[] stringlist = {HIROSHIMA_SFX_1, HIROSHIMA_SFX_2, HIROSHIMA_SFX_3};
                    Random r = new Random();
                    String randomsuffix = stringlist[r.nextInt(stringlist.length)];
                    s += doChatAppend(randomsuffix);
                    break;


            }
            if (ModuleManager.isModuleEnabled("ColourChat")) {
                return;
            }
            if (s.length() >= 256) s = s.substring(0,256);
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    });

    public String doChatAppend(String chatAppend) {
        if (blue.getValue() == true) {
            return "`" + chatAppend;
        } else {
            return chatAppend;
        }
    }

}
