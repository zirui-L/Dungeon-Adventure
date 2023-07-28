package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;

    private MoveMethod moveMethod;

    private boolean isUnderMindControl = false;
    private int mindControlDuration;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
        this.moveMethod = new TowardsMove();
    }

    public boolean isAllied() {
        return allied;
    }

    public boolean isAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    public void setAdjacentToPlayer(boolean isAdjacentToPlayer) {
        this.isAdjacentToPlayer = isAdjacentToPlayer;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        boolean canBribe = bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount
                && player.isWithinRadius(getPosition(), bribeRadius);
        return canBribe || player.hasSceptre();
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }

    }

    private void mindControl(Player player, Sceptre sceptre) {
        isUnderMindControl = true;
        mindControlDuration = sceptre.getDuration();
    }

    public void updateControl() {
        if (!isUnderMindControl) {
            return;
        }
        if (mindControlDuration > 0) {
            mindControlDuration--;
            return;
        }
        allied = false;
        isUnderMindControl = false;
    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        if (player.hasSceptre()) {
            mindControl(player, player.getSceptre());
        } else {
            bribe(player);
            if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
                isAdjacentToPlayer = true;
            }
    }

    @Override
    public void move(Game game) {

        if (allied) {
            moveMethod = new AlliedMove();
        } else if (game.getEffectivePotion() instanceof InvisibilityPotion) {
            moveMethod = new RandomMove();
        } else if (game.getEffectivePotion() instanceof InvincibilityPotion) {
            moveMethod = new AwayMove();
        } else {
            moveMethod = new TowardsMove();
        }
        moveMethod.nextPosition(this, game);
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }
}
