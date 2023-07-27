package dungeonmania.entities.logics;

import dungeonmania.util.Position;

public interface Trigger {
    public void subscribe(Triggerble item);

    public void unsubscribe(Triggerble item);

    public int getActivatedTick();

    public void setActivatedTick(int activatedTick);

    public Position getPosition();

    public String getId();

    public boolean isActivated();

    public boolean isPreviouslyActivated();

    public void setPreviouslyActivated(boolean previouslyActivated);
}
