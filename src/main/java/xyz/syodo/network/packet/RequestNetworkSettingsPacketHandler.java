package xyz.syodo.network.packet;

import org.cloudburstmc.protocol.bedrock.data.*;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;
import xyz.syodo.network.PacketHandlerPipe;
import xyz.syodo.utils.ProtocolVersion;
import xyz.syodo.utils.Logger;

public class RequestNetworkSettingsPacketHandler extends PacketHandler<RequestNetworkSettingsPacket>{

    public RequestNetworkSettingsPacketHandler(PacketHandlerPipe HANDLER) {
        super(HANDLER);
    }

    @Override
    public PacketSignal handle(RequestNetworkSettingsPacket packet) {

        ProtocolVersion version = ProtocolVersion.get(packet.getProtocolVersion());
        Logger.info("Player connected using " + version.getMinecraftVersion() + " (" + version.getProtocol() + ")");
        HANDLER.getPlayer().getSession().setCodec(version.getCodec());

        NetworkSettingsPacket settingsPacket = new NetworkSettingsPacket();
        settingsPacket.setCompressionThreshold(1);
        settingsPacket.setCompressionAlgorithm(PacketCompressionAlgorithm.SNAPPY);
        HANDLER.getPlayer().sendPacketImmediately(settingsPacket);
        HANDLER.getPlayer().getSession().getPeer().setCompression(PacketCompressionAlgorithm.SNAPPY);

        return PacketSignal.HANDLED;
    }
}
