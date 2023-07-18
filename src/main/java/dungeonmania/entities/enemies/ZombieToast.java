package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    private MoveMethod moveMethod;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
        this.moveMethod = new RandomMove();
    }

    @Override
    public void move(Game game) {
        GameMap map = game.getMap();
        if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            moveMethod = new AwayMove();
        } else {
            moveMethod = new RandomMove();
        }
        moveMethod.nextPosition(this, game);
    }

}
