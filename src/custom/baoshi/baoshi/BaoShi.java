package custom.baoshi.baoshi;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class BaoShi {

    public ItemStack selfItem;
    public List<String> addLoreList;
    public Double probability;
    public String slot;
    public Integer synthesis;
    public Integer syntheticProbability;

    public BaoShi(ItemStack selfItem, Double probability, List<String> addLoreList, String slot, Integer synthesis, Integer syntheticProbability) {
        this.selfItem = selfItem;
        this.addLoreList = addLoreList;
        this.probability = probability;
        this.slot = slot;
        this.synthesis = synthesis;
        this.syntheticProbability = syntheticProbability;
    }
}
