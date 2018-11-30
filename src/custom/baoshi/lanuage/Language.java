package custom.baoshi.lanuage;

import custom.baoshi.api.API;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Language {

    public static String APPEND_TO_THE_RIGHT_KEY = "";
    public static String ADD_CANCEL_PROMPT = "";
    public static String CAN_NOT_ADD_PROMPT = "";
    public static String CAN_ADD_PROMPT = "";
    public static List<String> ADD_PROMPT_LIST = new ArrayList<>();
    public static String FUCK_BAOHUFU = "";
    public static String CREATE = "";
    public static String CREATEFAIL = "";

    public static void loadLanguage(Plugin p) {
        File file1 = new File(p.getDataFolder(), "language" + API.getFileVersion() + ".yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            APPEND_TO_THE_RIGHT_KEY = config1.getString("APPEND_TO_THE_RIGHT_KEY").replace("&", "§");
            ADD_CANCEL_PROMPT = config1.getString("ADD_CANCEL_PROMPT").replace("&", "§");
            CAN_NOT_ADD_PROMPT = config1.getString("CAN_NOT_ADD_PROMPT").replace("&", "§");
            CAN_ADD_PROMPT = config1.getString("CAN_ADD_PROMPT").replace("&", "§");
            ADD_PROMPT_LIST = API.replace(config1.getStringList("ADD_PROMPT_LIST"), "&", "§");
            FUCK_BAOHUFU = config1.getString("FUCK_BAOHUFU").replace("&", "§");
            CREATE = config1.getString("CREATE").replace("&", "§");
            CREATEFAIL = config1.getString("CREATEFAIL").replace("&", "§");
        } else {
            p.saveResource("language" + API.getFileVersion() + ".yml", true);
            loadLanguage(p);
        }
    }
}
