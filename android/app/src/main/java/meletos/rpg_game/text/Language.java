package meletos.rpg_game.text;

public enum Language {
    ENG("text/eng/gui.txt", "text/eng/items.txt", "eng.txt", 0),
    CZE("text/cze/gui.txt", "text/cze/items.txt", "cze.txt", 1);

    private final String gui;
    private final String items;
    private final String dialog;
    private final int ID;
    private static final Language[] values = Language.values();

    Language(String gui, String items, String dialog, int ID) {
        this.gui = gui;
        this.items = items;
        this.ID = ID;
        this.dialog = dialog;
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

    public String getDialog() {
        return dialog;
    }
}
