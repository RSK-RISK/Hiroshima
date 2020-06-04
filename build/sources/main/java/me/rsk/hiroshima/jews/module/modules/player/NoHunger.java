package me.rsk.hiroshima.jews.module.modules.player;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.gasda.event.HiroshimaEvent;
import me.rsk.hiroshima.gasda.event.events.PacketEvent;
import me.rsk.hiroshima.jews.module.Module;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SPRINTING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SPRINTING;

@Module.Info(name = "SlowHunger", category = Module.Category.PLAYER)
public class NoHunger extends Module {

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @EventHandler
    Listener<PacketEvent.Send> packetSend = new Listener<>(event -> {
        if(event.getEra() == HiroshimaEvent.Era.PRE){
            if (event.getPacket() instanceof CPacketPlayer) {
                final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
                if (mc.player.fallDistance > 0 || mc.playerController.isHittingBlock) {
                    packet.onGround = true;
                } else {
                    packet.onGround = false;
                }
            }
            if (event.getPacket() instanceof CPacketEntityAction) {
                final CPacketEntityAction packet = (CPacketEntityAction) event.getPacket();
                if (packet.getAction() == START_SPRINTING || packet.getAction() == STOP_SPRINTING) {
                    event.cancel();
                }
            }
        }
    });
}
