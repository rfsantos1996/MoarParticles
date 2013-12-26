package com.jabyftw.mp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Rafael
 */
public class ParticlesExecutor implements CommandExecutor {

    private final MoarParticles pl;

    public ParticlesExecutor(MoarParticles pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("particles.listener")) {
            if(args.length < 2) {
                return false;
            } else {
                String effect = args[0];
                boolean value = Boolean.parseBoolean(args[1]);
                if(effect.startsWith("e")) {
                    pl.entityDamage = value;
                    sender.sendMessage(pl.getLang("changedValue").replaceAll("%value", "entity damaged").replaceAll("%to", args[1]));
                    return true;
                } else if(effect.startsWith("p")) {
                    pl.playerDamage = value;
                    sender.sendMessage(pl.getLang("changedValue").replaceAll("%value", "player damaged").replaceAll("%to", args[1]));
                    return true;
                } else if(effect.startsWith("t")) {
                    pl.teleport = value;
                    sender.sendMessage(pl.getLang("changedValue").replaceAll("%value", "teleport").replaceAll("%to", args[1]));
                    return true;
                } else {
                    pl.fallDamage = value;
                    sender.sendMessage(pl.getLang("changedValue").replaceAll("%value", "fall damage").replaceAll("%to", args[1]));
                    return true;
                }
            }
        } else {
            pl.getLang("noPermission");
            return true;
        }
    }
}
