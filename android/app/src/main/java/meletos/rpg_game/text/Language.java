package meletos.rpg_game.text;

public enum Language {
    ENG("text/eng.txt"), CZE("text/cze.txt");

    String filename;

    Language(String filename) {
        this.filename = filename;
    }
}
