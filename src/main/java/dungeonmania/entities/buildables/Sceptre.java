package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class Sceptre extends Buildable {
    public static final double SCEPTRE_DAMAGE_MAGNIFIER = 1;
    public static final double SCEPTRE_DAMAGE_REDUCER = 1;
    public static final double DEFAULT_HEALTH = 0;
    public static final double DEFAULT_ATTACK = 0;
    public static final double DEFAULT_DEFENCE = 0;

    private int duration;

    public Sceptre(int duration) {
        super(null);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public static boolean checkBuildCriteria(boolean remove, List<InventoryItem> items) {
        List<Wood> wood = getEntities(Wood.class, items);
        List<Arrow> arrows = getEntities(Arrow.class, items);
        List<Treasure> treasure = getEntities(Treasure.class, items);
        List<Key> keys = getEntities(Key.class, items);
        List<SunStone> sunstone = getEntities(SunStone.class, items);
        if ((wood.size() >= 1 || arrows.size() >= 2)
            && sunstone.size() >= 1
            && (sunstone.size() >= 2 || treasure.size() >= 1 || keys.size() >= 1)) {
            if (remove) {
                if (wood.size() >= 1) {
                    items.remove(wood.get(0));
                } else if (arrows.size() >= 2) {
                    items.remove(arrows.get(0));
                    items.remove(arrows.get(1));
                }
                items.remove(sunstone.get(0));
                if (sunstone.size() >= 2) {
                    return true;
                }
                if (treasure.size() >= 1) {
                    items.remove(treasure.get(0));
                } else if (keys.size() >= 1) {
                    items.remove(keys.get(0));
                }
            }
            return true;
        }
        return false;
    }
}
