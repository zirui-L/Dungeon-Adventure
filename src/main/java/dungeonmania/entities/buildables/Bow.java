package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.InventoryItem;

public class Bow extends Buildable implements BattleItem {
    public static final double BOW_DAMAGE_MAGNIFIER = 2;
    public static final double BOW_DAMAGE_REDUCER = 1;
    public static final double DEFAULT_HEALTH = 0;
    public static final double DEFAULT_ATTACK = 0;
    public static final double DEFAULT_DEFENCE = 0;

    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
    }

    public static boolean checkBuildCriteria(boolean remove, List<InventoryItem> items) {
        List<Wood> wood = getEntities(Wood.class, items);
        List<Arrow> arrows = getEntities(Arrow.class, items);
        if (wood.size() >= 1 && arrows.size() >= 3) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(arrows.get(0));
                items.remove(arrows.get(1));
                items.remove(arrows.get(2));
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
        return BattleStatistics.applyBuff(origin, new BattleStatistics(DEFAULT_HEALTH, DEFAULT_ATTACK, DEFAULT_DEFENCE,
                BOW_DAMAGE_MAGNIFIER, BOW_DAMAGE_REDUCER));
    }

    @Override
    public int getDurability() {
        return durability;
    }
}
