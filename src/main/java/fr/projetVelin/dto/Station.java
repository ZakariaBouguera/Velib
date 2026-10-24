package fr.projetVelin.dto;
public class Station {
    private String name;
    private String address;
    private int veloDisponible;
    private int borneDisponible;
    private double distance;

    public Station(String name,String address,  int veloDisponible, int borneDisponible, double distance) {
        this.name = name;
        this.address = address;
        this.veloDisponible = veloDisponible;
        this.borneDisponible = borneDisponible;
        this.distance = distance;
    }


    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public int getVeloDisponible() {
        return veloDisponible;
    }
    public int getBorneDisponible() {
        return borneDisponible;
    }
    public double getDistance() {
        return distance;
    }
}