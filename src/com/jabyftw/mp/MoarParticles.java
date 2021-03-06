package com.jabyftw.mp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Rafael
 */
public class MoarParticles extends JavaPlugin implements Listener {

    public FileConfiguration config;
    public Random r = new Random();
    public boolean entityDamage, playerDamage, fallDamage, teleport;
    public int smoke, breake, tp, fire;
    public List<String> colors = new ArrayList();
    public Map<Player, Integer> tpCd = new HashMap();
    public EffectExecutor FxExec;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        String[] li = {"57", "35", "22", "152", "133", "79", "51"};
        colors.addAll(Arrays.asList(li));
        config = getConfig();
        config.addDefault("config.listener.entityDamage", true);
        config.addDefault("config.listener.playerDamage", true);
        config.addDefault("config.listener.fallDamage", true);
        config.addDefault("config.listener.teleport", true);
        config.addDefault("config.command.effectDelayInTicks", 4);
        config.addDefault("config.command.effectDurationInSec", 60);
        config.addDefault("config.effect.smokeQuantity", 16);
        config.addDefault("config.effect.breakQuantity", 4);
        config.addDefault("config.effect.teleportQuantity", 4);
        config.addDefault("config.effect.fireQuantity", 4);
        //config.addDefault("lang.", "&");
        config.addDefault("lang.noPermission", "&cNo permission!");
        config.addDefault("lang.changedValue", "&6Changed value of &e%value&6 to &e%to&6.");
        config.addDefault("lang.alreadyOnEffect", "&cAlready running effect on you.");
        config.options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        smoke = config.getInt("config.effect.smokeQuantity");
        breake = config.getInt("config.effect.breakQuantity");
        tp = config.getInt("config.effect.teleportQuantity");
        fire = config.getInt("config.effect.fireQuantity");
        entityDamage = config.getBoolean("config.listener.entityDamage");
        playerDamage = config.getBoolean("config.listener.playerDamage");
        fallDamage = config.getBoolean("config.listener.fallDamage");
        teleport = config.getBoolean("config.listener.teleport");
        FxExec = new EffectExecutor(this, config.getInt("config.command.effectDelayInTicks"), config.getInt("config.command.effectDurationInSec"));
        getServer().getPluginCommand("effect").setExecutor(FxExec);
        getServer().getPluginCommand("particles").setExecutor(new ParticlesExecutor(this));
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().log(Level.INFO, "Enabled in {0}ms", (System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled.");
    }

    public String getLang(String path) {
        return config.getString("lang." + path).replaceAll("&", "§");
    }

    public void effectSmoke(int quantity, Location loc) {
        for (int i = 0; i <= quantity; i++) {
            loc.getWorld().playEffect(loc, Effect.SMOKE, r.nextInt(8));
        }
    }

    public void effectBreak(int quantity, Location loc, int id) {
        for (int i = 0; i <= quantity; i++) {
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, id);
        }
    }

    public void effectFire(int quantity, Location loc) {
        for (int i = 0; i <= quantity; i++) {
            loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0);
        }
    }

    public void effectTp(int quantity, Location loc) {
        loc.getWorld().playSound(loc, Sound.PORTAL_TRIGGER, 1, 0);
        for (int i = 0; i <= quantity; i++) {
            loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 0);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getCause().equals(DamageCause.FALL)) {
                if (fallDamage) {
                    if (p.getNoDamageTicks() < (p.getMaximumNoDamageTicks() / 2)) {
                        effectSmoke(smoke, p.getLocation());
                    }
                }
            } else {
                if (playerDamage) {
                    if (p.getNoDamageTicks() < (p.getMaximumNoDamageTicks() / 2)) {
                        effectBreak(breake, e.getEntity().getLocation(), 11);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (playerDamage) {
                if (p.getNoDamageTicks() < (p.getMaximumNoDamageTicks() / 2)) {
                    effectBreak(breake, p.getLocation(), 11);
                }
            }
        } else {
            if (entityDamage) {
                if (e.getEntity() instanceof LivingEntity) {
                    if (((LivingEntity) e.getEntity()).getNoDamageTicks() < (((LivingEntity) e.getEntity()).getMaximumNoDamageTicks() / 2)) {
                        effectBreak(breake, e.getEntity().getLocation(), 55);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onTp(PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        if (teleport) {
            if (!e.getCause().equals(TeleportCause.PLUGIN)) {
                if (!tpCd.containsKey(p)) {
                    tpCd.put(p, getServer().getScheduler().scheduleSyncDelayedTask(this, new BukkitRunnable() {

                        @Override
                        public void run() {
                            tpCd.remove(p);
                        }
                    }, 20 * 3));
                    getServer().getScheduler().scheduleSyncDelayedTask(this, new BukkitRunnable() {

                        @Override
                        public void run() {
                            effectTp(tp, p.getLocation());
                        }
                    }, 20);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        checkExit(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        checkExit(e.getPlayer());
    }

    private void checkExit(Player p) {
        if (FxExec.effects.containsKey(p)) {
            getServer().getScheduler().cancelTask(FxExec.effects.get(p));
            FxExec.effects.remove(p);
        }
        if (tpCd.containsKey(p)) {
            getServer().getScheduler().cancelTask(tpCd.get(p));
            tpCd.remove(p);
        }
    }
}
