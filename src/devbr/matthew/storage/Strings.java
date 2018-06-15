package devbr.matthew.storage;

import devbr.matthew.Votekick;
import devbr.matthew.util.Util;

/*
    Votekick developed by MatthewDevBr.
 */

public class Strings {

    private static Votekick vk = Votekick.getVotekick();

    public static String plugin_prefix, language_file, invalid_version, version_not_supported, no_permission, console, cooldown, permission, permission_cant_be_kicked,
            no_vote_under_progress, vote_under_progress, already_voted, cannot_vote_self, cannot_vote_player, insufficient_players, player_kicked, player_not_kicked, player_not_found,
            vote_started, player_voted_yes, player_voted_no, command_usage, command2_usage, yes, no, player_kicked_screen, player_kicked_commands, halftime_left, vote_permission,
            vote_no_permission, permission_reload;
    public static int vote_duration, vote_cooldown, percentage;
    public static boolean permission_required, vote_permission_required, check_for_updates;

    public static void startStrings() {

        plugin_prefix = Util.color(FileManager.getFromLanguage("plugin_prefix"));
        language_file = FileManager.getFromConfig("file_language");
        invalid_version = Util.color(FileManager.getFromLanguage("invalid_version"));
        version_not_supported = Util.color(FileManager.getFromLanguage("version_not_supported"));
        no_permission = Util.color(FileManager.getFromLanguage("no_permission"));
        vote_no_permission = Util.color(FileManager.getFromLanguage("vote_no_permission"));
        console = Util.color(FileManager.getFromLanguage("console"));
        cooldown = Util.color(FileManager.getFromLanguage("cooldown"));
        permission = FileManager.getFromConfig("permission");
        vote_permission = FileManager.getFromConfig("vote_permission");
        permission_cant_be_kicked = FileManager.getFromConfig("permission_cant_be_kicked");
        permission_reload = FileManager.getFromConfig("permission_reload");
        permission_required = Integer.parseInt(FileManager.getFromConfig("permission_required")) == 1 ? true : false;
        vote_permission_required = Integer.parseInt(FileManager.getFromConfig("vote_permission_required")) == 1 ? true : false;
        check_for_updates = Integer.parseInt(FileManager.getFromConfig("check_for_updates")) == 1 ? true : false;
        vote_cooldown = Integer.parseInt(FileManager.getFromConfig("vote_cooldown"));
        if (vote_cooldown <= 0) {
            vote_cooldown = 1;
        }
        vote_duration = Integer.parseInt(FileManager.getFromConfig("vote_duration"));
        percentage = Integer.parseInt(FileManager.getFromConfig("percentage"));
        if (percentage <= 0) {
            Util.print("&cPercentage cannot be minor or equals 0%! Changing to 50%.");
            percentage = 50;
        }
        if (percentage > 100) {
            Util.print("&aPercentage cannot be higher than 100%! Changing to 100%.");
            percentage = 100;
        }
        no_vote_under_progress = Util.color(FileManager.getFromLanguage("no_vote_under_progress"));
        vote_under_progress = Util.color(FileManager.getFromLanguage("vote_under_progress"));
        already_voted = Util.color(FileManager.getFromLanguage("already_voted"));
        cannot_vote_self = Util.color(FileManager.getFromLanguage("cannot_vote_self"));
        cannot_vote_player = Util.color(FileManager.getFromLanguage("cannot_vote_self"));
        insufficient_players = Util.color(FileManager.getFromLanguage("insufficient_players"));
        player_kicked = Util.color(FileManager.getFromLanguage("player_kicked"));
        player_not_kicked = Util.color(FileManager.getFromLanguage("player_not_kicked"));
        player_not_found = Util.color(FileManager.getFromLanguage("player_not_found"));
        vote_started = Util.color(FileManager.getFromLanguage("vote_started"));
        player_voted_yes = Util.color(FileManager.getFromLanguage("player_voted_yes"));
        player_voted_no = Util.color(FileManager.getFromLanguage("player_voted_no"));
        command_usage = Util.color(FileManager.getFromLanguage("command_usage"));
        command2_usage = Util.color(FileManager.getFromLanguage("command2_usage"));
        halftime_left = Util.color(FileManager.getFromLanguage("halftime_left"));
        yes = FileManager.getFromLanguage("vote_yes");
        no = FileManager.getFromLanguage("vote_no");

    }

    public enum DefaultStrings {

        plugin_prefix("&7[&cVotekick&7]"),
        invalid_version("&cInvalid version, turning off. Plugin version: &e%version%&c. Version on plugin.yml: &e%yml_version%&c."),
        language_file("en.yml"),
        no_permission("&cInsufficient permissions."),
        vote_no_permission("&cInsufficient permissions."),
        console("&cYou are not a player."),
        cooldown("&cPlease, wait more &e%seconds%s &cbefore start a new vote."),
        permission("votekick.start"),
        vote_permission("votekick.vote"),
        permission_cant_be_kicked("votekick.admin"),
        permission_reload("votekick.admin.reload"),
        no_vote_under_progress("&cThere is no voting under progress."),
        vote_under_progress("&cA voting is already under progress."),
        already_voted("&cYou already voted."),
        cannot_vote_self("&cYou cannot open a voting to kick yourself."),
        cannot_vote_player("&cThis player cannot be kicked."),
        insufficient_players("&7At least 4 players are required to start a vote."),
        player_kicked("&c%player% &7got kicked due to a voting!"),
        player_not_kicked("&c%player% &7cannot be kicked! Insufficient votes."),
        player_not_found("&cPlayer not found!"),
        vote_started("&b%player% &7started a vote to kick &c%player_in_vote%&7! &e%votes_required% &7votes are required.%new-line%&7Voting ends in: &e%time_left%s&7, To vote use: &a/vote <yes/no>"),
        player_voted_yes("&b%player% &7voted &ayes&7! Votes: &e%votes%&7/&e%votes_required% &7Voting ends in: &e%time_left%s&7%new-line%&7To vote use: &a/vote <yes/no>"),
        player_voted_no("&b%player% &7voted &cno&7! Votes: &e%votes%&7/&e%votes_required% &7Voting ends in: &e%time_left%s&7%new-line%&7To vote use: &a/vote <yes/no>"),
        command_usage("&a/votekick <player>"),
        command2_usage("&a/vote <yes/no>"),
        halftime_left("&7Voting to kick &c%player% &7ends in: &e%time_left%s&7."),
        yes("yes"),
        no("no"),

        check_for_updates("1"),
        permission_required("1"),
        vote_permission_required("0"),
        vote_duration("60"),
        vote_cooldown("120"),
        percentage("50"),

        player_kicked_screen("\n&cYou got kicked from server!\n&cWell, a voting kicked you.\n"),
        player_kicked_commands("kick %player% %kick_screen%"),

        version_not_supported("&cThis server version is not supported! Version: &e%server_version%");

        private String s;
        DefaultStrings(String s) {
            this.s = s;
        }
        public String getDefaultString() {
            return Util.color(s);
        }
    }

}
