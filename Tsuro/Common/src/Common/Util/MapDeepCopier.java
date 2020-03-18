package src.Common.Util;

import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Posn;
import src.Common.Tiles;

import java.util.HashMap;
import java.util.Map;

public class MapDeepCopier {

    public static <T, U> Map<T, U> copy(Map<T, U> map) {
        Map<T, U> mapCopy = new HashMap<>();
        for (T key : map.keySet()) {
            mapCopy.put(copy(key), copy(map.get(key)));
        }
        return mapCopy;
    }

    /**
     * Handle the possible keys and values we are supporting.
     * Must be one of:
     * <ul>
     *     <li>AvatarColor</li>
     *     <li>AvatarLocation</li>
     *     <li>Posn</li>
     *     <li>Tiles</li>
     * </ul>
     * @param item item to copy
     * @return copy of item (deep if needed based on the data structure)
     */
    private static <R> R copy(R item) {
        if(item instanceof AvatarColor) {
            return item;
        }
        else if (item instanceof AvatarLocation) {
            AvatarLocation loc = (AvatarLocation) item;
            return (R) loc.clone();
        }
        else if (item instanceof Posn) {
            Posn posn = (Posn) item;
            return (R) posn.clone();
        }
        else if (item instanceof Tiles) {
            Tiles tile = (Tiles) item;
            return (R) tile.clone();
        }
        else {
            return item;
        }
    }
}
