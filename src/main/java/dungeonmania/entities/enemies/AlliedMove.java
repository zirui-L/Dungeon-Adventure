package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMove implements MoveMethod {
    public void nextPosition(Enemy enemy, Game game) {
        Mercenary mercenary = (Mercenary) enemy;
        GameMap map = game.getMap();
        Player player = game.getPlayer();

        boolean isAdjacentToPlayer = mercenary.isAdjacentToPlayer();

        Position nextPos;
        nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                : map.dijkstraPathFind(mercenary.getPosition(), player.getPosition(), enemy);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos))
            mercenary.setAdjacentToPlayer(true);

        map.moveTo(enemy, nextPos);
    }
}
