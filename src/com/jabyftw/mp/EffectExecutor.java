package com.jabyftw.mp;

import com.jabyftw.mp.constant.ColoredGround;
import com.jabyftw.mp.constant.FireEffect;
import com.jabyftw.mp.constant.SmokeEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Rafael
 */
public class EffectExecutor implements CommandExecutor {

    private final MoarParticles pl;
    private final int delay, duration;
    public Map<Player, Integer> effects = new HashMap();

    public EffectExecutor(MoarParticles pl, int delay, int duration) {
        this.pl = pl;
        this.delay = delay;
        this.duration = duration;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 1) {
                return false;
            } else {
                Player p = (Player) sender;
                if (effects.containsKey(p)) {
                    sender.sendMessage(pl.getLang("alreadyOnEffect"));
                    return true;
                } else {
                    BukkitScheduler bs = pl.getServer().getScheduler();
                    if (args.length == 1) { // /effect fire/smoke/colored
                        if (args[0].startsWith("f")) {
                            if (sender.hasPermission("particles.fire")) {
                                effects.put(p, bs.scheduleSyncDelayedTask(pl, new FireEffect(pl, p), delay));
                                removeEffect(p);
                                return true;
                            } else {
                                sender.sendMessage(pl.getLang("noPermission"));
                                return true;
                            }
                        } else if (args[0].startsWith("s")) {
                            if (sender.hasPermission("particles.smoke")) {
                                effects.put(p, bs.scheduleSyncDelayedTask(pl, new SmokeEffect(pl, p), delay));
                                removeEffect(p);
                                return true;
                            } else {
                                sender.sendMessage(pl.getLang("noPermission"));
                                return true;
                            }
                        } else {
                            if (sender.hasPermission("particles.colored")) {
                                effects.put(p, bs.scheduleSyncDelayedTask(pl, new ColoredGround(pl, p), delay));
                                removeEffect(p);
                                return true;
                            } else {
                                sender.sendMessage(pl.getLang("noPermission"));
                                return true;
                            }
                        }
                    } else { // not 1, not less than 1, so theres more
                        if (sender.hasPermission("perticles.all")) {
                            if (args[0].startsWith("f")) {
                                if (sender.hasPermission("particles.fire")) {
                                    if (args[1].startsWith("a")) {
                                        for (Player player : p.getWorld().getPlayers()) {
                                            if (!effects.containsKey(player)) {
                                                effects.put(player, bs.scheduleSyncDelayedTask(pl, new FireEffect(pl, player), delay));
                                                removeEffect(player);
                                            }
                                        }
                                        return true;
                                    } else {
                                        try {
                                            int radius = Integer.parseInt(args[1]);
                                            for (Player player : getPlayersNear(p, radius)) {
                                                if (!effects.containsKey(player)) {
                                                    effects.put(player, bs.scheduleSyncDelayedTask(pl, new FireEffect(pl, player), delay));
                                                    removeEffect(player);
                                                }
                                            }
                                            return true;
                                        } catch (NumberFormatException e) {
                                            return false;
                                        }
                                    }
                                } else {
                                    sender.sendMessage(pl.getLang("noPermission"));
                                    return true;
                                }
                            } else if (args[0].startsWith("s")) {
                                if (sender.hasPermission("particles.smoke")) {
                                    if (args[1].startsWith("a")) {
                                        for (Player player : p.getWorld().getPlayers()) {
                                            if (!effects.containsKey(player)) {
                                                effects.put(player, bs.scheduleSyncDelayedTask(pl, new SmokeEffect(pl, player), delay));
                                                removeEffect(player);
                                            }
                                        }
                                        return true;
                                    } else {
                                        try {
                                            int radius = Integer.parseInt(args[1]);
                                            for (Player player : getPlayersNear(p, radius)) {
                                                if (!effects.containsKey(player)) {
                                                    effects.put(player, bs.scheduleSyncDelayedTask(pl, new SmokeEffect(pl, player), delay));
                                                    removeEffect(player);
                                                }
                                            }
                                            return true;
                                        } catch (NumberFormatException e) {
                                            return false;
                                        }
                                    }
                                } else {
                                    sender.sendMessage(pl.getLang("noPermission"));
                                    return true;
                                }
                            } else {
                                if (sender.hasPermission("particles.colored")) {
                                    if (args[1].startsWith("a")) {
                                        for (Player player : p.getWorld().getPlayers()) {
                                            if (!effects.containsKey(player)) {
                                                effects.put(player, bs.scheduleSyncDelayedTask(pl, new ColoredGround(pl, player), delay));
                                                removeEffect(player);
                                            }
                                        }
                                        return true;
                                    } else {
                                        try {
                                            int radius = Integer.parseInt(args[1]);
                                            for (Player player : getPlayersNear(p, radius)) {
                                                if (!effects.containsKey(player)) {
                                                    effects.put(player, bs.scheduleSyncDelayedTask(pl, new ColoredGround(pl, player), delay));
                                                    removeEffect(player);
                                                }
                                            }
                                            return true;
                                        } catch (NumberFormatException e) {
                                            return false;
                                        }
                                    }
                                } else {
                                    sender.sendMessage(pl.getLang("noPermission"));
                                    return true;
                                }
                            }
                        } else {
                            sender.sendMessage(pl.getLang("noPermission"));
                            return true;
                        }
                    }
                }
            }
        } else {
            sender.sendMessage("Only ingame");
            return true;
        }
    }

    private void removeEffect(Player pla) {
        final Player p = pla;
        pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new BukkitRunnable() {

            @Override
            public void run() {
                if (effects.containsKey(p)) {
                    pl.getServer().getScheduler().cancelTask(effects.get(p));
                    effects.remove(p);
                }
            }
        }, (20 * duration));
    }

    private List<Player> getPlayersNear(Player p, int radius) {
        List<Player> l = new ArrayList();
        for (Player player : p.getWorld().getPlayers()) {
            if (player.getLocation().distanceSquared(p.getLocation()) < (radius * radius)) {
                l.add(player);
            }
        }
        return l;
    }
}
