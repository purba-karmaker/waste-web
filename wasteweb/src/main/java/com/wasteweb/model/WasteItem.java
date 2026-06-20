package com.wasteweb.model;

public class WasteItem {
    private String description;
    private String quantity;
    private String unit;
    private String location;
    private String category;

    public WasteItem() {}

    public WasteItem(String description, String quantity, String unit, String location) {
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
