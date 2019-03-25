package meletos.rpg_game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Directions {
    NONE(0), LEFT(1), RIGHT(2), DOWN(3), UP(4), DOWNLEFT(5), DOWNRIGHT(6), UPLEFT(7), UPRIGHT(8);

    private static final List<Directions> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final int ID;

    Directions(int ID) {
        this.ID = ID;
    }

    public static Directions randomDirection()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
