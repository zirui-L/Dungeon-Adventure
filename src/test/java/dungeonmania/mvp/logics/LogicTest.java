package dungeonmania.mvp.logics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicTest {
    @Test
    @Tag("3-1-1")
    @DisplayName("Test walking onto a wire")
    public void playerCanWalkOnWire() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_lightBulb", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        Position playerPos = TestUtils.getEntities(res, "player").get(0).getPosition();

        assertTrue(TestUtils.entityAtPosition(res, "wire", playerPos));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        playerPos = TestUtils.getEntities(res, "player").get(0).getPosition();

        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_off", playerPos));
    }

    @Test
    @Tag("3-1-2")
    @DisplayName("Test turning on a light bulb")
    public void turnOnLight() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_lightBulb", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // light with or condition and 1 source is lit up, light with and condition and 2 source lit up

        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(2, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("3-1-3")
    @DisplayName("Test turning on a light bulb - xor")
    public void turnOnLightXOR() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_lightBulbXor", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // xor light with 1 conduct lights up, else does not light up

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("3-1-4")
    @DisplayName("Test turning on a light bulb - and")
    public void turnOnLightAnd() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_lightBulbAnd", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // light with and condition and 1 source is off, light with and condition and 2 source is on

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("3-1-5")
    @DisplayName("Test turning on a light bulb - co_and")
    public void turnOnLightCoAnd() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_lightBulbCoand", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // light with co_and condition and 1 source is off, light with co_and condition and 2 source is on

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

        // move to the second switch
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // light with 2 sources activated in the different tick remains off
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("3-1-6")
    @DisplayName("Test open a switch door - xor")
    public void openDoorXOR() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_switchDoorXor", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // xor light with 1 conduct lights up, else does not light up

        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        assertEquals(1, TestUtils.getEntities(res, "switch_door_open").size());

        // move to the switch door and see whether it can be overlapped
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        Position playerPos = TestUtils.getEntities(res, "player").get(0).getPosition();

        assertTrue(TestUtils.entityAtPosition(res, "switch_door_open", playerPos));

        // Move to the second switch door, it remains closed

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        playerPos = TestUtils.getEntities(res, "player").get(0).getPosition();

        assertFalse(TestUtils.entityAtPosition(res, "switch_door_open", playerPos));
    }

    @Test
    @Tag("3-1-6")
    @DisplayName("Test open a switch door - coand")
    public void openDoorCoand() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_switchDoorCoand", "c_bombTest_placeCardinallyActive");

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // door with co_and condition and 1 source is off, light with co_and condition and 2 source is on

        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        assertEquals(1, TestUtils.getEntities(res, "switch_door_open").size());

        // move to the second switch
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // door with 2 sources activated in the different tick remains off
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        assertEquals(1, TestUtils.getEntities(res, "switch_door_open").size());
    }

    @Test
    @Tag("3-1")
    @DisplayName("Test explode bomb via a wire")
    public void explodeViaWire() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_wireBomb", "c_bombTest_placeCardinallyActive");

        assertEquals(2, TestUtils.getEntities(res, "bomb").size());
        assertEquals(9, TestUtils.getEntities(res, "wire").size());

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // a logic bomb with or statement, and non-logic bomb (non-logic bomb can only be activated next to a switch)
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        assertEquals(8, TestUtils.getEntities(res, "wire").size());
    }

    @Test
    @Tag("3-1-6")
    @DisplayName("Test activate bomb - coand")
    public void explodeBombCoand() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logics_bombCoand", "c_bombTest_placeCardinallyActive");

        assertEquals(2, TestUtils.getEntities(res, "bomb").size());
        assertEquals(11, TestUtils.getEntities(res, "wire").size());

        // Move onto a switch
        res = dmc.tick(Direction.DOWN);

        // bomb with co_and condition and 1 source is off, bomb with co_and condition and 2 source is on
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        assertEquals(8, TestUtils.getEntities(res, "wire").size());

        // move to the second switch
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // bomb with 2 sources activated in the different tick remains off
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        assertEquals(8, TestUtils.getEntities(res, "wire").size());

    }
}
