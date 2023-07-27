package dungeonmania.entities.logics;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicItem extends Triggerble {
    private String logic;

    public LogicItem(Position position, String logic) {
        super(position);
        this.logic = logic;
    }

    protected boolean isLogicSatisfied() {
        switch (logic) {
        case "and":
            return activeTriggers() >= 2 && allTriggersActivated();
        case "or":
            return activeTriggers() >= 1;
        case "xor":
            return activeTriggers() == 1;
        case "co_and":
            return compareAcitveTriggers();
        default:
            break;
        }

        return false;
    }

    public void notify(GameMap map) {
        setActivated(isLogicSatisfied());
    }

    public String getLogic() {
        return logic;
    }

    public boolean compareAcitveTriggers() {
        return isActivatedSameTick() && allTriggersActivated() && activeTriggers() >= 2;
    }

    @Override
    public void onDestroy(GameMap map) {
        List<Trigger> triggers = this.getTriggers();

        triggers.stream().forEach(trigger -> trigger.unsubscribe(this));

        this.setTriggers(new ArrayList<>());

    }

    private boolean isActivatedSameTick() {
        return this.getTriggers().stream().map(trigger -> trigger.getActivatedTick()).distinct().count() == 1;
    }

}
