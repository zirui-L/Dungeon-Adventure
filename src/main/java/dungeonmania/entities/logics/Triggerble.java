package dungeonmania.entities.logics;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Triggerble extends Activatable {
    private List<Trigger> triggers = new ArrayList<>();

    public Triggerble(Position position) {
        super(position);
    }

    public void subscribe(Trigger item) {
        this.triggers.add(item);
    }

    public void unsubscribe(Trigger item) {
        this.triggers.remove(item);
    }

    public abstract void notify(GameMap map);

    public List<Trigger> getTriggers() {
        return triggers;
    }

    protected int activeTriggers() {
        return (int) getTriggers().stream().filter(trigger -> ((Activatable) trigger).isActivated()).count();
    }

    protected boolean allTriggersActivated() {
        return activeTriggers() == triggers.size();
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }
}
