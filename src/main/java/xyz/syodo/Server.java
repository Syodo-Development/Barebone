package xyz.syodo;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueDatagramChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.NonNull;
import org.cloudburstmc.netty.channel.raknet.RakChannelFactory;
import org.cloudburstmc.netty.channel.raknet.RakServerChannel;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.protocol.bedrock.BedrockPeer;
import org.cloudburstmc.protocol.bedrock.BedrockPong;
import org.cloudburstmc.protocol.bedrock.BedrockServerSession;
import org.cloudburstmc.protocol.bedrock.codec.BedrockCodec;
import org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockServerInitializer;
import xyz.syodo.config.ServerConfiguration;
import xyz.syodo.network.PacketHandlerPipe;
import xyz.syodo.network.PacketHandlerRegistery;
import xyz.syodo.network.packet.LoginPacketHandler;
import xyz.syodo.network.packet.RequestNetworkSettingsPacketHandler;
import xyz.syodo.network.packet.ResourcePackClientResponsePacketHandler;
import xyz.syodo.utils.Logger;
import xyz.syodo.utils.ProtocolVersion;

import java.net.InetSocketAddress;

public class Server {

    // STATIC INSTANCES

    private static Server INSTANCE;

    public static void main(String[] args) {
        Server.get().start();
    }

    public static Server get() {
        if(INSTANCE == null) INSTANCE = new Server();
        return INSTANCE;
    }

    // BAREBONE SERVER

    @NonNull
    private InetSocketAddress bindAddress;
    private ServerBootstrap serverBootstrap;
    private RakServerChannel future;
    private final BedrockCodec CODEC = ProtocolVersion.latest().getCodec();

    public Server() {

        ServerConfiguration serverConfiguration = ServerConfiguration.get();

        this.bindAddress = new InetSocketAddress(serverConfiguration.getBindAdress(), serverConfiguration.getPort());

        var nettyThreadNumber = Math.min(serverConfiguration.getProcessorCount(), Runtime.getRuntime().availableProcessors());
        var threadFactory = new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").build();
        Class<? extends DatagramChannel> datagramChannelClass;
        EventLoopGroup eventLoopGroup;
        if (Epoll.isAvailable()) {
            datagramChannelClass = EpollDatagramChannel.class;
            eventLoopGroup = new EpollEventLoopGroup(nettyThreadNumber, threadFactory);
        } else if (KQueue.isAvailable()) {
            datagramChannelClass = KQueueDatagramChannel.class;
            eventLoopGroup = new KQueueEventLoopGroup(nettyThreadNumber, threadFactory);
        } else {
            datagramChannelClass = NioDatagramChannel.class;
            eventLoopGroup = new NioEventLoopGroup(nettyThreadNumber, threadFactory);
        }

        PacketHandlerRegistery packetHandlerRegistery = PacketHandlerRegistery.get();
        packetHandlerRegistery.addPacketHandler(RequestNetworkSettingsPacketHandler.class);
        packetHandlerRegistery.addPacketHandler(LoginPacketHandler.class);
        packetHandlerRegistery.addPacketHandler(ResourcePackClientResponsePacketHandler.class);

        serverBootstrap = new ServerBootstrap()
             .channelFactory(RakChannelFactory.server(datagramChannelClass))
             .option(RakChannelOption.RAK_ADVERTISEMENT,
                 new BedrockPong()
                     .edition("MCPE")
                     .motd(serverConfiguration.getMotd())
                     .subMotd("")
                     .serverId(1)
                     .nintendoLimited(false)
                     .playerCount(0)
                     .maximumPlayerCount(serverConfiguration.getMaxPlayers())
                     .gameType("Survival")
                     .ipv4Port(serverConfiguration.getPort())
                     .ipv6Port(serverConfiguration.getPort())
                     .protocolVersion(CODEC.getProtocolVersion()).toByteBuf())
                .group(eventLoopGroup)
                .childHandler(new BedrockServerInitializer() {

                    @Override
                    protected void initSession(BedrockServerSession session) {
                    }

                    public BedrockServerSession createSession0(BedrockPeer peer, int subClientId) {
                        BedrockServerSession session = new BedrockServerSession(peer, subClientId);
                        session.setLogging(true);
                        session.setPacketHandler(new PacketHandlerPipe(session));
                        return session;
         }});
    }


    public boolean start() {
        if(future == null) {
            Logger.success("Starting Barebone Instance on port " + bindAddress.getPort());
            future = (RakServerChannel) serverBootstrap.bind(bindAddress).syncUninterruptibly().channel();
            return true;
        } else return false;
    }

}
