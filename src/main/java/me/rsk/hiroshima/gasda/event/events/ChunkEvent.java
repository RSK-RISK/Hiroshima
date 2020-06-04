package me.rsk.hiroshima.gasda.event.events;

import me.rsk.hiroshima.gasda.event.HiroshimaEvent;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;

/**
 * @author 086
 */
public class ChunkEvent extends HiroshimaEvent {
    private Chunk chunk;
    private SPacketChunkData packet;

    public ChunkEvent(Chunk chunk, SPacketChunkData packet) {
        this.chunk = chunk;
        this.packet = packet;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public SPacketChunkData getPacket() {
        return packet;
    }
}
