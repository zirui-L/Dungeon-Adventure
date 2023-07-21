package dungeonmania.goals;

import dungeonmania.Game;

public class EnemyGoal extends VariableNode {
    private static final String ENEMY = "enemies";
    private int target;

    public EnemyGoal(int target) {
        this.setType(ENEMY);
        this.target = target;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;

        return game.getKilledEnemyCount() >= target && game.getSpawnerNumber() == 0;
    }
}
