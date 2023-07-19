package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Bow extends Buildable {
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
