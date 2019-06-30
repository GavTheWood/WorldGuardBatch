package de.eldoria.worldguardbatch.commands.basecommand;

import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import org.bukkit.entity.Player;

public interface Subcommand {
    /**
     * Direct command to handel in class.
     *
     * @param sender sender of the command.
     * @param pArg Primary arg of the command.
     * @param args   args for command execution.
     */
    void directCommand(Player sender, PrimaryActionArgument pArg, String[] args);
}