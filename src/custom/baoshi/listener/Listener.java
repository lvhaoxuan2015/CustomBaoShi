package custom.baoshi.listener;

import custom.baoshi.Main;
import custom.baoshi.api.API;
import custom.baoshi.baoshi.BaoShi;
import custom.baoshi.baoshi.BaoShiStack;
import custom.baoshi.baoshi.Manager;
import custom.baoshi.lanuage.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.block.Action;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || (e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.isCancelled())) {
            Player p = e.getPlayer();
            ItemStack item = API.getItemInHand(p);
            if (item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (API.getBaoShiByItem(item) != null){
                    BaoShi bs = API.getBaoShiByItem(item);
                    for (BaoShiStack bss : Manager.baoShiStacks){
                        if (bss.baoShis.contains(bs)){
                            if (bss.baoShis.indexOf(bs) < bss.baoShis.size() - 1){
                                bs = bss.baoShis.get(bss.baoShis.indexOf(bs) + 1);
                                Integer a = bs.synthesis;
                                if (a != null){
                                    if (item.getAmount() >= a && a != 0){
                                        item.setAmount(item.getAmount() - a);
                                        API.setItemInHand(p, item);
                                        p.getInventory().addItem(bs.selfItem);
                                        p.sendMessage(Language.CREATE);
                                    }      
                                }                                                            
                            }                         
                        }
                    }
                }
            }                     
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() < 0) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (!e.isRightClick()) {
            return;
        }
        if (e.getInventory().getType() != InventoryType.CRAFTING && e.getInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (BaoShiManager.BaoShiUsingMap.containsKey(p.getName())) {
            if (e.isRightClick()) {
                if (e.getInventory().getType() != InventoryType.PLAYER && e.getInventory().getType() != InventoryType.CRAFTING) {
                    p.sendMessage(Language.APPEND_TO_THE_RIGHT_KEY);
                    return;
                }
                if (item == null || item.getType().equals(Material.AIR)) {
                    e.setCancelled(true);
                    p.closeInventory();
                    p.sendMessage(Language.ADD_CANCEL_PROMPT);
                    BaoShiManager.BaoShiUsingMap.remove(p.getName());
                    BaoShiManager.BaoShiFirstItemMap.remove(p.getName());
                    return;
                }
                ItemStack fitem = BaoShiManager.BaoShiFirstItemMap.get(p.getName());
                if (!Main.ItemList.contains(item.getType())) {
                    e.setCancelled(true);
                    BaoShiManager.BaoShiUsingMap.remove(p.getName());
                    BaoShiManager.BaoShiFirstItemMap.remove(p.getName());
                    p.sendMessage(Language.CAN_NOT_ADD_PROMPT);
                    return;
                }
                if (!p.getInventory().contains(fitem)) {
                    p.sendMessage(Language.CAN_NOT_ADD_PROMPT);
                    BaoShiManager.BaoShiUsingMap.remove(p.getName());
                    BaoShiManager.BaoShiFirstItemMap.remove(p.getName());
                    e.setCancelled(true);
                    return;
                }
                BaoShi l = API.getBaoShiByItem(fitem);
                double probability = Math.random() * 100 + 1;
                double syntheticProbability = Math.random() * 100 + 1;
                if (item.hasItemMeta()){
                    if (item.getItemMeta().hasLore()){
                        if (item.getItemMeta().getLore().contains(API.getBaoShiByItem(fitem).slot)){
                            if (probability < l.probability && item.hasItemMeta() && item.getItemMeta().hasLore() && syntheticProbability < l.syntheticProbability){
                                item = API.addBaoShi(item, fitem);
                                p.sendMessage(Language.CAN_ADD_PROMPT.replace("%s", l.selfItem.getItemMeta().getDisplayName()));
                                e.setCancelled(true);
                                int sl = fitem.getAmount() - 1;
                                p.getInventory().remove(fitem);
                                ItemStack i = fitem.clone();
                                i.setAmount(sl);
                                p.getInventory().addItem(i);
                                p.closeInventory();
                                BaoShiManager.BaoShiUsingMap.remove(p.getName());
                                BaoShiManager.BaoShiFirstItemMap.remove(p.getName());
                            } else {
                                e.setCancelled(true);
                                int sl = fitem.getAmount() - 1;
                                p.getInventory().remove(fitem);
                                ItemStack i = fitem.clone();
                                i.setAmount(sl);
                                p.getInventory().addItem(i);
                                p.sendMessage(Language.CREATEFAIL);
                                p.closeInventory();
                                BaoShiManager.BaoShiUsingMap.remove(p.getName());
                                BaoShiManager.BaoShiFirstItemMap.remove(p.getName());
                            }       
                        }        
                    }                   
                }                                         
            }
        } else {
            if (e.isRightClick()) {
                if (API.getBaoShiByItem(item) != null) {
                    if (!p.getInventory().contains(item)) {
                        p.sendMessage(Language.APPEND_TO_THE_RIGHT_KEY);
                        e.setCancelled(true);
                        return;
                    }
                    BaoShiManager.BaoShiUsingMap.put(p.getName(), meta);
                    BaoShiManager.BaoShiFirstItemMap.put(p.getName(), item);
                    BaoShi l = API.getBaoShiByItem(item);
                    for (String str : Language.ADD_PROMPT_LIST) {
                        p.sendMessage(str.replace("%s", l.selfItem.getItemMeta().getDisplayName()));
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                }
            }
        }
    }
}
