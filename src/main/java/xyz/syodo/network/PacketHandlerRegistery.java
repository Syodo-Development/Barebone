package xyz.syodo.network;

import lombok.Getter;
import xyz.syodo.network.packet.PacketHandler;
import xyz.syodo.utils.Registery;

import java.util.ArrayList;
import java.util.List;

public class PacketHandlerRegistery implements Registery {

    private static PacketHandlerRegistery INSTANCE;

    public static PacketHandlerRegistery get() {
        if(INSTANCE == null) INSTANCE = new PacketHandlerRegistery();
        return INSTANCE;
    }

    private final List<Class> packetHandlers = new ArrayList<>();;

    public Class[] getPacketHandlers() {
        return packetHandlers.toArray(new Class[packetHandlers.size()]);
    }

    public boolean addPacketHandler(Class packetHandler) {
        if(packetHandler.getSuperclass() == PacketHandler.class){
            if(!packetHandlers.stream().anyMatch(opackethandler -> opackethandler == packetHandler)){
                packetHandlers.add(packetHandler);
                return true;
            } else throw new RuntimeException("PacketHandler already registered (" + packetHandler.getName() + ")");
        } else throw new RuntimeException("Class is not a subclass of PacketHandler (" + packetHandler.getName() + ")");
    }
    
}
