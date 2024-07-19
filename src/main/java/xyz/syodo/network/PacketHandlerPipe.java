package xyz.syodo.network;

import lombok.Getter;
import lombok.SneakyThrows;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.common.PacketSignal;
import xyz.syodo.network.packet.PacketHandler;
import xyz.syodo.network.player.Player;
import xyz.syodo.utils.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PacketHandlerPipe implements BedrockPacketHandler {

    private List<PacketHandler> handlers = new ArrayList<>();
    @Getter
    private final Player player;

    public PacketHandlerPipe(BedrockSession session) {
        this.player = new Player(session);
        Arrays.stream(PacketHandlerRegistery.get().getPacketHandlers()).forEach(this::addPacketHandler);
    }

    private void addPacketHandler(Class packetHandlerClass) {
        try {
            Constructor<?> ctor = packetHandlerClass.getConstructor(PacketHandlerPipe.class);
            Object object = ctor.newInstance(new Object[] { this });
            if(object instanceof PacketHandler<?> packetHandler){
                handlers.add(packetHandler);
            } else throw new RuntimeException("Class is not a packethandler");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PacketSignal handlePacket(BedrockPacket packet) {
        Logger.info("Received " + packet.getClass().getSimpleName());
        Optional<PacketHandler> handler = handlers.stream().filter(f -> f.getType() == packet.getClass()).findFirst();
        if(handler.isPresent()) {
            return handler.get().handle(packet);
        } else {
            Logger.error("No PacketHandler for " + packet.toString());
            return PacketSignal.UNHANDLED;
        }
    }

}
