package devbr.matthew.util;

import devbr.matthew.Votekick;
import devbr.matthew.storage.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {

    public static void print(String s) {
        Bukkit.getConsoleSender().sendMessage(Strings.DefaultStrings.plugin_prefix.getDefaultString()+" "+color(s));
    }
    public static void shutdown(String reason) {
        print("&cTurning off. &7Reason: "+reason);
        Votekick.getVotekick().getServer().getPluginManager().disablePlugin(Votekick.getVotekick());
    }

    public static String color(String s) {
        if (s == null) {
            return "null";
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(Player p, String msg) {
        p.sendMessage(Strings.plugin_prefix+" "+Util.color(msg));
    }

    public static int getVotesRequired() {
        int players_online = Bukkit.getOnlinePlayers().size();
        int percentage = Strings.percentage;
        int votes = (players_online*percentage)/100;
        return votes;
    }

    public int getPing(Player p) {
        try {
            Class<?> craftPlayer = ReflectionUtil.getNMSClass("entity.CraftPlayer");
            Object handle = ReflectionUtil.getMethod(craftPlayer, "getHandle").invoke(p);
            int ping = (Integer) ReflectionUtil.getField(handle.getClass(), "ping").get(handle);
            return ping;
        }catch (Exception e) {
            print("&cCannot get ping for player "+p.getName()+"!");
            return 0;
        }
    }

}
