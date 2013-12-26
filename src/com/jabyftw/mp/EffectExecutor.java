package com.jabyftw.mp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Rafael
 */
public class EffectExecutor implements CommandExecutor {

    private final MoarParticles pl;

    public EffectExecutor(MoarParticles pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}
