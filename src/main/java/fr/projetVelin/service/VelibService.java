package fr.projetVelin.service;

import fr.projetVelin.dto.Station;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VelibService {
    private final String API_DISPO_URL = "https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/velib-disponibilite-en-temps-reel/records?limit=100";
    private final String API_STATIONS_URL = "https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/velib-emplacement-des-stations/records?limit=100";

    public List<Station> recupStations(double userLatitude, double userLongitude) {
        RestTemplate restTemplate = new RestTemplate();
        List<Station> stations = new ArrayList<>();

        try {
            Map<String, String> mapAdresses = new HashMap<>();
            String jsonStations = restTemplate.getForObject(API_STATIONS_URL, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootStations = mapper.readTree(jsonStations);
            for (JsonNode node : rootStations.path("results")) {
                String code = node.path("stationcode").asText();
                String adresse = node.path("station_adresse").asText("Adresse non disponible");
                mapAdresses.put(code, adresse);
            }

            String jsonDispo = restTemplate.getForObject(API_DISPO_URL, String.class);
            JsonNode rootDispo = mapper.readTree(jsonDispo);
            JsonNode results = rootDispo.path("results");

            for (JsonNode node : results) {
                String codeStation = node.path("stationcode").asText();
                String nomStation = node.path("name").asText("Station inconnue");
                int velosDisponibles = node.path("numbikesavailable").asInt(0);
                int placesDisponibles = node.path("numdocksavailable").asInt(0);

                String adresseStation = mapAdresses.getOrDefault(codeStation, "Adresse non disponible");

                JsonNode noeudCoordonnees = node.path("coordonnees_geo");
                double distance = 0.0;

                if (!noeudCoordonnees.isMissingNode()) {
                    double stationLatitude = noeudCoordonnees.path("lat").asDouble();
                    double stationLongitude = noeudCoordonnees.path("lon").asDouble();
                    distance = calculeDistance(userLatitude, userLongitude, stationLatitude, stationLongitude);
                }

                stations.add(new Station(nomStation, adresseStation, velosDisponibles, placesDisponibles, distance));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stations.sort(Comparator.comparingDouble(Station::getDistance));

        List<Station> stationsProches = new ArrayList<>();
        int limite = Math.min(5, stations.size());
        for (int i = 0; i < limite; i++) {
            stationsProches.add(stations.get(i));
        }

        return stationsProches;
    }

    private double calculeDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double latitudeDiff = latitude2 - latitude1;
        double longitudeDiff = longitude2 - longitude1;
        return Math.round(Math.hypot(latitudeDiff, longitudeDiff) * 111000);
    }
}