package ro.fr33styler.botcreator.version;

public enum GameVersion {

    VERSION_1_16("1.16.5", new Version_1_16()),
    VERSION_1_15("1.15.2", new Version_1_15()),
    VERSION_1_12("1.12.2", new Version_1_12());

    private final String id;
    private final Version version;

    GameVersion(String id, Version version) {
        this.id = id;
        this.version = version;
    }

    public String getID() {
        return id;
    }

    public static Version getByName(String id) {
        for (GameVersion version : values()) {
            if (version.getID().equals(id)) {
                return version.version;
            }
        }
        return null;
    }

}
