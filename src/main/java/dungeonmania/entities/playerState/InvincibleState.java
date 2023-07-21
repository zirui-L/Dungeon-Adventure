package dungeonmania.entities.playerState;

public class InvincibleState extends PlayerState {
    private int duration;

    public InvincibleState(int duration) {
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
