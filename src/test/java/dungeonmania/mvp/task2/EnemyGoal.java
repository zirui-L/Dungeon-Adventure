package dungeonmania.mvp.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class EnemyGoal {
    @Test
    @Tag("2-1-1")
    @DisplayName("Test achieving a basic enemy goal")
    public void enemyBasicGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_basicGoalTest_enemy", "c_basicGoalTest_enemy");

        DungeonResponse postBattleResponse = controller.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(postBattleResponse).contains(":enemies"));

        postBattleResponse = controller.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(postBattleResponse));

    }

    @Test
    @Tag("2-1-2")
    @DisplayName("Test destroy spawner")
    public void enemyBasicGoalSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse res = controller.newGame("d_basicGoalTest_enemySpawner", "c_basicGoalTest_enemy");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        DungeonResponse postBattleResponse = controller.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(postBattleResponse).contains(":enemies"));

        postBattleResponse = controller.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(postBattleResponse).contains(":enemies"));
        postBattleResponse = controller.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(postBattleResponse).contains(":enemies"));
        postBattleResponse = controller.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(postBattleResponse).contains(":enemies"));
        postBattleResponse = assertDoesNotThrow(() -> controller.interact(spawnerId));
        assertEquals("", TestUtils.getGoals(postBattleResponse));
    }

    @Test
    @Tag("2-1-3")
    @DisplayName("Testing a map with 4 conjunction goal")
    public void andAll() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalTest_andFive", "c_complexGoalsTest_andAll");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("2-1-4")
    @DisplayName("Testing that the exit goal must be achieved last in EXIT and ENEMIES")
    public void exitAndTreasureOrder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_exitAndEnemy", "c_complexGoalsTest_exitAndEnemy");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // move player onto exit
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);

        // don't check state of exit goal in string
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to destroy spawner
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.interact(spawnerId));

        // assert treasure goal met, but goal string is not empty
        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertNotEquals("", TestUtils.getGoals(res));

        // move player back onto exit
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

}
