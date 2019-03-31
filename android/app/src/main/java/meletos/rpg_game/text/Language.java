package meletos.rpg_game.text;

public enum Language {
    ENG("text/eng.txt",0), CZE("text/cze.txt",1);

    private final String filename;
    private final int ID;
    private static final Language[] values = Language.values();

    Language(String filename, int ID) {
        this.filename = filename;
        this.ID = ID;
    }

    public static Language getLanguage(int ID){
        for (Language value:values) {
            if (value.ID == ID) return value;
        }
        return ENG;
    }

    public int getID() {
        return ID;
    }

    public String getFilename() {
        return filename;
    }
}
