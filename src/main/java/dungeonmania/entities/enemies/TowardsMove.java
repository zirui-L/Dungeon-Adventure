package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;

public class TowardsMove implements MoveMethod {
    public void nextPosition(Enemy enemy, Game game) {

        GameMap map = game.getMap();
        Player player = game.getPlayer();

        map.moveTo(enemy, map.dijkstraPathFind(enemy.getPosition(), player.getPosition(), enemy));
    }
}
