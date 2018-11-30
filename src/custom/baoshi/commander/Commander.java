package custom.baoshi.commander;

import custom.baoshi.Main;
import custom.baoshi.baoshi.Manager;
import custom.baoshi.inv.RemoveInventory;
import custom.baoshi.loader.Loader;
import java.io.File;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;

public class Commander implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("[CustomBaoShi]" + "不是OP");
        } else if (args.length == 0) {
            sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l宝石§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
            sender.sendMessage("§c§l▏   §c/baoshi 名称 等级 数量 玩家");
            sender.sendMessage("§c§l▏   §c/baoshi open 打开拆除面板");
            sender.sendMessage("§c§l▏   §c/baoshi reload 重载插件");
            sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l宝石§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
        } else if (args.length == 4) {
            Player pp = Main.getPlugin(Main.class).getServer().getPlayer(args[3]);
            if (pp == null) {
                sender.sendMessage("[CustomBaoShi]" + "§c玩家不在线");
            }
            int sl = Integer.parseInt(args[2]);
            if (Manager.getBaoShiStackByName(args[0]) != null){
                ItemStack item = Manager.getBaoShiStackByName(args[0]).baoShis.get(Integer.parseInt(args[1]) - 1).selfItem;
                item.setAmount(sl);
                pp.getInventory().addItem(item);    
            } else {
                sender.sendMessage("[CustomBaoShi]" + "§c不存在的宝石");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("open")){
            RemoveInventory.open((Player) sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
            Manager.baoShiStacks.clear();
            Plugin p = Main.getPlugin(Main.class);
            if (!p.getDataFolder().exists()){
                p.getDataFolder().mkdir();
            }
            if (!new File(p.getDataFolder(), "defaultBaoShi.yml").exists()){
                p.saveResource("defaultBaoShi.yml", true);    
            }        
            for (File f : p.getDataFolder().listFiles()) {
                Loader.loadBaoShiStackByFile(f);
            }
            sender.sendMessage("[CustomBaoShi]" + "§c重载完毕");
        }
        return true;
    }
}
