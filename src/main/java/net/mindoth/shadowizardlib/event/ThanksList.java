package net.mindoth.shadowizardlib.event;

import net.mindoth.shadowizardlib.ShadowizardLibClient;
import net.mindoth.shadowizardlib.network.SyncClientEffectsPacket;
import net.mindoth.shadowizardlib.network.ToggleClientEffectsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

public class ThanksList {

    static Map<UUID, SupporterParticleType> PARTICLES = new HashMap<>();
    public static final Set<UUID> DISABLED = new HashSet<>();

    public enum SupporterParticleType {
        ASH(() -> ParticleTypes.ASH),
        CAMPFIRE_SMOKE(() -> ParticleTypes.CAMPFIRE_COSY_SMOKE),
        CLOUD(() -> ParticleTypes.CLOUD),
        DMG_HEART(() -> ParticleTypes.DAMAGE_INDICATOR),
        DRAGON_BREATH(() -> ParticleTypes.DRAGON_BREATH),
        ELECTRIC_SPARK(() -> ParticleTypes.ELECTRIC_SPARK),
        END_ROD(() -> ParticleTypes.END_ROD),
        FIRE(() -> ParticleTypes.FLAME),
        FIREWORK(() -> ParticleTypes.FIREWORK),
        GLOW(() -> ParticleTypes.GLOW),
        GROWTH(() -> ParticleTypes.HAPPY_VILLAGER),
        HEART(() -> ParticleTypes.HEART),
        SCULK_SOUL(() -> ParticleTypes.SCULK_SOUL),
        SLIME(() -> ParticleTypes.ITEM_SLIME),
        SNEEZE(() -> ParticleTypes.SNEEZE),
        SNOW(() -> ParticleTypes.ITEM_SNOWBALL),
        SOUL(() -> ParticleTypes.SOUL),
        SOUL_FIRE(() -> ParticleTypes.SOUL_FIRE_FLAME),
        WITCH(() -> ParticleTypes.WITCH);

        public final Supplier<ParticleOptions> type;

        SupporterParticleType(Supplier<ParticleOptions> type) {
            this.type = type;
        }
    }

    public static void init() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Mindoth/shadowizardlib/main/thanks.txt");
                try ( BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())) ) {
                    String s;
                    while ( (s = reader.readLine()) != null ) {
                        String[] split = s.split(" ", 2);
                        if (split.length != 2) {
                            continue;
                        }
                        PARTICLES.put(UUID.fromString(split[0]), SupporterParticleType.valueOf(split[1]));
                    }
                }
                catch ( IOException exception ) {
                    exception.printStackTrace();
                }
            }
            catch ( Exception k ) {
                //Not possible
            }
            if ( !PARTICLES.isEmpty() ) {
                NeoForge.EVENT_BUS.addListener(ThanksList::clientTick);
                NeoForge.EVENT_BUS.addListener(ThanksList::onClientJoin);
            }
        }, "ShadowizardLib Supporter Effect Loader").start();
    }

    public static void onClientJoin(PlayerEvent.PlayerLoggedInEvent event) {
        UUID id = event.getEntity().getUUID();
        if ( PARTICLES.get(id) != null ) PacketDistributor.sendToAllPlayers(new SyncClientEffectsPacket(0, id));
    }

    public static void clientTick(ClientTickEvent.Post event) {
        if ( ShadowizardLibClient.ClientModBusEvents.TOGGLE.consumeClick() ) PacketDistributor.sendToServer(new ToggleClientEffectsPacket());
        SupporterParticleType t = null;
        if ( Minecraft.getInstance().level != null ) {
            for ( Player player : Minecraft.getInstance().level.players() ) {
                if ( !player.isInvisible() && player.tickCount * 3 % 2 == 0 && !DISABLED.contains(player.getUUID()) && (t = PARTICLES.get(player.getUUID())) != null ) {
                    ClientLevel world = (ClientLevel)player.level();
                    RandomSource rand = world.random;
                    ParticleOptions type = t.type.get();
                    world.addParticle(type, player.getX() + rand.nextDouble() * 0.4 - 0.2, player.getY() + 0.1, player.getZ() + rand.nextDouble() * 0.4 - 0.2, 0, 0, 0);
                }
            }
        }
    }
}
