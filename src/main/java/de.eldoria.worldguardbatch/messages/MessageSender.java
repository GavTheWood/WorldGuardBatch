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

    /**
     * Send invalid number error.
     *
     * @param p target of the message
     */
    public static void sendInvalidNumberError(Player p) {
        sendError(p, ERROR_INVALID_NUMBERS);
    }

    /**
     * Sends world not found error.
     *
     * @param p target of the message
     */
    public static void sendWorldNotFoundError(Player p) {
        sendError(p, ERROR_WORLD_NOT_FOUND);
    }

    /**
     * Sends unknown player error.
     *
     * @param p target of the message
     */
    public static void sendUnknownPlayerError(Player p) {
        sendError(p, ERROR_UNKNOWN_PLAYER);
    }

    /**
     * Sends unknown flag error.
     *
     * @param p target of the message
     */
    public static void sendUnkownFlagError(Player p) {
        sendError(p, ERROR_UNKNOWN_FLAG);
    }

    /**
     * Srnfd wrong flag value error.
     *
     * @param p target of the message
     */
    public static void sendWrongFlagValueError(Player p) {
        sendError(p, ERROR_WRONG_FLAG_VALUE);
    }

    /**
     * Sends no regions found error.
     *
     * @param p target of the message
     */
    public static void sendNoRegionsFoundError(Player p) {
        sendError(p, ERROR_NO_REGIONS_FOUND);
    }

    /**
     * Sends region not found error.
     *
     * @param p        target of the message
     * @param regionId region which cant be found
     */
    public static void sendRegionNotFoundError(Player p, String regionId) {
        sendError(p, getRegionNotFound(regionId));
    }

    /**
     * Sends too many arguments error.
     *
     * @param p    target of the message
     * @param pArg command indicator
     */
    public static void sendTooManyArgumentError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorTooManyArguments(pArg));
    }

    /**
     * Sends too few arguments error.
     *
     * @param p    target of the message
     * @param pArg command indicator
     */
    public static void sendTooFewArgumentError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorTooFewArguments(pArg));
    }

    /**
     * Sends unknown command error.
     *
     * @param p target of the message
     */
    public static void sendUnkownCommandError(Player p) {
        sendError(p, ERROR_UNKNOWN_COMMAND);
    }

    /**
     * Sends unknown region query error.
     *
     * @param p    target of the message
     * @param pArg command indicator
     */
    public static void sendUnknownRegionQueryError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorUnknownRegionQuery(pArg));
    }

    /**
     * Sends unknown membership scope error.
     *
     * @param p    target of the message
     * @param pArg command indicator
     */
    public static void sendUnkownMembershipScopeError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorUnknownMembershipScope(pArg));
    }

    /**
     * Sends unknown check argument error.
     *
     * @param p    target of the message
     * @param pArg command indicator
     */
    public static void sendUnkownCheckArgumentError(Player p, PrimaryActionArgument pArg) {
        sendError(p, getErrorUnknownCheckArgument(pArg));
    }

    private static void sendError(Player p, String message) {
        p.sendMessage(message);
    }

    /**
     * Sends a message, when a region is modified.
     *
     * @param p      target of the message
     * @param region name of the modified region
     */
    public static void sendModifiedMessage(Player p, String region) {
        p.sendMessage("Region " + region + " modified.");
    }

    /**
     * Sends a message with the total count of modified regions.
     *
     * @param p     target of the message
     * @param count count of regions
     */
    public static void sendTotalModifiedMessage(Player p, int count) {
        p.sendMessage("Modified " + count + " regions!");
    }

}
