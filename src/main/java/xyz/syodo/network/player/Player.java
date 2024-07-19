package xyz.syodo.network.player;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.data.*;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import xyz.syodo.config.ServerConfiguration;
import xyz.syodo.utils.Logger;
import xyz.syodo.utils.PaletteManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class Player {

    @Getter
    protected final BedrockSession session;

    public void sendPacket(BedrockPacket packet) {
        Logger.info("Sending " + packet.getClass().getSimpleName());
        session.sendPacket(packet);
    }

    public void sendPacketImmediately(BedrockPacket packet) {
        Logger.info("Sending " + packet.getClass().getSimpleName());
        session.sendPacketImmediately(packet);
    }

}
