package de.eldoria.worldguardbatch.commands.basecommand;

import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import de.eldoria.worldguardbatch.messages.CommandText;
import de.eldoria.worldguardbatch.messages.MessageSender;
import de.eldoria.worldguardbatch.messages.MessagesLib;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class HelpCommand implements Subcommand {
    private MessageSender messageSender;


    /**
     * Creates new HelpCommand instance.
     */
    public HelpCommand() {
        this.messageSender = MessageSender.getInstance();
    }

    @Override
    public void directCommand(Player sender, PrimaryActionArgument pArg, String[] args) {
        if (args.length == 1) {
            sendGeneralHelp(sender);
        } else if (args.length == 2) {
            var command = PrimaryActionArgument.getPrimary(args[1]);
            if (command == PrimaryActionArgument.NONE) {
                messageSender.sendUnkownCommandError(sender);
            } else {
                sendCommandHelp(sender, command);
            }
        }

    }

    private void sendGeneralHelp(Player p) {
        StringJoiner stringJoiner = new StringJoiner(messageSender.getNewLine());

        for (PrimaryActionArgument arg : PrimaryActionArgument.values()) {
            CommandText command = MessagesLib.getCommandText(arg);
            if (command != null) {
                String cmd = command.getDescription() + messageSender.getNewLine()
                        + command.getPattern();
                stringJoiner.add(cmd);
            }
        }

        p.sendMessage(stringJoiner.toString());

    }

    private void sendCommandHelp(Player p, PrimaryActionArgument pArg) {
        var command = MessagesLib.getCommandText(pArg);

        if (command == null) {
            messageSender.sendUnkownCommandError(p);
            return;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(command.getDescription()).append(messageSender.getNewLine())
                .append(command.getPattern()).append(messageSender.getNewLine()).append(command.getQueries());

        p.sendMessage(builder.toString());
    }

}
