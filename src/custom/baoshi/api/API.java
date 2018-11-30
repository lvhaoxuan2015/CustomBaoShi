package custom.baoshi.api;

import custom.baoshi.Main;
import static custom.baoshi.Main.ServerVersion;
import custom.baoshi.baoshi.BaoShi;
import custom.baoshi.baoshi.BaoShiStack;
import custom.baoshi.baoshi.Manager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class API {

    //优化完成
    public static String getFileVersion() {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return "-1_9";
        } else {
            return "-1_8";
        }
    }

    public static List<String> replace(List<String> a, String b, String c) {
        List<String> ret = new ArrayList<>();
        for (String d : a) {
            ret.add(d.replace(b, c));
        }
        return ret;
    }

    //优化完成
    public static ItemStack getItemInHand(Player p) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return p.getInventory().getItemInMainHand();
        }
        return p.getInventory().getItemInHand();
    }

    public static ItemStack getItemInOffHand(Player p) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return p.getInventory().getItemInOffHand();
        }
        return new ItemStack(Material.AIR);
    }

    //优化完成
    public static void setItemInHand(Player p, ItemStack i) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            p.getInventory().setItemInMainHand(i);
        } else {
            p.getInventory().setItemInHand(i);
        }
    }

    public static String getServerVersion() throws NoSuchMethodException {
        String sv = null;
        Method[] m = PlayerInventory.class.getDeclaredMethods();
        try {
            Class clas = Class.forName("org.bukkit.Particle");
            sv = "1.13";
            return sv;
        } catch (ClassNotFoundException ex) {
        }
        for (Method e : m) {
            if (e.toGenericString().contains("getItemInMainHand")) {
                sv = "1.9+";
                return sv;
            }
        }
        if (sv == null) {
            if (Material.getMaterial("SLIME_BLOCK") != null) {
                sv = "1.8";
                return sv;
            } else {
                sv = "1.7";
                return sv;
            }
        }
        return sv;
    }
    
    //优化完成
    public static ItemStack addBaoShi(ItemStack i, ItemStack baoShi) {
        if (i != null && baoShi != null) {
            if (baoShi.hasItemMeta() && baoShi.getItemMeta().hasLore()) {
                if (getBaoShiByItem(baoShi) != null) {
                    ItemMeta meta = i.getItemMeta();
                    List<String> lore = new ArrayList();
                    if (meta.hasLore()) {
                        lore.addAll(meta.getLore());
                    }                   
                    BaoShi l = getBaoShiByItem(baoShi);
                    int ii = lore.indexOf(l.slot);
                    lore.remove(ii);
                    lore.addAll(ii, l.addLoreList);
                    meta.setLore(lore);
                    i.setItemMeta(meta);              
                }
            }
        }
        return i;
    }

    //优化完成
    public static BaoShi getBaoShiByItem(ItemStack i) {
        BaoShi b = null;
        if (i != null) {
            if (i.hasItemMeta()) {
                for (BaoShiStack baoShiStackKey : Manager.baoShiStacks) {
                    for (BaoShi baoShiKey : baoShiStackKey.baoShis) {
                        if (baoShiKey.selfItem.getItemMeta().equals(i.getItemMeta())) {
                            b = baoShiKey;
                            break;
                        }
                    }
                }
            }
        }
        return b;
    }    

}
