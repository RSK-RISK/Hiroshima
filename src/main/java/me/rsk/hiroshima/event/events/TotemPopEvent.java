package me.rsk.hiroshima.event.events;

import me.rsk.hiroshima.event.HiroshimaEvent;
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