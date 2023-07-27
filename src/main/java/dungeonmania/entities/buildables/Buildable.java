package dungeonmania.entities.buildables;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem {
    public Buildable(Position position) {
        super(position);
    }

    public static <T> List<T> getEntities(Class<T> clz, List<InventoryItem> items) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }
}
