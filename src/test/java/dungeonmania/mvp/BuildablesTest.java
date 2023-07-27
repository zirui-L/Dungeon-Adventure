package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildablesTest {
    @Test
    @Tag("5-1")
    @DisplayName("Test IllegalArgumentException is raised when attempting to build an unknown entity - sword")
    public void buildSwordIllegalArgumentException() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BuildablesTest_BuildSwordIllegalArgumentException",
                "c_BuildablesTest_BuildSwordIllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> dmc.build("sword"));
    }

    @Test
    @Tag("5-2")
    @DisplayName("Test InvalidActionException is raised when the player "
            + "does not have sufficient items to build a bow or shield")
    public void buildInvalidActionException() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BuildablesTest_BuildInvalidArgumentException", "c_BuildablesTest_BuildInvalidArgumentException");
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @Tag("5-4")
    @DisplayName("Test building a shield with a key")
    public void buildShieldWithKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithKey", "c_BuildablesTest_BuildShieldWithKey");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
    }

    @Test
    @Tag("5-5")
    @DisplayName("Test building a shield with treasure")
    public void buildShieldWithTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithTreasure",
                "c_BuildablesTest_BuildShieldWithTreasure");
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("5-6")
    @DisplayName("Test building a shield with sun stone")
    public void buildShieldWithSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithSunStone",
                "c_BuildablesTest_BuildShieldWithSunStone");
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Wood used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        // Sun Stone used in construction remain in inventory
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-7")
    @DisplayName("Test building a Sceptred with a key, a wood, and a sun stone")
    public void buildSceptredWithWoodAndKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptredWithWoodAndKey",
                                            "c_BuildablesTest_BuildSceptredWithWoodAndKey");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-8")
    @DisplayName("Test building a Sceptred with arrows, treasure (not sunstone), and a sun stone")
    public void buildSceptredWithArrowAndTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptredWithArrowAndTreasure",
                                            "c_BuildablesTest_BuildSceptredWithArrowAndTreasure");

        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up arrows x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptred
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-9")
    @DisplayName("Test building a Sceptred with wood, and two sun stones")
    public void buildSceptredWithWoodAndTwoSunStones() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptredWithWoodAndTwoSunStones",
                                        "c_BuildablesTest_BuildSceptredWithWoodAndTwoSunStones");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up sun stone 1
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sun Stone 2
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptred
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        // Sunstone used as treasure/key is remained
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-10")
    @DisplayName("Test building a Midnight Armour with sword and a sun stone")
    public void buildMidnightArmourWithSwordAndSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildMidnightArmourWithSwordAndSunStone",
                                            "c_BuildablesTest_BuildMidnightArmourWithSwordAndSunStone");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-11")
    @DisplayName("Test InvalidActionException is raised when the player "
            + "building a Midnight Armour while zombies exist")
    public void buildInvalidActionExceptionMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildInvalidActionExceptionMidnightArmour",
                                        "c_BuildablesTest_BuildInvalidActionExceptionMidnightArmour");
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        assertEquals(1, getZombies(res).size());

        // Build Midnight Armour and recieve exception
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @Tag("5-12")
    @DisplayName("Test responsse buildables parameter is a list of buildables that the player can currently build")
    public void dungeonResponseBuildables() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_DungeonResponseBuildables",
                "c_BuildablesTest_DungeonResponseBuildables");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // Gather entities to build bow
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Bow added to buildables list
        buildables.add("bow");
        assertEquals(buildables, res.getBuildables());

        // Gather entities to build shield
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Shield added to buildables list
        buildables.add("shield");
        assertEquals(buildables.size(), res.getBuildables().size());

        // Gather entities to build Sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Sceptre added to buildables list
        buildables.add("sceptre");
        assertEquals(buildables.size(), res.getBuildables().size());

        // Gather entities to build Midnight Armour
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Sceptre added to buildables list
        buildables.add("midnight_armour");
        assertEquals(buildables.size(), res.getBuildables().size());
        assertTrue(buildables.containsAll(res.getBuildables()));
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build bow
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // Bow disappears from buildables list
        buildables.remove("bow");
        assertEquals(buildables, res.getBuildables());

        // Build shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Shield disappears from buildables list
        buildables.remove("shield");
        assertEquals(buildables, res.getBuildables());

        // Build Sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Sceptre disappears from buildables list
        buildables.remove("sceptre");
        assertEquals(buildables, res.getBuildables());

        // Build Midnight Armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Midnight Armour disappears from buildables list
        buildables.remove("midnight_armour");
        assertEquals(buildables, res.getBuildables());
    }

    private List<EntityResponse> getZombies(DungeonResponse res) {
        return TestUtils.getEntities(res, "zombie_toast");
    }
}
