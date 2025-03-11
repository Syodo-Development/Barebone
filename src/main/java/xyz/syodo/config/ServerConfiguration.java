package xyz.syodo.config;

import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.protocol.bedrock.BedrockPong;
import xyz.syodo.utils.ProtocolVersion;

@Getter
@Setter
public class ServerConfiguration implements Configuration {

    private static ServerConfiguration CONFIG;

    public static ServerConfiguration get() {
        if(CONFIG == null) CONFIG = new ServerConfiguration();
        return CONFIG;
    }

    private String bindAdress = "0.0.0.0";
    private Integer port = 19132;

    private Integer processorCount = 4;
    private boolean plugins = true;

    private String edition = "MCPE";
    private String motd = "Barebone Bedrock Server";
    private String subMotd = "";
    private int serverId = 1;
    private boolean nintendoLimited = false;
    private int playerCount = 0;
    private Integer maxPlayers = 1;
    private String gameType = "Survival";
    private String[] extras = new String[0];

    public BedrockPong buildPong() {
        return new BedrockPong()
                .edition(this.edition)
                .motd(this.motd)
                .subMotd(this.subMotd)
                .serverId(this.serverId)
                .nintendoLimited(this.nintendoLimited)
                .playerCount(this.playerCount)
                .maximumPlayerCount(this.maxPlayers)
                .gameType(this.gameType)
                .ipv4Port(port)
                .ipv6Port(port)
                .version(ProtocolVersion.latest().getMinecraftVersion())
                .protocolVersion(ProtocolVersion.latest().getCodec().getProtocolVersion())
                .extras(extras);

    }

}
