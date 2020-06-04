package me.rsk.hiroshima.jews.module.modules.misc;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.gasda.event.events.PacketEvent;
import me.rsk.hiroshima.jews.module.Module;
import net.minecraft.network.play.client.CPacketCloseWindow;

/*
 * Hamburger added 25/02/2020
 */

@Module.Info(name = "MoreInv", category = Module.Category.MISC, description = "Store items in crafting slots")
public class XCarry extends Module {

    @EventHandler
    private Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketCloseWindow){
                event.cancel();
            }

    });
}
