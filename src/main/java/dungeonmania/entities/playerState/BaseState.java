package dungeonmania.entities.playerState;

import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;

public class BaseState extends PlayerState {
    @Override
    public PlayerState changeState() {
        return this;
    }

    @Override
    public PlayerState changeState(Potion potion, int duration) {
        if (potion instanceof InvincibilityPotion) {
            return new InvincibleState(duration);
        }
        return new InvisibleState(duration);
    }
}
