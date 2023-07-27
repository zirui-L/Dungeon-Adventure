package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        int sunStone = count(SunStone.class);
        int sword = count(Sword.class);
        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (treasure >= 1 || keys >= 1)) {
            result.add("shield");
        }
        if ((wood >= 1 || arrows >= 2) && sunStone >= 1
        && (sunStone >= 2 || treasure >= 1 || keys >= 1)) {
            result.add("sceptre");
        }
        if (sword >= 1 && sunStone >= 1) {
            result.add("midnight_armour");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, String itemString, EntityFactory factory) {
        switch (itemString) {
            case "bow":
                if (Bow.checkBuildCriteria(remove, items)) return factory.buildBow();
                break;
            case "shield":
                if (Shield.checkBuildCriteria(remove, items)) return factory.buildShield();
                break;
            case "midnight_armour":
                if (MidnightArmour.checkBuildCriteria(p, remove, items)) return factory.buildMidnightArmour();
                break;
            case "sceptre":
                if (Sceptre.checkBuildCriteria(remove, items)) return factory.buildSceptre();
                break;
            default:
                break;
            }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

    public boolean hasSceptre() {
        return getFirst(Sceptre.class) != null;
    }

    public Sceptre getSceptre() {
        Sceptre sceptre = getFirst(Sceptre.class);
        return sceptre;
    }
}
