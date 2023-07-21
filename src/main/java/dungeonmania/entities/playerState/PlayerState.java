package dungeonmania.entities.playerState;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;

public abstract class PlayerState {
    public PlayerState changeState() {
            return new BaseState();
    }

    public PlayerState changeState(Potion potion, int duration) {
        if (potion instanceof InvincibilityPotion) {
            return new InvincibleState(duration);
        }
        return new InvisibleState(duration);
    }


    public boolean ifNextTrigger() {
        return true;
    }
}
