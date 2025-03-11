package xyz.syodo.network.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.packet.*;

@RequiredArgsConstructor
public class Player {

    @Getter
    protected final BedrockSession session;

    public void sendPacket(BedrockPacket packet) {
        session.sendPacket(packet);
    }

    public void sendPacketImmediately(BedrockPacket packet) {
        session.sendPacketImmediately(packet);
    }

}
