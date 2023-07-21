package dungeonmania.entities.playerState;

public class InvisibleState extends PlayerState {
    private int duration;

    public InvisibleState(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean ifNextTrigger() {
        if (duration > 0) {
            duration--;
            return false;
        }
        return true;
    }
}
