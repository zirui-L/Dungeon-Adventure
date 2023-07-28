package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Exit;
import dungeonmania.util.Position;

public class ExitGoal extends VariableNode {
    private static final String EXIT = "exit";

    public ExitGoal() {
        this.setType(EXIT);
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;

        Position pos = game.getPlayerPosition();
        List<Exit> es = game.getEntities(Exit.class);
        if (es == null || es.size() == 0)
            return false;
        return es.stream().map(Entity::getPosition).anyMatch(pos::equals);
    }
}
