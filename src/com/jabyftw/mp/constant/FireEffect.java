package com.jabyftw.mp.constant;

import com.jabyftw.mp.MoarParticles;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Rafael
 */
public class FireEffect extends BukkitRunnable {

    private final MoarParticles pl;
    private final Player p;

    public FireEffect(MoarParticles pl, Player p) {
        this.pl = pl;
        this.p = p;
    }

    @Override
    public void run() {
        pl.effectFire((pl.fire / 2), p.getLocation());
    }
}
