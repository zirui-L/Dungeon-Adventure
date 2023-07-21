package dungeonmania.goals;

import dungeonmania.Game;

public abstract class VariableNode implements Goal {
    private String type;

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";

        return ":" + type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
