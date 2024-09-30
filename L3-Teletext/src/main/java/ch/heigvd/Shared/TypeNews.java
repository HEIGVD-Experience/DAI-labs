package ch.heigvd.Shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe conteant des contneurs de données partagés dans les différents fichiers
 */
public class TypeNews {

    // Dictionnaire contenant les IP multicast par type de news
    public static Map<String, String> mapTypeIP = new HashMap<String, String>() {{
        put("WEATHER", "239.0.0.1");
        put("HEIG"   , "239.0.0.2");
        put("POLITIC", "239.0.0.3");
        put("SPORT"  , "239.0.0.4");
    }};

    // Liste contenant les types de commandes de news disponible
    public static List<String> newsListUserChoice = new ArrayList<>() {{
        for (String key : TypeNews.mapTypeIP.keySet()) {
            add(key);
        }
        add("BREAKING");
        add("LATEST");
    }};
}