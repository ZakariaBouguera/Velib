package fr.projetVelin.service;

import fr.projetVelin.dto.Station;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class VelibService {

    private final String API_URL = "https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/velib-disponibilite-en-temps-reel/records?limit=100";

    public List<Station> recupStations(double userLatitude, double userLongitude) {
        RestTemplate restTemplate = new RestTemplate();
        List<Station> stations = new ArrayList<>();

        try {
            String jsonResponse = restTemplate.getForObject(API_URL, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode results = root.path("results");

            for (JsonNode node : results) {
                String nomStation = node.path("name").asText("Station inconnue");
                int velosDisponibles = node.path("numbikesavailable").asInt(0);
                int placesDisponibles = node.path("numdocksavailable").asInt(0);

                JsonNode noeudCoordonnees = node.path("coourdonnees_geo");
                double distance = 0.0;

                if (!noeudCoordonnees.isMissingNode()) {
                    double stationLatitude = noeudCoordonnees.path("lat").asDouble();
                    double stationLongitude = noeudCoordonnees.path("lon").asDouble();

                    // Calcul de la distance entre l'utilisateur et la station
                    distance = calculeDistance(userLatitude, userLongitude, stationLatitude, stationLongitude);
                }

                stations.add(new Station(nomStation, velosDisponibles, placesDisponibles, distance));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stations;
    }

    private double calculeDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double latitudeDiff = latitude2 - latitude1;
        double longitudeDiff = longitude2 - longitude1;
        return Math.round(Math.hypot(latitudeDiff, longitudeDiff) * 111000);
    }
}