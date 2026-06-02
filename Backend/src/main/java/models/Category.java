package models;

public enum Category {

    ITALIANA("Italian"),
    CHINA("Chinese"),
    JAPONESA("Japanese"),
    MEXICANA("Mexican"),
    AMERICANA("American"),
    INDIA("Indian"),
    MEDITERRANEA("Mediterranean"),
    RAPIDA("Fast Food"),
    MARISCOS("Seafood"),
    VEGETARIANA("Vegetarian"),
    CAFE("Cafe"),
    POSTRES("Desserts"),
    OTRAS("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Category fromString(String value) {
        if (value == null || value.isBlank()) return OTRAS;
        String lower = value.toLowerCase();
        for (Category c : values()) {
            if (lower.contains(c.displayName.toLowerCase())) return c;
        }
        return OTRAS;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
