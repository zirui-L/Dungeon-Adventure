package dungeonmania.goals;

import dungeonmania.Game;

public class AndNode extends CompoundNode {
    public AndNode(Goal left, Goal right) {
        super(left, right);
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return this.getLeft().achieved(game) && this.getRight().achieved(game);
    }
}
