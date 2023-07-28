package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

import java.util.List;
import java.util.UUID;

public abstract class Entity {
    public static final int FLOOR_LAYER = 0;
    public static final int ITEM_LAYER = 1;
    public static final int DOOR_LAYER = 2;
    public static final int CHARACTER_LAYER = 3;

    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private String entityId;

    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    public void onOverlap(GameMap map, Entity entity) {
        return;
    }

    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    public void onDestroy(GameMap gameMap) {
        return;
    }

    public Position getPosition() {
        return position;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public String getId() {
        return entityId;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public List<Position> getCardinallyAdjacentPositions() {
        return position.getCardinallyAdjacentPositions();
    }

    public List<Position> getAdjacentPositions() {
        return position.getAdjacentPositions();
    }
    public boolean isWithinRadius(Position other, int radius) {
        return position.isWithinRadius(other, radius);
    }
}
