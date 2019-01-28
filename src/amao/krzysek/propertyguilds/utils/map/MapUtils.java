package amao.krzysek.propertyguilds.utils.map;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MapUtils {

    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortValue(LinkedHashMap<K, V> map) {
        return map.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder())).limit(10).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
