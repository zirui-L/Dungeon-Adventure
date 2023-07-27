package dungeonmania.entities.logics;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public abstract class Activatable extends Entity {
    private boolean activated;

    public Activatable(Position position) {
        super(position);

    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

}
