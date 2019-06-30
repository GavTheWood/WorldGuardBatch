package de.eldoria.worldguardbatch.commands.basecommand;

import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.messages.CommandText;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.messages.MessagesLib;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class HelpCommand implements Subcommand {
    MessageSender ms;


    public HelpCommand() {
        this.ms = MessageSender.getInstance();
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length == 1) {
            sendGeneralHelp(sender);
        } else if (args.length == 2) {
            var command = PrimaryActionArgument.getPrimary(args[1]);
            if (command == PrimaryActionArgument.NONE) {
                ms.sendUnkownCommandError(sender);
            }else{
                sendCommandHelp(sender, command);
            }
        }

    }

    private void sendGeneralHelp(Player p) {
        StringJoiner stringJoiner = new StringJoiner(ms.getNewLine());

        for (PrimaryActionArgument arg : PrimaryActionArgument.values()) {
            CommandText command = MessagesLib.getCommandText(arg);
            if (command != null) {
                String cmd = command.getDescription() + ms.getNewLine() +
                        command.getPattern();
                stringJoiner.add(cmd);
            }
        }

        p.sendMessage(stringJoiner.toString());

    }

    private void sendCommandHelp(Player p, PrimaryActionArgument pArg) {
        var command = MessagesLib.getCommandText(pArg);

        if (command == null) {
            ms.sendUnkownCommandError(p);
            return;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(command.getDescription()).append(ms.getNewLine())
                .append(command.getPattern()).append(ms.getNewLine()).append(command.getQueries());

        p.sendMessage(builder.toString());
    }

}
