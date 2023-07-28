package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public class Door extends Entity {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && (hasSunStone((Player) entity) || hasKey((Player) entity)));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        Key key = player.getFirst(Key.class);

        if (hasSunStone(player)) {
            open();
        } else if (hasKey(player)) {
            player.remove(key);
            open();
        }
    }

    private boolean hasKey(Player player) {
        Key key = player.getFirst(Key.class);

        return (key != null && player.getKeyNumber() == number);
    }

    private boolean hasSunStone(Player player) {
        SunStone sunStone = player.getFirst(SunStone.class);
        return sunStone != null;
    }
    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

}
