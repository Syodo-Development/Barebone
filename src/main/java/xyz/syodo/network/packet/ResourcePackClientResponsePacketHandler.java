package xyz.syodo.network.packet;

import io.netty.buffer.Unpooled;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.cloudburstmc.protocol.bedrock.data.*;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.packet.*;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.cloudburstmc.protocol.common.util.OptionalBoolean;
import xyz.syodo.config.ServerConfiguration;
import xyz.syodo.network.PacketHandlerPipe;
import xyz.syodo.utils.PaletteManager;
import xyz.syodo.utils.ProtocolVersion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ResourcePackClientResponsePacketHandler extends PacketHandler<ResourcePackClientResponsePacket>{

    public ResourcePackClientResponsePacketHandler(PacketHandlerPipe HANDLER) {
        super(HANDLER);
    }

    private static final NbtMap EMPTY_TAG = NbtMap.EMPTY;
    private static final byte[] EMPTY_LEVEL_CHUNK_DATA;

    static {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(new byte[258]); // Biomes + Border Size + Extra Data Size

            try (NBTOutputStream stream = NbtUtils.createNetworkWriter(outputStream)) {
                stream.writeTag(EMPTY_TAG);
            }

            EMPTY_LEVEL_CHUNK_DATA = outputStream.toByteArray();
        }catch (IOException e) {
            throw new AssertionError("Unable to generate empty level chunk data");
        }
    }

    @Override
    public PacketSignal handle(ResourcePackClientResponsePacket packet) {

        switch (packet.getStatus()) {
            case COMPLETED:
                StartGamePacket startGamePacket = new StartGamePacket();
                startGamePacket.setUniqueEntityId(1);
                startGamePacket.setRuntimeEntityId(1);
                startGamePacket.setPlayerGameType(GameType.CREATIVE);
                startGamePacket.setPlayerPosition(Vector3f.from(0, 0, 0));
                startGamePacket.setRotation(Vector2f.from(1, 1));
                startGamePacket.setServerId("");
                startGamePacket.setWorldId("");
                startGamePacket.setScenarioId("");
                startGamePacket.setSeed(-1);
                startGamePacket.setDimensionId(0);
                startGamePacket.setGeneratorId(1);
                startGamePacket.setLevelGameType(GameType.CREATIVE);
                startGamePacket.setDifficulty(1);
                startGamePacket.setDefaultSpawn(Vector3i.ZERO);
                startGamePacket.setAchievementsDisabled(false);
                startGamePacket.setCurrentTick(-1);
                startGamePacket.setEduEditionOffers(0);
                startGamePacket.setEduFeaturesEnabled(false);
                startGamePacket.setRainLevel(0);
                startGamePacket.setLightningLevel(0);
                startGamePacket.setMultiplayerGame(true);
                startGamePacket.setBroadcastingToLan(true);
                startGamePacket.getGamerules().add((new GameRuleData<>("showcoordinates", false)));
                startGamePacket.setPlatformBroadcastMode(GamePublishSetting.PUBLIC);
                startGamePacket.setXblBroadcastMode(GamePublishSetting.PUBLIC);
                startGamePacket.setCommandsEnabled(false);
                startGamePacket.setTexturePacksRequired(true);
                startGamePacket.setBonusChestEnabled(false);
                startGamePacket.setStartingWithMap(false);
                startGamePacket.setTrustingPlayers(true);
                startGamePacket.setDefaultPlayerPermission(PlayerPermission.VISITOR);
                startGamePacket.setServerChunkTickRange(4);
                startGamePacket.setBehaviorPackLocked(false);
                startGamePacket.setResourcePackLocked(true);
                startGamePacket.setFromLockedWorldTemplate(false);
                startGamePacket.setUsingMsaGamertagsOnly(false);
                startGamePacket.setFromWorldTemplate(false);
                startGamePacket.setWorldTemplateOptionLocked(false);
                startGamePacket.setSpawnBiomeType(SpawnBiomeType.DEFAULT);
                startGamePacket.setCustomBiomeName("");
                startGamePacket.setEducationProductionId("");
                startGamePacket.setForceExperimentalGameplay(OptionalBoolean.empty());
                startGamePacket.setAuthoritativeMovementMode(AuthoritativeMovementMode.CLIENT);
                startGamePacket.setRewindHistorySize(0);
                startGamePacket.setServerAuthoritativeBlockBreaking(false);
                startGamePacket.setVanillaVersion("*");
                startGamePacket.setInventoriesServerAuthoritative(true);
                startGamePacket.setServerEngine("Barebone");
                startGamePacket.setLevelId(ServerConfiguration.get().getMotd());
                startGamePacket.setLevelName(ServerConfiguration.get().getMotd());
                startGamePacket.setPremiumWorldTemplateId("00000000-0000-0000-0000-000000000000");
                startGamePacket.setCurrentTick(0);
                startGamePacket.setEnchantmentSeed(0);
                startGamePacket.setMultiplayerCorrelationId("");
                startGamePacket.setServerEngine("");
                startGamePacket.setPlayerPropertyData(NbtMap.EMPTY);
                startGamePacket.setWorldTemplateId(UUID.randomUUID());

                startGamePacket.setChatRestrictionLevel(ChatRestrictionLevel.NONE);

                startGamePacket.setBlockPalette(PaletteManager.get().CACHED_PALLETE);

                HANDLER.getPlayer().sendPacket(startGamePacket);

                CreativeContentPacket creativeContentPacket = new CreativeContentPacket();
                creativeContentPacket.setContents(new ItemData[0]);
                HANDLER.getPlayer().sendPacket(creativeContentPacket);

                Vector3f pos = Vector3f.ZERO;
                int chunkX = pos.getFloorX() >> 4;
                int chunkZ = pos.getFloorX() >> 4;

                for (int x = -3; x < 3; x++) {
                    for (int z = -3; z < 3; z++) {
                        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
                        levelChunkPacket.setChunkX(chunkX + x);
                        levelChunkPacket.setChunkZ(chunkZ + z);
                        levelChunkPacket.setSubChunksLength(0);
                        levelChunkPacket.setData(Unpooled.wrappedBuffer(EMPTY_LEVEL_CHUNK_DATA));
                        HANDLER.getPlayer().sendPacket(levelChunkPacket);
                    }
                }

                BiomeDefinitionListPacket biomeDefinitionListPacket = new BiomeDefinitionListPacket();
                biomeDefinitionListPacket.setDefinitions(PaletteManager.get().BIOMES);
                HANDLER.getPlayer().sendPacket(biomeDefinitionListPacket);

                AvailableEntityIdentifiersPacket availableEntityIdentifiersPacket = new AvailableEntityIdentifiersPacket();
                availableEntityIdentifiersPacket.setIdentifiers(PaletteManager.get().ENTITY_IDENTIFIERS);
                HANDLER.getPlayer().sendPacket(availableEntityIdentifiersPacket);

                PlayStatusPacket playStatusPacket = new PlayStatusPacket();
                playStatusPacket.setStatus(PlayStatusPacket.Status.PLAYER_SPAWN);
                HANDLER.getPlayer().sendPacket(playStatusPacket);

                UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
                updateAttributesPacket.setRuntimeEntityId(0);
                List<AttributeData> attributes = new ArrayList<>();
                attributes.add(new AttributeData("minecraft:movement", 0.0f, 1024f, 0.1f, 0.1f));
                updateAttributesPacket.setAttributes(attributes);
                HANDLER.getPlayer().sendPacket(updateAttributesPacket);
                break;
            case HAVE_ALL_PACKS:
                ResourcePackStackPacket resourcePackStackPacket = new ResourcePackStackPacket();
                resourcePackStackPacket.setForcedToAccept(false);
                resourcePackStackPacket.setGameVersion("*");
                HANDLER.getPlayer().sendPacket(resourcePackStackPacket);
                break;
            default:
                HANDLER.getPlayer().getSession().disconnect("disconnectionScreen.resourcePack");
                break;
        }

        return PacketSignal.HANDLED;
    }
}
