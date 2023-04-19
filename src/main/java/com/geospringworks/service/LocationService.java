package com.geospringworks.service;

import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;

import java.util.Optional;

public interface LocationService {

    public boolean exists(String userId, Long id);

    public Long saveLocation(String userId, Feature feature);

    public void updateLocation(String userId, Long id, Feature feature);

    public void deleteLocation(String userId, Long id);

//    public Optional<Feature> findLocationById(String userId, Long id);

    public String findLocationById(String userId, Long id);

    public FeatureCollection findAllLocations(String userId);

    public FeatureCollection findAllLocationsWithin(String userId, org.wololo.geojson.Geometry geoJson);
}
