package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.util.Position;

public class RandomMove implements MoveMethod {
    private Random randGen = new Random();

    public void nextPosition(Enemy enemy, Game game) {
        Position nextPos;
        List<Position> pos = enemy.getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> game.canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = enemy.getPosition();
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }

        game.moveTo(enemy, nextPos);
    }
}
