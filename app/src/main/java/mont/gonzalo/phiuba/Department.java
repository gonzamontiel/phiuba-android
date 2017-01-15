package mont.gonzalo.phiuba;

import java.util.HashMap;

/**
 * Created by gonzalo on 1/14/17.
 */
public class Department {

    static private HashMap<String,Integer> depMap = createIconsMap();
    static private HashMap<String,Integer> createIconsMap() {
        depMap = new HashMap<String, Integer>();
        depMap.put("61", R.drawable.dep61);
        depMap.put("62", R.drawable.dep62);
        depMap.put("63", R.drawable.dep63);
        depMap.put("64", R.drawable.dep64);
        depMap.put("65", R.drawable.dep65);
        depMap.put("66", R.drawable.dep66);
        depMap.put("67", R.drawable.dep67);
        depMap.put("68", R.drawable.dep68);
        depMap.put("69", R.drawable.dep69);
        depMap.put("70", R.drawable.dep70);
        depMap.put("71", R.drawable.dep71);
        depMap.put("72", R.drawable.dep72);
        depMap.put("73", R.drawable.dep73);
        depMap.put("74", R.drawable.dep74);
        depMap.put("75", R.drawable.dep75);
        depMap.put("76", R.drawable.dep76);
        depMap.put("77", R.drawable.dep77);
        depMap.put("78", R.drawable.dep78);
        depMap.put("ALIM", R.drawable.dep_alim);
        return depMap;
    }

    public static int getIconByDepartmentCode(String depCode) {
        return depMap.containsKey(depCode) ? depMap.get(depCode) : R.drawable.dep_default;
    }
}
