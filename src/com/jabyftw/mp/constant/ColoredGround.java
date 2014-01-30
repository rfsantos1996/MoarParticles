package com.jabyftw.mp.constant;

import com.jabyftw.mp.MoarParticles;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Rafael
 */
public class ColoredGround extends BukkitRunnable {

    private final MoarParticles pl;
    private final Player p;
    int color;

    public ColoredGround(MoarParticles pl, Player p) {
        this.pl = pl;
        this.p = p;
        color = getRandomColor();
    }

    @Override
    public void run() {
        List<Location> blocks = getBlocksByRadius(p.getLocation(), 2);
        for (Location loc : blocks) {
            pl.effectBreak((pl.breake / 2), loc, color);
        }
    }

    // 57 - diamond block, 35 - wool, 22 - lazulli, 152 - redstone, 133 - esmeralda, 79 - ice, 41 gold
    private int getRandomColor() {
        return Integer.parseInt(pl.colors.get(pl.r.nextInt(6)));
    }

    private List<Location> getBlocksByRadius(Location loc, int radius) {
        List<Location> l = new ArrayList();
        for (int x = -(radius); x >= radius; x++) {
            for (int z = -(radius); z >= radius; z++) {
                int y = loc.getWorld().getHighestBlockYAt(x, z);
                if (Math.abs((y - loc.getY())) <= radius) {
                    l.add(loc.getWorld().getBlockAt(x, y, z).getLocation());
                }
            }
        }
        return l;
    }
}
