package devbr.matthew.storage;

import devbr.matthew.Votekick;
import devbr.matthew.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.util.List;

public class FileManager {

    private static File config_file;
    private static File language_file;

    private static FileConfiguration config_yml;
    private static FileConfiguration language_yml;

    private static Votekick vk = Votekick.getVotekick();

    public static void startFileManager() {
        if (!vk.getDataFolder().exists()) {
            vk.getDataFolder().mkdir();
        }

        config_file = new File(vk.getDataFolder(), "config.yml");
        if (!config_file.exists()) {
            vk.saveResource("config.yml", false);
        }
        loadConfig();

        File en_yml = new File(vk.getDataFolder(), "en.yml");
        if (!en_yml.exists()) {
            vk.saveResource("en.yml", false);
        }

        Strings.language_file = FileManager.getFromConfig("file_language");
        language_file = new File(vk.getDataFolder(), Strings.language_file);
        if (!language_file.exists()) {
            Util.print("&cLanguage file &7'"+Strings.language_file+"' &cnot found! Using default &7'en.yml'&c.");
            language_file = new File(vk.getDataFolder(), "en.yml");
            Strings.language_file = Strings.DefaultStrings.language_file.getDefaultString();
        }
        loadLanguageFile();

        List<String> kick_screen = language_yml.getStringList("player_kicked_screen");
        List<String> kick_commands = language_yml.getStringList("player_kicked_commands");

        if (kick_screen != null) {
            String build_kick_screen = "";
            for (String s : kick_screen) {
                build_kick_screen = build_kick_screen + "\n" + s;
            }
            build_kick_screen = Util.color(build_kick_screen);
            Strings.player_kicked_screen = build_kick_screen;
        } else {
            Util.print("&cCannot load &7'player_kicked_screen' &cfrom language file &7'"+Strings.language_file+"'&c. Using default.");
            Strings.player_kicked_screen = Strings.DefaultStrings.player_kicked_screen.getDefaultString();
        }

        if (kick_commands != null) {
            String build_kick_commands = "";
            for (String s : kick_commands) {
                build_kick_commands = build_kick_commands + "%next-command%" + s;
            }
            build_kick_commands = Util.color(build_kick_commands);
            Strings.player_kicked_commands = build_kick_commands;
        } else {
            Util.print("&cCannot load &7'player_kicked_commands' &cfrom language file &7'"+Strings.language_file+"'&c. Using default.");
            Strings.player_kicked_commands = Strings.DefaultStrings.player_kicked_commands.getDefaultString();
        }

    }

    public static void loadConfig() {
        config_yml = YamlConfiguration.loadConfiguration(config_file);
        if (config_yml == null) {
            Util.shutdown("&cCannot load config file! Please delete the config file.");
        }
    }
    public static void loadLanguageFile() {
        language_yml = YamlConfiguration.loadConfiguration(language_file);
        if (language_yml == null) {
            Util.shutdown("&cCannot load language file! File: &7'"+Strings.language_file+"'");
        }
    }

    public static String getFromConfig(String path) {
        String toReturn = config_yml.getString(path);
        if (toReturn == null) {
            Util.print("&cCannot load &7'"+path+"' &cfrom config. Using default.");
            return Strings.DefaultStrings.valueOf(path).getDefaultString();
        }
        return toReturn;
    }
    public static String getFromLanguage(String path) {
        if (language_yml == null) {
            Util.print("&cCannot load &7'"+path+"' &cfrom language file &7'"+Strings.language_file+"'&c. Using default.");
            return Strings.DefaultStrings.valueOf(path).getDefaultString();
        }
        String toReturn = language_yml.getString(path);
        if (toReturn == null) {
            Util.print("&cCannot load &7'"+path+"' &cfrom language file &7'"+Strings.language_file+"'&c. Using default.");
            return Strings.DefaultStrings.valueOf(path).getDefaultString();
        }
        return toReturn;
    }

}
