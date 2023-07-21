package dungeonmania.goals;

import dungeonmania.Game;

public abstract class CompoundNode implements Goal {
    private String type;

    private Goal left;
    private Goal right;

    public CompoundNode(Goal left, Goal right) {
        this.left = left;
        this.right = right;
    }

    public Goal getLeft() {
        return left;
    }

    public Goal getRight() {
        return right;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return "(" + left.toString(game) + " " + type + " " + right.toString(game) + ")";
    }

}
