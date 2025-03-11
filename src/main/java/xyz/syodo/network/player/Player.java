package xyz.syodo.network.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.packet.*;
import xyz.syodo.network.PacketHandlerPipe;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class Player {

    protected final BedrockSession session;
    protected final PacketHandlerPipe packetHandlerPipe;
    protected final Map<String, Object> meta = new HashMap<>();

    public void sendPacket(BedrockPacket packet) {
        session.sendPacket(packet);
    }

    public void sendPacketImmediately(BedrockPacket packet) {
        session.sendPacketImmediately(packet);
    }

}
