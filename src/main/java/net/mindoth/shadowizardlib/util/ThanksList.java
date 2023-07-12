package net.mindoth.shadowizardlib.util;

import net.mindoth.shadowizardlib.network.ShadowizardNetwork;
import net.mindoth.shadowizardlib.network.SupporterDisableMessage;
import net.mindoth.shadowizardlib.registries.KeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

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
        END_ROD(() -> ParticleTypes.END_ROD),
        FIRE(() -> ParticleTypes.FLAME),
        FIREWORK(() -> ParticleTypes.FIREWORK),
        GROWTH(() -> ParticleTypes.HAPPY_VILLAGER),
        HEART(() -> ParticleTypes.HEART),
        SLIME(() -> ParticleTypes.ITEM_SLIME),
        SNOW(() -> ParticleTypes.ITEM_SNOWBALL),
        SOUL(() -> ParticleTypes.SOUL),
        SOUL_FIRE(() -> ParticleTypes.SOUL_FIRE_FLAME),
        WITCH(() -> ParticleTypes.WITCH);

        public final Supplier<IParticleData> type;

        SupporterParticleType(Supplier<IParticleData> type) {
            this.type = type;
        }
    }

    public static void init() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Mindoth/shadowizardlib/main/thanks.txt");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String s;
                    while ((s = reader.readLine()) != null) {
                        String[] split = s.split(" ", 2);
                        if (split.length != 2) {
                            continue;
                        }
                        PARTICLES.put(UUID.fromString(split[0]), SupporterParticleType.valueOf(split[1]));
                    }
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            catch (Exception k) {
                //not possible
            }
            if (PARTICLES.size() > 0) MinecraftForge.EVENT_BUS.addListener(ThanksList::clientTick);
        }, "ShadowizardLib Supporter Effect Loader").start();
    }

    public static void clientTick(TickEvent.ClientTickEvent event) {
        if ( KeyBinds.TOGGLE.consumeClick() ) ShadowizardNetwork.CHANNEL.sendToServer(new SupporterDisableMessage(0));
        SupporterParticleType t = null;
        if ( event.phase == TickEvent.Phase.END && Minecraft.getInstance().level != null ) {
            for ( PlayerEntity player : Minecraft.getInstance().level.players() ) {
                if ( !player.isInvisible() && player.tickCount * 3 % 2 == 0 && !DISABLED.contains(player.getUUID()) && (t = PARTICLES.get(player.getUUID())) != null ) {
                    ClientWorld world = (ClientWorld) player.level;
                    Random rand = world.random;
                    IParticleData type = t.type.get();
                    world.addParticle(type, player.getX() + rand.nextDouble() * 0.4 - 0.2, player.getY() + 0.1, player.getZ() + rand.nextDouble() * 0.4 - 0.2, 0, 0, 0);
                }
            }
        }
    }
}
