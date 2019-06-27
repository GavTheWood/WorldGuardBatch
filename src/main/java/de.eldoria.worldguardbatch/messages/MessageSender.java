package de.eldoria.worldguardbatch.messages;

import de.eldoria.worldguardbatch.commands.PrimaryActionArgument;
import org.bukkit.entity.Player;

import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_INVALID_NUMBERS;
import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_NO_REGIONS_FOUND;
import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_UNKNOWN_COMMAND;
import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_UNKNOWN_FLAG;
import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_UNKNOWN_PLAYER;
import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_WORLD_NOT_FOUND;
import static de.eldoria.worldguardbatch.messages.MessagesLib.ERROR_WRONG_FLAG_VALUE;
import static de.eldoria.worldguardbatch.messages.MessagesLib.getErrorTooFewArguments;
import static de.eldoria.worldguardbatch.messages.MessagesLib.getErrorTooManyArguments;
import static de.eldoria.worldguardbatch.messages.MessagesLib.getErrorUnknownCheckArgument;
import static de.eldoria.worldguardbatch.messages.MessagesLib.getErrorUnknownMembershipScope;
import static de.eldoria.worldguardbatch.messages.MessagesLib.getErrorUnknownRegionQuery;
import static de.eldoria.worldguardbatch.messages.MessagesLib.getRegionNotFound;

public class MessageSender {

    /**
     * Sends a too few or too many arg message.
     *
     * @param p              player which should receive the message
     * @param actionArgument action argument of command
     * @param args           args array
     * @param requiredArgs   required args.
     */
    public static void sendArgumentMessage(Player p, PrimaryActionArgument actionArgument,
                                           String[] args, int requiredArgs) {
        sendArgumentMessage(p, actionArgument, args, requiredArgs, requiredArgs);
    }

    /**
     * Sends a too few or too many arg message.
     *
     * @param p              player which should receive the message
     * @param actionArgument action argument of command
     * @param args           args array
     * @param min            min required argument count
     * @param max            max required argument count
     */
    public static void sendArgumentMessage(Player p, PrimaryActionArgument actionArgument,
                                           String[] args, int min, int max) {
        if (args.length < min) {
            p.sendMessage(getErrorTooFewArguments(actionArgument));
        } else if (args.length > max) {
            p.sendMessage(getErrorTooManyArguments(actionArgument));
        }
    }

    public static void sendInvalidNumberError(Player p) {
        sendError(p, ERROR_INVALID_NUMBERS);
    }

    public static void sendWorldNotFoundError(Player p) {
        sendError(p, ERROR_WORLD_NOT_FOUND);
    }

    public static void sendUnknownPlayerError(Player p) {
        sendError(p, ERROR_UNKNOWN_PLAYER);
    }

    public static void sendUnkownFlagError(Player p) {
        sendError(p, ERROR_UNKNOWN_FLAG);
    }

    public static void sendWrongFlagValueError(Player p) {
        sendError(p, ERROR_WRONG_FLAG_VALUE);
    }

    public static void sendNoRegionsFoundError(Player p) {
        sendError(p, ERROR_NO_REGIONS_FOUND);
    }

    public static void sendRegionNotFoundError(Player p, String regionId) {
        sendError(p, getRegionNotFound(regionId));
    }

    public static void sendTooManyArgumentError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorTooManyArguments(pArg));
    }

    public static void sendTooFewArgumentError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorTooFewArguments(pArg));
    }

    public static void sendUnkownCommandError(Player p, PrimaryActionArgument pArg) {
        sendError(p, ERROR_UNKNOWN_COMMAND);
    }

    public static void sendUnknownRegionQueryError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorUnknownRegionQuery(pArg));
    }

    public static void sendUnkownMembershipScopeError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorUnknownMembershipScope(pArg));
    }

    public static void sendUnkownCheckArgumentError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorUnknownCheckArgument(pArg));
    }

    private static void sendError(Player p, String message) {
        p.sendMessage(message);
    }

}
