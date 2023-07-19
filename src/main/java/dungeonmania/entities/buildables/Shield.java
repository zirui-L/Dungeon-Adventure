package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Shield extends Buildable {
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
