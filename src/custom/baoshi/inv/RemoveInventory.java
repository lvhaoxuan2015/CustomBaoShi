package custom.baoshi.inv;

import custom.baoshi.baoshi.BaoShi;
import custom.baoshi.baoshi.BaoShiStack;
import custom.baoshi.baoshi.Manager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RemoveInventory implements Listener {

    static String name = "宝石拆除";

    public static void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, "宝石拆除");
        p.openInventory(inv);
        check(inv);
    }

    public static void check(Inventory inv) {
        ItemStack bariier = new ItemStack(Material.DIAMOND);
        inv.setItem(0, bariier);
        inv.setItem(1, bariier);
        inv.setItem(2, bariier);
        inv.setItem(4, bariier);
        inv.setItem(6, bariier);
        inv.setItem(7, bariier);
        inv.setItem(8, bariier);
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a拆除所有宝石");
        List<String> lore = new ArrayList();
        lore.add("§7拆除所有宝石。");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(5, item);
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv == null) {
            return;
        }
        if (e.getRawSlot() < 0) {
            return;
        }
        if (inv.getName().equals(name)) {
            check(inv);
            if (e.getRawSlot() <= 8) {
                int slot = e.getRawSlot();
                if ((slot <= 8 && slot >= 0) && slot != 3 && slot != 5) {
                    e.setCancelled(true);
                } else if (slot == 5) {
                    if (inv.getItem(3) == null) {
                        e.setCancelled(true);
                        return;
                    }
                    List<ItemStack> items2 = getBaoShiItemByItem(inv.getItem(3));
                    ItemStack[] items = new ItemStack[items2.size()];
                    items = items2.toArray(items);
                    p.getInventory().addItem(items);
                    inv.setItem(3, removeBaoShiItemByItem(inv.getItem(3)));
                    e.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        if (inv.getName().equals(name)) {
            if (inv.getItem(3) != null) {
                p.getInventory().addItem(inv.getItem(3));
                p.sendMessage("§b物品已经回到你的背包");
            }
        }
    }

    public List<ItemStack> getBaoShiItemByItem(ItemStack item) {
        List<ItemStack> items = new ArrayList<>();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasLore()) {
                    List<String> lore = item.getItemMeta().getLore();
                    for (BaoShiStack bss : Manager.baoShiStacks) {
                        for (BaoShi bs : bss.baoShis) {
                            ItemStack itemm = bs.selfItem.clone();
                            itemm.setAmount(indexOf(lore, bs));
                            if (itemm.getAmount() > 0){
                                items.add(itemm);    
                            }                           
                        }
                    }
                    item.getItemMeta().setLore(lore);
                }
            }
        }
        return items;
    }

    public ItemStack removeBaoShiItemByItem(ItemStack item) {
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasLore()) {
                    ItemMeta im = item.getItemMeta();
                    List<String> lore = im.getLore();
                    for (BaoShiStack bss : Manager.baoShiStacks) {
                        for (BaoShi bs : bss.baoShis) {
                            remove(lore, bs);
                        }
                    }
                    im.setLore(lore);
                    item.setItemMeta(im);
                }
            }
        }
        return item;
    }

    public List<String> remove(List<String> lore, BaoShi bs) {
        List<String> re = bs.addLoreList;
        for (int i = 0; i <= lore.size() - re.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < re.size(); j++) {
                if (!lore.get(i + j).equals(re.get(j))) {
                    flag = false;
                }
            }
            if (flag) {
                int size = re.size();
                for (int x = 0; x < size; x++) {
                    lore.remove(i + x);
                    x--;
                    size--;
                }
                lore.add(i, bs.slot);
            }
        }
        return lore;
    }    
    public int indexOf(List<String> lore, BaoShi bs){
        int index = 0;
        List<String> re = bs.addLoreList;
        for (int i = 0; i <= lore.size() - re.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < re.size(); j++) {
                if (!lore.get(i + j).equals(re.get(j))) {
                    flag = false;
                }
            }
            if (flag) {
                index++;
            }
        }
        return index;
    }    
}
