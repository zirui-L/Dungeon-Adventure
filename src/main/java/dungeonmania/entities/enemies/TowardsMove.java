package dungeonmania.entities.enemies;

import dungeonmania.Game;

public class TowardsMove implements MoveMethod {
    public void nextPosition(Enemy enemy, Game game) {

        game.moveTo(enemy, game.dijkstraPathFind(enemy.getPosition(), game.getPlayerPosition(), enemy));
    }
}
