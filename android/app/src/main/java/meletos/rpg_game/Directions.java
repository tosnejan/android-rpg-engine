package meletos.rpg_game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Directions {
    LEFT, RIGHT, DOWN, UP, DOWNLEFT, DOWNRIGHT, UPLEFT, UPRIGHT;

    private static final List<Directions> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Directions randomDirection()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
