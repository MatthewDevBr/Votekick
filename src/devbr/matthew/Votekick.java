package devbr.matthew;

import devbr.matthew.storage.FileManager;
import devbr.matthew.storage.Strings;
import devbr.matthew.util.ReflectionUtil;
import devbr.matthew.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Votekick extends JavaPlugin {

    public double plugin_version = 0.1;
    public String plugin_yml_version;

    public void onEnable() {
        FileManager.startFileManager();
        Strings.startStrings();
        Util.print("&aYour server version is &e" + ReflectionUtil.getServerVersion() + "&a!");
        if (!isVersionSupported()) {
            Util.shutdown(Strings.version_not_supported.replace("%version%", this.plugin_version+"")
                    .replace("%yml_version%", this.plugin_yml_version)
                    .replace("%server_version%", ReflectionUtil.getServerVersion()));
            return;
        }
        plugin_yml_version = this.getDescription().getVersion();
        double checkVersion;
        try {
            checkVersion = Double.parseDouble(plugin_yml_version);
        } catch (NumberFormatException e) {
            Util.shutdown(Strings.invalid_version.replace("%version%", this.plugin_version+"")
                    .replace("%yml_version%", this.plugin_yml_version)
                    .replace("%server_version%", ReflectionUtil.getServerVersion()));
            return;
        }
        if (Double.parseDouble(plugin_yml_version) != plugin_version) {
            Util.shutdown(Strings.invalid_version.replace("%version%", this.plugin_version+"")
                    .replace("%yml_version%", this.plugin_yml_version)
                    .replace("%server_version%", ReflectionUtil.getServerVersion()));
            return;
        }
        Util.print("&a&m====================");
        Util.print(" &c&lVotekick &fversion &e" + plugin_version + "&f.");
        Util.print(" &fDeveloped by &bMatthewDevBr&f.");
        Util.print("&a&m====================");
    }

    private boolean isVersionSupported() {
        String version = ReflectionUtil.getServerVersion();
        return version.contains("v1_8") || version.contains("v1_9") || version.contains("v1_10") || version.contains("v1_11") || version.contains("v1_12");
    }

    public static Votekick getVotekick() {
        return Votekick.getPlugin(Votekick.class);
    }

    private Map<UUID, Integer> delay = new HashMap<>();
    private List<UUID> voted = new ArrayList<>();
    private int votes_required;
    private int votes;
    private int time_left;
    private Player toBeKicked;
    private String toBeKickedName;
    public boolean vote_under_progress = false;
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (!(s instanceof Player)) {
            s.sendMessage(Strings.plugin_prefix+" "+Strings.console);
            return true;
        }
        Player p = (Player) s;
        if (l.equalsIgnoreCase("votekick")) {
            if (a.length != 1) {
                Util.sendMessage(p, Strings.command_usage);
                return true;
            }
            if (Strings.permission_required) {
                if (!p.hasPermission(Strings.permission)) {
                    Util.sendMessage(p, Strings.no_permission);
                    return true;
                }
            }
            if (vote_under_progress) {
                Util.sendMessage(p, Strings.vote_under_progress);
                return true;
            }
            Player toKick = Bukkit.getPlayer(a[0]);
            if (toKick == null) {
                Util.sendMessage(p, Strings.player_not_found);
                return true;
            }
            if (toKick == p) {
                Util.sendMessage(p, Strings.cannot_vote_self);
                return true;
            }
            if (toKick.hasPermission(Strings.permission_cant_be_kicked)) {
                Util.sendMessage(p, Strings.cannot_vote_player);
                return true;
            }
            if (Bukkit.getOnlinePlayers().size() < 4) {
                Util.sendMessage(p, Strings.insufficient_players);
                return true;
            }
            if (delay.containsKey(p.getUniqueId())) {
                Util.sendMessage(p, Strings.cooldown.replace("%seconds%", delay.get(p.getUniqueId())+""));
                return true;
            }
            delay.put(p.getUniqueId(), Strings.vote_cooldown);
            new BukkitRunnable() {
                UUID uuid = p.getUniqueId();
                int cooldown = Strings.vote_cooldown;
                @Override
                public void run() {
                    cooldown--;
                    if (cooldown <= 0) {
                        delay.remove(uuid);
                        this.cancel();
                        return;
                    }
                    delay.put(uuid, cooldown);
                }
            }.runTaskTimerAsynchronously(this, 20, 20);

            this.votes_required = Util.getVotesRequired();
            this.votes = 1;
            this.toBeKicked = toKick;
            this.toBeKickedName = toKick.getName();
            this.vote_under_progress = true;
            this.voted.add(p.getUniqueId());
            this.voted.add(toKick.getUniqueId());
            this.time_left = Strings.vote_duration;

            String vote_started = Strings.vote_started.replace("%player%", p.getName()).replace("%player_in_vote%", toKick.getName()).replace("%votes_required%", votes_required+"")
                    .replace("%time_left%", time_left+"").replace("%votes%", votes+"");
            String[] vote_started_split = null;
            if (vote_started.contains("\n")) {
                vote_started_split = vote_started.split("\n");
            } else if (vote_started.contains("%new-line%")){
                vote_started_split = vote_started.split("%new-line%");
            }
            if (vote_started_split == null) {
                Bukkit.broadcastMessage(Strings.plugin_prefix+" "+vote_started);
            } else {
                for (String split : vote_started_split) {
                    Bukkit.broadcastMessage(Strings.plugin_prefix+" "+split);
                }
            }
            new BukkitRunnable() {
                String pNick = toKick.getName();
                int halftime = time_left/2;
                @Override
                public void run() {
                    time_left--;
                    if (time_left <= 0) {
                        if (votes >= votes_required) {
                            Bukkit.broadcastMessage(Strings.plugin_prefix+" "+Strings.player_kicked.replace("%player%", pNick));
                            if (toBeKicked != null) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        String commands = Strings.player_kicked_commands;
                                        if (commands.contains("%next-command%")) {
                                            String[] toSplit = commands.split("%next-command%");
                                            for (String cmd : toSplit) {
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", toBeKickedName).replace("%kick_screen%", Strings.player_kicked_screen));
                                            }
                                        } else {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("%player%", toBeKickedName).replace("%kick_screen%", Strings.player_kicked_screen));
                                        }
                                    }
                                }.runTask(getVotekick());
                            }
                        } else {
                            Bukkit.broadcastMessage(Strings.plugin_prefix+" "+Strings.player_not_kicked.replace("%player%", pNick));
                        }
                        votes_required = 0;
                        votes = 0;
                        toBeKicked = null;
                        vote_under_progress = false;
                        time_left = 0;
                        voted.clear();
                        this.cancel();
                        return;
                    }
                    if (time_left == halftime) {
                        Bukkit.broadcastMessage(Strings.plugin_prefix+" "+Strings.halftime_left.replace("%player%", pNick).replace("%time_left%", time_left+""));
                    }
                    if (votes >= votes_required) {
                        time_left = 0;
                    }
                }
            }.runTaskTimerAsynchronously(this, 20, 20);
            return true;
        } else if (l.equalsIgnoreCase("vote")) {
            if (a.length != 1) {
                Util.sendMessage(p, Strings.command2_usage);
                return true;
            }
            if (Strings.vote_permission_required) {
                if (!p.hasPermission(Strings.vote_permission)) {
                    Util.sendMessage(p, Strings.vote_no_permission);
                    return true;
                }
            }
            if (!vote_under_progress) {
                Util.sendMessage(p, Strings.no_vote_under_progress);
                return true;
            }
            if (voted.contains(p.getUniqueId())) {
                Util.sendMessage(p, Strings.already_voted);
                return true;
            }
            if (a[0].equalsIgnoreCase(Strings.yes)) {
                String voted_yes = Strings.player_voted_yes.replace("%player%", p.getName()).replace("%player_in_vote%", toBeKickedName).replace("%votes_required%", votes_required+"")
                        .replace("%time_left%", time_left+"").replace("%votes%", votes+"");
                String[] voted_yes_split = null;
                if (voted_yes.contains("\n")) {
                    voted_yes_split = voted_yes.split("\n");
                } else if (voted_yes.contains("%new-line%")){
                    voted_yes_split = voted_yes.split("%new-line%");
                }
                if (voted_yes_split == null) {
                    Bukkit.broadcastMessage(Strings.plugin_prefix+" "+voted_yes);
                } else {
                    for (String split : voted_yes_split) {
                        Bukkit.broadcastMessage(Strings.plugin_prefix+" "+split);
                    }
                }
                votes = votes + 1;
                voted.add(p.getUniqueId());
                return true;
            }
            if (a[0].equalsIgnoreCase(Strings.no)) {
                String voted_no = Strings.player_voted_no.replace("%player%", p.getName()).replace("%player_in_vote%", toBeKicked.getName()).replace("%votes_required%", votes_required+"")
                        .replace("%time_left%", time_left+"").replace("%votes%", votes+"");
                String[] voted_no_split = null;
                if (voted_no.contains("\n")) {
                    voted_no_split = voted_no.split("\n");
                } else if (voted_no.contains("%new-line%")){
                    voted_no_split = voted_no.split("%new-line%");
                }
                if (voted_no_split == null) {
                    Bukkit.broadcastMessage(Strings.plugin_prefix+" "+voted_no_split);
                } else {
                    for (String split : voted_no_split) {
                        Bukkit.broadcastMessage(Strings.plugin_prefix+" "+split);
                    }
                }
                voted.add(p.getUniqueId());
                return true;
            } else {
                Util.sendMessage(p, Strings.command2_usage);
                return true;
            }
        }
        return false;
    }

}
