package dungeonmania.goals;

import dungeonmania.Game;

public class OrNode extends CompoundNode {
    public OrNode(Goal left, Goal right) {
        super(left, right);
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;

        return this.getLeft().achieved(game) || this.getRight().achieved(game);
    }
}
