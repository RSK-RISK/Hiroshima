package me.rsk.hiroshima.jews.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.gasda.command.Command;
import me.rsk.hiroshima.gasda.event.events.PacketEvent;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.List;

import static me.rsk.hiroshima.gasda.util.FileHelper.appendTextFile;

/**
 * Created by hub on 11 June 2019.
 * Updated by hub on 13 December 2019
 * <p>
 * This is a enhanced skid of: EbicClient/blob/master/client/hacks/CoordTpExploit.java
 */
@Module.Info(name = "CoordLogger", description = "Coord Logger", category = Module.Category.MISC)
public class CoordLogger extends Module {

    private static final String fileName = "Hiroshima_CoordLogger.txt";

    private Setting<Boolean> tp = register(Settings.b("TpExploit", false));
    private Setting<Boolean> lightning = register(Settings.b("Thunder", false));
    private Setting<Boolean> portal = register(Settings.b("EndPortal", false));
    private Setting<Boolean> wither = register(Settings.b("Wither", false));
    private Setting<Boolean> dragon = register(Settings.b("Dragon", false));
    private Setting<Boolean> savetofile = register(Settings.b("SaveToFile", false));

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {

        if (lightning.getValue() && event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.WEATHER && packet.getSound() == SoundEvents.ENTITY_LIGHTNING_THUNDER) {
                sendNotification("[CoordLogger]: " + ChatFormatting.RED.toString() + "Lightning spawned at X" + packet.getX() + " Z" + packet.getZ());
            }
        }

        if (event.getPacket() instanceof SPacketEffect) {
            SPacketEffect packet = (SPacketEffect) event.getPacket();
            if (portal.getValue() && packet.getSoundType() == 1038) {
                sendNotification("[CoordLogger]: " + ChatFormatting.RED.toString() + "End Portal activated at X" + packet.getSoundPos().getX() + " Y" + packet.getSoundPos().getY() + " Z" + packet.getSoundPos().getZ());
            }
            if (wither.getValue() && packet.getSoundType() == 1023) {
                sendNotification("[CoordLogger]: " + ChatFormatting.RED.toString() + "Wither spawned at X" + packet.getSoundPos().getX() + " Y" + packet.getSoundPos().getY() + " Z" + packet.getSoundPos().getZ());
            }
            if (dragon.getValue() && packet.getSoundType() == 1028) {
                sendNotification("[CoordLogger]: " + ChatFormatting.RED.toString() + "Dragon killed at X" + packet.getSoundPos().getX() + " Y" + packet.getSoundPos().getY() + " Z" + packet.getSoundPos().getZ());
            }
        }

    });

    private HashMap<Entity, Vec3d> knownPlayers = new HashMap<>();

    @Override
    public void onUpdate() {

        if (!tp.getValue()) {
            return;
        }

        if (mc.player == null) {
            return;
        }

        List<Entity> tickEntityList = mc.world.getLoadedEntityList();

        for (Entity entity : tickEntityList) {
            if (entity instanceof EntityPlayer && !entity.getName().equals(mc.player.getName())) {
                Vec3d targetPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
                if (knownPlayers.containsKey(entity)) {
                    if (Math.abs(mc.player.getPositionVector().distanceTo(targetPos)) >= 128 && knownPlayers.get(entity).distanceTo(targetPos) >= 64) {
                        sendNotification("[CoordLogger]: " + ChatFormatting.RED.toString() + "Player " + entity.getName() + " moved to Position " + targetPos.toString());
                    }
                    knownPlayers.put(entity, targetPos);
                } else {
                    knownPlayers.put(entity, targetPos);
                }
            }
        }

    }

    private void sendNotification(String s) {
        Command.sendChatMessage(s);
        if (savetofile.getValue()) {
            appendTextFile(s, fileName);
        }
    }

}
