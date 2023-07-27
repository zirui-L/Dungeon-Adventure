package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.logics.Activatable;
import dungeonmania.entities.logics.Trigger;
import dungeonmania.entities.logics.Triggerble;
import dungeonmania.entities.logics.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Activatable implements Trigger {
    private boolean activated;

    private List<Triggerble> subs = new ArrayList<>();

    private int activatedTick;
    private boolean previouslyActivated;

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void subscribe(Triggerble item) {
        subs.add(item);
    }

    public void unsubscribe(Triggerble item) {
        subs.remove(item);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void notifyTriggerbles(GameMap map) {
        subs.stream().filter(triggerble -> triggerble instanceof Wire).forEach(triggerble -> triggerble.notify(map));
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
