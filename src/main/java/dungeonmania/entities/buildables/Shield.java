package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class Shield extends Buildable implements BattleItem {
    public static final double SHIELD_DAMAGE_MAGNIFIER = 1;
    public static final double SHIELD_DAMAGE_REDUCER = 1;
    public static final double DEFAULT_HEALTH = 0;
    public static final double DEFAULT_ATTACK = 0;

    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    public static boolean checkBuildCriteria(boolean remove, List<InventoryItem> items) {
        List<Wood> wood = getEntities(Wood.class, items);
        List<Treasure> treasure = getEntities(Treasure.class, items);
        List<Key> keys = getEntities(Key.class, items);
        List<SunStone> sunstone = getEntities(SunStone.class, items);
        if (wood.size() >= 2 && (treasure.size() >= 1 || keys.size() >= 1 || sunstone.size() >= 1)) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(wood.get(1));
                if (sunstone.size() >= 1) {
                    return true;
                }
                if (treasure.size() >= 1) {
                    items.remove(treasure.get(0));
                } else {
                    items.remove(keys.get(0));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(DEFAULT_HEALTH, DEFAULT_ATTACK, defence,
                SHIELD_DAMAGE_MAGNIFIER, SHIELD_DAMAGE_REDUCER));
    }

    @Override
    public int getDurability() {
        return durability;
    }

}
