package meletos.rpg_game.file_io;

import java.io.Serializable;

/**
 * Wrapper class holding all the information about a level
 * Gets serialised into a file
 */
public class LevelRepresentation implements Serializable {
        private String lvlName;

        private class Character implements Serializable {
            private String name, type, graphicsFolder;
            private int x, y;
        }
    }
