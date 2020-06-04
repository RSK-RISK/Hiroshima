package me.rsk.hiroshima.gasda.event.events;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.rsk.hiroshima.HiroshimaClient;
import me.rsk.hiroshima.gasda.event.HiroshimaEvent;
import me.rsk.hiroshima.gasda.util.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SPacketEntityStatus;

public class TotemPopEvent extends HiroshimaEvent {

    private Entity entity;

    public TotemPopEvent(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}