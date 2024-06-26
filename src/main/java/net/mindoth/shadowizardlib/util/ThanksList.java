package net.mindoth.shadowizardlib.util;

import net.mindoth.shadowizardlib.network.PacketDistro;
import net.mindoth.shadowizardlib.network.ShadowizardNetwork;
import net.mindoth.shadowizardlib.network.SupporterDisableMessage;
import net.mindoth.shadowizardlib.registries.KeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

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
                //not possible
            }
            if ( PARTICLES.size() > 0 ) {
                MinecraftForge.EVENT_BUS.addListener(ThanksList::clientTick);
                MinecraftForge.EVENT_BUS.addListener(ThanksList::onClientJoin);
            }
        }, "ShadowizardLib Supporter Effect Loader").start();
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if ( KeyBinds.TOGGLE.consumeClick() ) ShadowizardNetwork.CHANNEL.sendToServer(new SupporterDisableMessage(0));
        SupporterParticleType t = null;
        if ( event.phase == TickEvent.Phase.END && Minecraft.getInstance().level != null ) {
            for ( Player player : Minecraft.getInstance().level.players() ) {
                if ( !player.isInvisible() && player.tickCount * 3 % 2 == 0 && !DISABLED.contains(player.getUUID()) && (t = PARTICLES.get(player.getUUID())) != null ) {
                    ClientLevel world = (ClientLevel) player.level();
                    RandomSource rand = world.random;
                    ParticleOptions type = t.type.get();
                    world.addParticle(type, player.getX() + rand.nextDouble() * 0.4 - 0.2, player.getY() + 0.1, player.getZ() + rand.nextDouble() * 0.4 - 0.2, 0, 0, 0);
                }
            }
        }
    }

    public static void onClientJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if ( (PARTICLES.get(event.getEntity().getUUID())) != null ) {
            DISABLED.remove(event.getEntity().getUUID());
            PacketDistro.sendTo(ShadowizardNetwork.CHANNEL, new SupporterDisableMessage(1, event.getEntity().getUUID()), event.getEntity());
            PacketDistro.sendToAll(ShadowizardNetwork.CHANNEL, new SupporterDisableMessage(1, event.getEntity().getUUID()));
        }
    }
}
