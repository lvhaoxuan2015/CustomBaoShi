package custom.baoshi;

import custom.baoshi.api.API;
import custom.baoshi.commander.Commander;
import custom.baoshi.inv.RemoveInventory;
import custom.baoshi.lanuage.Language;
import custom.baoshi.listener.Listener;
import custom.baoshi.loader.Loader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static String ServerVersion = "";
    public static List<Material> ItemList = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        if (!new File(getDataFolder(), "defaultBaoShi.yml").exists()){
            saveResource("defaultBaoShi.yml", true);    
        }        
        for (File f : getDataFolder().listFiles()) {
            Loader.loadBaoShiStackByFile(f);
        }
        Loader.readCuiLianItem(this);
        Language.loadLanguage(this);
        getServer().getPluginManager().registerEvents(new Listener(), this);
        getServer().getPluginManager().registerEvents(new RemoveInventory(), this);
        getServer().getPluginCommand("baoshi").setExecutor(new Commander());
        try {
            ServerVersion = API.getServerVersion();
        } catch (NoSuchMethodException ex) {
        }
        Language.loadLanguage(this);
    }
}
