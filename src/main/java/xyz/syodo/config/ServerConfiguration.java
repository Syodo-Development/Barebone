package xyz.syodo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

    private String motd = "Barebone Bedrock Server";
    private Integer maxPlayers = 1;

}
