package dungeonmania.goals;

import dungeonmania.Game;

import dungeonmania.entities.Switch;

public class BouldersGoal extends VariableNode {
    private static final String BOULDERS = "boulders";

    public BouldersGoal() {
        this.setType(BOULDERS);
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;

        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }
}
