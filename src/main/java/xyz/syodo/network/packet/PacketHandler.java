package xyz.syodo.network.packet;

import lombok.RequiredArgsConstructor;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.common.PacketSignal;
import xyz.syodo.network.PacketHandlerPipe;

import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public abstract class PacketHandler<E extends BedrockPacket> {

    protected final PacketHandlerPipe HANDLER;

    public abstract PacketSignal handle(E packet);

    public Class getType() {
        return (Class<BedrockPacket>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
