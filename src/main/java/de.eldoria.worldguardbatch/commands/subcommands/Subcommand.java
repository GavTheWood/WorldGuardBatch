package de.eldoria.worldguardbatch.commands.subcommands;

import org.bukkit.entity.Player;

public interface Subcommand {
    /**
     * Direct command to handel in class.
     * @param args args for command execution.
     * @return true if command was executed successfully.
     */
    boolean directCommand(Player sender, String[] args);
}
