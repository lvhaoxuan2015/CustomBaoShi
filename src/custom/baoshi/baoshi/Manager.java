package custom.baoshi.baoshi;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    public static List<BaoShiStack> baoShiStacks = new ArrayList<>();
    
    public static BaoShiStack getBaoShiStackByName(String name){
        for (BaoShiStack bss : baoShiStacks){
            if (bss.name.equals(name)){
                return bss;
            }
        }
        return null;
    }
}
