package custom.baoshi.loader;

import custom.baoshi.Main;
import custom.baoshi.api.API;
import custom.baoshi.baoshi.BaoShi;
import custom.baoshi.baoshi.BaoShiStack;
import custom.baoshi.baoshi.Manager;
import java.io.File;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Loader {

    static Plugin p = Main.getPlugin(Main.class);

    public static void readCuiLianItem(Plugin p) {
        Main.ItemList.clear();
        File file1 = new File(p.getDataFolder(), "Item.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String str : config1.getStringList("items")) {
                Main.ItemList.add(Material.getMaterial(str));
            }
        } else {
            p.saveResource("Item.yml", true);
            readCuiLianItem(p);
        }
    }

    public static void loadBaoShiStackByFile(File f) {
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(f);
        BaoShiStack bss = new BaoShiStack(f.getName());
        for (int i = 0; config1.get(i + ".DisplayName") != null; i++) {
            String name = config1.getString(i + ".DisplayName").replace("&", "§");
            String slot = config1.getString(i + ".Slot").replace("&", "§");
            Integer synthesis = config1.getInt(i + ".Synthesis");
            List<String> lore = API.replace(config1.getStringList(i + ".Lore"), "&", "§");
            Material itemtype = Material.getMaterial(config1.getString(i + ".Type"));
            Double probability = config1.getDouble(i + ".Probability");
            List<String> addLores = API.replace(config1.getStringList(i + ".AddLores"), "&", "§");
            Integer syntheticProbability = config1.getInt(i + ".SyntheticProbability");
            ItemStack item = new ItemStack(itemtype);
            ItemMeta id = item.getItemMeta();
            id.setDisplayName(name);
            id.setLore(lore);
            item.setItemMeta(id);
            BaoShi bs = new BaoShi(item, probability, addLores, slot, synthesis, syntheticProbability);
            bss.baoShis.add(bs);
        }
        Manager.baoShiStacks.add(bss);
    }
}
