package fr.projetVelin.dto;
public class Station {
    private String name;
    private int bikesAvailable;
    private int docksAvailable;
    private double distance; // en mètres

    public Station(String name, int bikesAvailable, int docksAvailable, double distance) {
        this.name = name;
        this.bikesAvailable = bikesAvailable;
        this.docksAvailable = docksAvailable;
        this.distance = distance;
    }

    // Getters
    public String getName() { return name; }
    public int getBikesAvailable() { return bikesAvailable; }
    public int getDocksAvailable() { return docksAvailable; }
    public double getDistance() { return distance; }
}