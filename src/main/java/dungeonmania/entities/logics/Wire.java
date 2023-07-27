package dungeonmania.entities.logics;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Triggerble implements Trigger {
    private List<Triggerble> subs = new ArrayList<>();
    private boolean notified = false;

    private int activatedTick;
    private boolean previouslyActivated;

    public Wire(Position position) {
        super(position);

    }

    public void subscribe(Triggerble item) {
        subs.add(item);
    }

    public void unsubscribe(Triggerble item) {
        subs.remove(item);
    }

    @Override
    public void notify(GameMap map) {
        // If notified during this tick, return to aviod infinite loop
        if (this.notified) {
            return;
        }

        this.notified = true;

        setActivated(activeTriggers() >= 1);

        // notify all the surrounding triggerbles
        subs.stream().filter(triggerble -> triggerble instanceof Wire).forEach(triggerble -> triggerble.notify(map));

    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onDestroy(GameMap map) {
        List<Trigger> triggers = this.getTriggers();

        triggers.stream().forEach(trigger -> trigger.unsubscribe(this));
        subs.stream().forEach(triggerble -> triggerble.unsubscribe(this));
    }

    public int getActivatedTick() {
        return activatedTick;
    }

    public void setActivatedTick(int activatedTick) {
        this.activatedTick = activatedTick;
    }

    public boolean isPreviouslyActivated() {
        return previouslyActivated;
    }

    public void setPreviouslyActivated(boolean previouslyActivated) {
        this.previouslyActivated = previouslyActivated;
    }
}
