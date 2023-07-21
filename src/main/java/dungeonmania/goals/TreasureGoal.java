package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal extends VariableNode {
    private static final String TREASURE = "treasure";
    private int target;

    public TreasureGoal(int target) {
        this.setType(TREASURE);
        this.target = target;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.getCollectedTreasureCount() >= target;
    }
}
