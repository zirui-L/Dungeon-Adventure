package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.inventory.InventoryItem;

public class MidnightArmour extends Buildable implements BattleItem {
    public static final double SHIELD_DAMAGE_MAGNIFIER = 1;
    public static final double SHIELD_DAMAGE_REDUCER = 1;
    public static final double DEFAULT_HEALTH = 0;

    private double attack;
    private double defence;

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.attack = attack;
        this.defence = defence;
    }

        public static boolean checkBuildCriteria(Player player, boolean remove, List<InventoryItem> items) {
        List<SunStone> sunstone = getEntities(SunStone.class, items);
        List<Sword> sword = getEntities(Sword.class, items);
        if (sword.size() >= 1 && sunstone.size() >= 1 && player.getZombieNumber() == 0) {
            if (remove) {
                items.remove(sword.get(0));
                items.remove(sunstone.get(0));
            }
            return true;
        }
        return false;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(DEFAULT_HEALTH, attack, defence,
                SHIELD_DAMAGE_MAGNIFIER, SHIELD_DAMAGE_REDUCER));
    }

    @Override
    public void use(Game game) {
        return;
    }

    @Override
    public int getDurability() {
        return -1;
    }
}
