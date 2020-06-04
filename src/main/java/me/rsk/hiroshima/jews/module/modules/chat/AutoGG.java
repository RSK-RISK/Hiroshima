package me.rsk.hiroshima.jews.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.gasda.command.Command;
import me.rsk.hiroshima.gasda.event.events.PacketEvent;
import me.rsk.hiroshima.jews.module.Module;
import me.rsk.hiroshima.jews.module.ModuleManager;
import me.rsk.hiroshima.gasda.setting.Setting;
import me.rsk.hiroshima.gasda.setting.Settings;
import me.rsk.hiroshima.gasda.util.EntityUtil;
import me.rsk.hiroshima.gasda.util.Friends;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created 15 November 2019 by hub
 * Updated 24 November 2019 by hub
 * Added by Darki
 * Updated by Hamburger on 12/01/2020
 */


@Module.Info(name = "AutoGG", description = "Posts a message when you kill a player", category = Module.Category.CHAT)
public class AutoGG extends Module {

    private ConcurrentHashMap<String, Integer> targetedPlayers = null;

    public static Setting<Mode> mode = Settings.e("Mode", Mode.NUKED);

    private final String CLEAN1 = "Good Fight ";
    private final String CLEAN2 = " Hiroshima On Top!";
    private final String NUKED = " You Just Got Nuked By Hiroshima Client!";
    private final String EZ = "EZZZ ";

    public enum Mode {
        CLEAN, NUKED, EZ, RANDOM
    }

    public AutoGG() {
        register(mode);
    }
    private Setting<Integer> timeoutTicks = register(Settings.i("TimeoutTicks", 20));

    @Override
    public void onEnable() {
        targetedPlayers = new ConcurrentHashMap<>();
    }

    @Override
    public void onDisable() {
        targetedPlayers = null;
    }

    @Override
    public void onUpdate() {

        if (isDisabled() || mc.player == null) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        for (Entity entity : mc.world.getLoadedEntityList()) {

            // skip non player entities
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            EntityPlayer player = (EntityPlayer) entity;

            // skip if player is alive
            if (player.getHealth() > 0) {
                continue;
            }

            if (player.getArmorInventoryList().equals(null)) {
                return;
            }

            String name = player.getName();
            if (shouldAnnounce(name)) {
                doAnnounce(name);
                break;
            }

        }

        targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                targetedPlayers.remove(name);
            } else {
                targetedPlayers.put(name, timeout - 1);
            }
        });

    }

    @EventHandler
    public Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {

        if (mc.player == null) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        // return if packet is not of type CPacketUseEntity
        if (!(event.getPacket() instanceof CPacketUseEntity)) {
            return;
        }
        CPacketUseEntity cPacketUseEntity = ((CPacketUseEntity) event.getPacket());

        // return if action is not of type CPacketUseEntity.Action.ATTACK
        if (!(cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK))) {
            return;
        }

        // return if targeted Entity is not a player
        Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
        if (!EntityUtil.isPlayer(targetEntity)) {
            return;
        }

        addTargetedPlayer(targetEntity.getName());

    });

    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener = new Listener<>(event -> {

        if (mc.player == null) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        EntityLivingBase entity = event.getEntityLiving();

        if (entity == null) {
            return;
        }

        // skip non player entities
        if (!EntityUtil.isPlayer(entity)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        // skip if player is alive
        if (player.getHealth() > 0) {
            return;
        }

        String name = player.getName();
        if (shouldAnnounce(name)) {
            doAnnounce(name);
        }

    });

    private boolean shouldAnnounce(String name) {
        return targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {

        targetedPlayers.remove(name);

        //TODO rework this mess
        switch (mode.getValue()) {
            case CLEAN:
                postGG(CLEAN1 + name + CLEAN2);
                break;
            case EZ:
                postGG(EZ + name);
                break;
            case NUKED:
                postGG(name + NUKED);
                break;
            case RANDOM:
                String[] gglist = {CLEAN1, EZ, NUKED};
                Random random = new Random();
                String randomgg = gglist[random.nextInt(gglist.length)];
                if (randomgg == CLEAN1) {
                    randomgg = CLEAN1 + name + CLEAN2;
                }
                postGG(randomgg);
                break;

        }
    }

    public void postGG(String text) {
        mc.player.connection.sendPacket(new CPacketChatMessage(text));
    }

    public void addTargetedPlayer(String name) {

        // skip if self is the target
        if (Objects.equals(name, mc.player.getName())) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        targetedPlayers.put(name, timeoutTicks.getValue());

    }

}
