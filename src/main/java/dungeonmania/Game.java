package dungeonmania;

import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Game {
    private String id;
    private String name;
    private Goal goals;
    private GameMap map;
    private Player player;
    private BattleFacade battleFacade;
    private EntityFactory entityFactory;
    private boolean isInTick = false;
    public static final int PLAYER_MOVEMENT = 0;
    public static final int PLAYER_MOVEMENT_CALLBACK = 1;
    public static final int AI_MOVEMENT = 2;
    public static final int AI_MOVEMENT_CALLBACK = 3;
    public static final int ITEM_LONGEVITY_UPDATE = 4;
    public static final int LOGIC_ENTITY_UPDATE = 5;
    public static final int SWITCH_UPDATE = 6;
    public static final int SET_TRIGGER_ACTIVATED_TICK = 7;
    public static final int SET_TRIGGER_PREV_ACTIVATED = 8;
    public static final int NOTIFY_LOGIC_ITEMS = 9;
    public static final int MERCENARY_UPDATE = 10;
    public static final int ZOMBIE_NUMBER_UPDATE = 11;

    private ComparableCallback currentAction = null;

    private int tickCount = 0;
    private PriorityQueue<ComparableCallback> sub = new PriorityQueue<>();
    private PriorityQueue<ComparableCallback> addingSub = new PriorityQueue<>();

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        player = map.getPlayer();
        register(() -> player.onTick(tickCount), PLAYER_MOVEMENT, "potionQueue");
        register(() -> {
            List<Mercenary> mercenaries = map.getEntities(Mercenary.class);
            mercenaries.forEach(mercenary -> mercenary.updateControl());
        }, MERCENARY_UPDATE, "mercenaryUpdate");
        register(() -> player.setZombieNumber(map.getEntities(ZombieToast.class).size()), ZOMBIE_NUMBER_UPDATE,
                                                                                                    "mercenaryUpdate");
    }

    public Game tick(Direction movementDirection) {
        registerOnce(() -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        tick();
        return this;
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion)
                player.use((Potion) item, tickCount);
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    public void battle(Player player, Enemy enemy) {
        battleFacade.battle(this, player, enemy);
        if (player.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getBattleStatistics().getHealth() <= 0) {
            map.destroyEntity(enemy);
            player.incrementKilledEnemyCount();
        }
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables();
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }
        registerOnce(() -> player.build(buildable, entityFactory), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(() -> ((Interactable) e).interact(player, this), PLAYER_MOVEMENT, "playerInteracts");
        tick();
        return this;
    }

    public void register(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(Runnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        if (this.currentAction != null && id.equals(this.currentAction.getId())) {
            this.currentAction.invalidate();
        }

        for (ComparableCallback c : sub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
        for (ComparableCallback c : addingSub) {
            if (id.equals(c.getId())) {
                c.invalidate();
            }
        }
    }

    public int tick() {
        PriorityQueue<ComparableCallback> nextTickSub = new PriorityQueue<>();
        isInTick = true;
        while (!sub.isEmpty()) {
            currentAction = sub.poll();
            currentAction.run();
            if (currentAction.isValid()) {
                nextTickSub.add(currentAction);
            }
        }
        isInTick = false;
        nextTickSub.addAll(addingSub);
        addingSub = new PriorityQueue<>();
        sub = nextTickSub;
        tickCount++;
        return tickCount;
    }

    public int getTick() {
        return this.tickCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public int getCollectedTreasureCount() {
        return player.getCollectedTreasureCount();
    }

    public int getKilledEnemyCount() {
        return player.getKilledEnemyCount();
    }

    public int getSpawnerNumber() {
        return map.getSpawnerNumber();
    }

    public Player getPlayer() {
        return player;
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public int getTickCount() {
        return tickCount;
    }

    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return map.getEntities(type);
    }

    public List<Entity> getEntities(Position p) {
        return map.getEntities(p);
    }

    public void remove(InventoryItem inventoryItem) {
        player.remove(inventoryItem);
    }

    public Potion getEffectivePotion() {
        return player.getEffectivePotion();
    }

    public boolean canMoveTo(Entity entity, Position position) {
        return map.canMoveTo(entity, position);
    }

    public void moveTo(Entity entity, Position position) {
        map.moveTo(entity, position);
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public Position dijkstraPathFind(Position src, Position dest, Entity entity) {
        return map.dijkstraPathFind(src, dest, entity);
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        entityFactory.spawnZombie(game, spawner);
    }

    public void destroyEntity(Entity entity) {
        map.destroyEntity(entity);
    }

    public void addEntity(Entity entity) {
        map.addEntity(entity);
    }

    public Entity getUsedEntity(String itemUsedId) {
        return player.getEntity(itemUsedId);
    }

    public Entity getEntity(String id) {
        return map.getEntity(id);
    }
    public BattleItem getWeapon() {
        return player.getWeapon();
    }

    public <T> List<T> getInventoryEntities(Class<T> clz) {
        return player.getEntities(clz);
    }

    public Position getEntityPosition(String id) {
        return getEntity(id).getPosition();
    }

    public boolean isCardinallyAdjacent(Position a, Position b) {
        return a.isCardinallyAdjacent(b);
    }

    public boolean isWithinRadius(Position a, Position b, int radius) {
        return a.isWithinRadius(b, radius);
    }
}
