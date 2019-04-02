package meletos.rpg_game.text;

public enum Language {
    ENG("text/eng/gui.txt", "text/eng/items.txt", 0),
    CZE("text/cze/gui.txt", "text/cze/items.txt", 1);

    private final String gui;
    private final String items;
    private final int ID;
    private static final Language[] values = Language.values();

    Language(String gui, String items, int ID) {
        this.gui = gui;
        this.items = items;
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

    public String getGui() {
        return gui;
    }

    public String getItems() {
        return items;
    }
}
