package dungeonmania;

import java.util.List;

import org.json.JSONException;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.map.GameMap;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

/**
 * DO NOT CHANGE METHOD SIGNITURES OF THIS FILE
 * */
public class DungeonManiaController {
    private Game game = null;

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }

        if (!configs().contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        try {
            GameBuilder builder = new GameBuilder();
            game = builder.setConfigName(configName).setDungeonName(dungeonName).buildGame();
            return ResponseBuilder.getDungeonResponse(game);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        return null;
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        Player player = game.getPlayer();
        Inventory inventory = player.getInventory();
        Entity itemUsed = inventory.getEntity(itemUsedId);
        if (itemUsed == null) {
            throw new InvalidActionException("itemUsed is not in the player's inventory");
        }
        if (!(itemUsed instanceof Bomb || itemUsed instanceof InvincibilityPotion
                                        || itemUsed instanceof InvisibilityPotion)) {
            throw new IllegalArgumentException("ItemUsed is not a bomb, invincibility potion, "
                                                                        + "or an invisibility potion");
        }
        return ResponseBuilder.getDungeonResponse(game.tick(itemUsedId));
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        return ResponseBuilder.getDungeonResponse(game.tick(movementDirection));
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        List<String> validBuildables = List.of("bow", "shield", "midnight_armour", "sceptre");
        if (!validBuildables.contains(buildable)) {
            throw new IllegalArgumentException("Only bow, shield, midnight_armour and sceptre can be built");
        }
        GameMap map = game.getMap();

        if (buildable.equals("midnight_armour") && map.getEntities(ZombieToast.class).size() != 0) {
            throw new InvalidActionException("There are zombies currently in the dungeon");
        }

        return ResponseBuilder.getDungeonResponse(game.build(buildable));
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        GameMap map = game.getMap();
        Entity entity = map.getEntity(entityId);
        if (entity == null) {
            throw new IllegalArgumentException("Entity is not a valid Entity");
        }

        Player player = game.getPlayer();
        Position entityPosition = entity.getPosition();
        if (entity instanceof ZombieToastSpawner) {
            if (!entityPosition.isCardinallyAdjacent(player.getPosition())) {
                throw new InvalidActionException("The player is not cardinally adjacent to the spawner");
            }
            if (player.getWeapon() == null) {
                throw new InvalidActionException("The player does not have a weapon");
            }
        } else if (entity instanceof Mercenary) {
            Inventory inventory = player.getInventory();
            if (inventory.getEntities(Sceptre.class).size() == 0) {
                if (!entityPosition.isWithinRadius(player.getPosition(), ((Mercenary) entity).getBribeRadius())) {
                    throw new InvalidActionException("The player is not within specified bribing "
                                                                                    + "radius to the mercenary");
                }
                if (((Mercenary) entity).getBribeAmount()
                    > (inventory.getEntities(Treasure.class).size() - inventory.getEntities(SunStone.class).size())) {
                    throw new InvalidActionException("The player does not have enough gold to bribe the mercenary");
                }
            }
        }

        return ResponseBuilder.getDungeonResponse(game.interact(entityId));
    }

    /**
     * /game/new/generate
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName)
            throws IllegalArgumentException {
        return null;
    }

    /**
     * /game/rewind
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        return null;
    }

}
