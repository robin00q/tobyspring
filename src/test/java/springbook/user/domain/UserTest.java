package springbook.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertEquals(level.nextLevel(), user.getLevel());
        }
    }

    @Test
    void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
            assertThrows(IllegalStateException.class, () -> user.upgradeLevel());
        }
    }

}