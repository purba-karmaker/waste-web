package com.wasteweb.model;

import java.util.List;

public class WasteAnalysis {
    private String cleanDescription;
    private String category;
    private List<ReuseOption> reuseOptions;
    private ImpactStats impactStats;
    private List<Match> matches;

    public static class ReuseOption {
        private String title;
        private String description;
        private String icon;
        private String priority; // HIGH, MEDIUM, LOW

        public ReuseOption() {}
        public ReuseOption(String title, String description, String icon, String priority) {
            this.title = title;
            this.description = description;
            this.icon = icon;
            this.priority = priority;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }

    public static class ImpactStats {
        private double co2SavedKg;
        private double landfillAvoidedKg;
        private String equivalentTo;

        public ImpactStats() {}
        public ImpactStats(double co2SavedKg, double landfillAvoidedKg, String equivalentTo) {
            this.co2SavedKg = co2SavedKg;
            this.landfillAvoidedKg = landfillAvoidedKg;
            this.equivalentTo = equivalentTo;
        }

        public double getCo2SavedKg() { return co2SavedKg; }
        public void setCo2SavedKg(double co2SavedKg) { this.co2SavedKg = co2SavedKg; }
        public double getLandfillAvoidedKg() { return landfillAvoidedKg; }
        public void setLandfillAvoidedKg(double landfillAvoidedKg) { this.landfillAvoidedKg = landfillAvoidedKg; }
        public String getEquivalentTo() { return equivalentTo; }
        public void setEquivalentTo(String equivalentTo) { this.equivalentTo = equivalentTo; }
    }

    public static class Match {
        private String name;
        private String type;
        private String distance;
        private String need;
        private String urgency;

        public Match() {}
        public Match(String name, String type, String distance, String need, String urgency) {
            this.name = name;
            this.type = type;
            this.distance = distance;
            this.need = need;
            this.urgency = urgency;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDistance() { return distance; }
        public void setDistance(String distance) { this.distance = distance; }
        public String getNeed() { return need; }
        public void setNeed(String need) { this.need = need; }
        public String getUrgency() { return urgency; }
        public void setUrgency(String urgency) { this.urgency = urgency; }
    }

    // Getters & Setters
    public String getCleanDescription() { return cleanDescription; }
    public void setCleanDescription(String cleanDescription) { this.cleanDescription = cleanDescription; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<ReuseOption> getReuseOptions() { return reuseOptions; }
    public void setReuseOptions(List<ReuseOption> reuseOptions) { this.reuseOptions = reuseOptions; }
    public ImpactStats getImpactStats() { return impactStats; }
    public void setImpactStats(ImpactStats impactStats) { this.impactStats = impactStats; }
    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }
}
