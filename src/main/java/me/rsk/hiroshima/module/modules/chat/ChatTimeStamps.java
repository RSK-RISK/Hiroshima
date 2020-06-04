package me.rsk.hiroshima.module.modules.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rsk.hiroshima.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.setting.Setting;
import me.rsk.hiroshima.setting.Settings;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.Date;


@Module.Info(name = "ChatTimeStamps", category = Module.Category.CHAT)
public class ChatTimeStamps extends Module {

    private Setting<Boolean> deco = register(Settings.b("Deco", true));

    @EventHandler
    public Listener<ClientChatReceivedEvent> listener = new Listener<>(event -> {

        TextComponentString newTextComponentString = new TextComponentString(ChatFormatting.GRAY + (deco.getValue() ? "<" : "") + new SimpleDateFormat("k:mm").format(new Date()) + (deco.getValue() ? ">" : "") + ChatFormatting.RESET + " ");
        newTextComponentString.appendSibling(event.getMessage());

        event.setMessage(newTextComponentString);

    });

}