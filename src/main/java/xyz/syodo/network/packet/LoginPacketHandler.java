package xyz.syodo.network.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;

import xyz.syodo.network.PacketHandlerPipe;
import java.util.UUID;

public class LoginPacketHandler extends PacketHandler<LoginPacket>{

    public LoginPacketHandler(PacketHandlerPipe HANDLER) {
        super(HANDLER);
    }

    @Override
    public PacketSignal handle(LoginPacket packet) {

        PlayStatusPacket status = new PlayStatusPacket();
        status.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        HANDLER.getPlayer().sendPacket(status);

        SetEntityMotionPacket motion = new SetEntityMotionPacket();
        motion.setRuntimeEntityId(1);
        motion.setMotion(Vector3f.ZERO);
        HANDLER.getPlayer().sendPacket(motion);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        resourcePacksInfo.setForcedToAccept(false);
        resourcePacksInfo.setScriptingEnabled(false);
        resourcePacksInfo.setHasAddonPacks(false);
        resourcePacksInfo.setWorldTemplateVersion("");
        resourcePacksInfo.setWorldTemplateId(UUID.randomUUID());
        HANDLER.getPlayer().sendPacket(resourcePacksInfo);

        return PacketSignal.HANDLED;
    }
}
