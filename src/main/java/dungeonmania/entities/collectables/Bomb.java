package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.logics.LogicItem;
import dungeonmania.entities.logics.Trigger;
import dungeonmania.map.GameMap;

public class Bomb extends LogicItem implements InventoryItem {
    public enum State {
        SPAWNED, INVENTORY, PLACED
    }

    public static final int DEFAULT_RADIUS = 1;
    private State state;
    private int radius;

    private List<Trigger> subs = new ArrayList<>();

    public Bomb(Position position, int radius, String logic) {
        super(position, logic);
        state = State.SPAWNED;
        this.radius = radius;
    }

    @Override
    public void notify(GameMap map) {
        if (getLogic() == null) {
            if (isActivatedBySwitch()) {
                explode(map);
            }

        } else {
            if (isLogicSatisfied()) {
                explode(map);
            }
        }
    }

    private boolean isActivatedBySwitch() {
        return getTriggers().stream().filter(trigger -> (trigger instanceof Switch)).map(Switch.class::cast)
                .filter(s -> s.isActivated()).count() >= 1;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (state != State.SPAWNED)
            return;
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(this))
                return;
            subs.stream().forEach(s -> s.unsubscribe(this));
            map.destroyEntity(this);
        }
        this.state = State.INVENTORY;
    }

    public void onPutDown(GameMap map, Position p) {
        translate(Position.calculatePositionBetween(getPosition(), p));
        map.addEntity(this);
        this.state = State.PLACED;
        List<Position> adjPosList = getPosition().getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof Trigger))
                    .collect(Collectors.toList());
            entities.stream().map(Trigger.class::cast).forEach(s -> s.subscribe(this));
            entities.stream().map(Trigger.class::cast).forEach(s -> this.subscribe(s));
        });
    }

    public void explode(GameMap map) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                List<Entity> entities = map.getEntities(new Position(i, j));
                entities = entities.stream().filter(e -> !(e instanceof Player)).collect(Collectors.toList());
                for (Entity e : entities)
                    map.destroyEntity(e);
            }
        }
    }

}
