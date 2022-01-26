package de.volkswagen.f73.evnavigator.service;

import de.volkswagen.f73.evnavigator.model.POI;
import de.volkswagen.f73.evnavigator.repository.POIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Justo, David (SE-A/34)
 * @author Bücker, Thies (SE-A/34)
 */
@Service
public class POIService {

    @Autowired
    private POIRepository poiRepo;

    public List<POI> getAllPOIs(){
        return this.poiRepo.findAll();
    }

    public POI savePOI(POI poi){
        return this.poiRepo.save(poi);
    }

    public List<POI> getPOIsCloseTo(Double lat, Double lon, Double distKm){
        List<POI> pois = this.poiRepo.findAll();

        return pois.stream().filter(p -> true).collect(Collectors.toList());
    }
}
