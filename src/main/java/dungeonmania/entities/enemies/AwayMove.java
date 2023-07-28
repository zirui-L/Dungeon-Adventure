package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class AwayMove implements MoveMethod {
    public void nextPosition(Enemy enemy, Game game) {
        GameMap map = game.getMap();
        Position enemyPosition = enemy.getPosition();

        Position plrDiff = Position.calculatePositionBetween(map.getPosition(), enemyPosition);

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemyPosition, Direction.RIGHT)
                : Position.translateBy(enemyPosition, Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemyPosition, Direction.UP)
                : Position.translateBy(enemyPosition, Direction.DOWN);
        Position offset = enemyPosition;
        if (plrDiff.getY() == 0 && map.canMoveTo(enemy, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(enemy, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else
                offset = enemyPosition;
        } else {
            if (map.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (map.canMoveTo(enemy, moveX))
                offset = moveX;
            else
                offset = enemyPosition;
        }

        map.moveTo(enemy, offset);
    }
}
