package com.geospringworks;

import com.geospringworks.helper.ProjectUrl;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.feature.FeatureJSON;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.geospringworks.helper.GeometryHelper.transformShpToGeoJson;

@SpringBootTest
class GeoSpringWorksApplicationTests {
	@Autowired
	private ProjectUrl ProjectUrl;

	@Test
	void contextLoads() throws IOException {
		System.out.println("Hello Spring!");
//		System.out.println(ProjectUrl.getProjectUrl());
//		Path path = Paths.get("shpExample/bugsites.shp");
//		String json = transformShpToGeoJson(path.toString());
//		System.out.println(json);
//		InputStream inputStream = new ByteArrayInputStream(json.getBytes());
//		FeatureCollection featureCollection = new FeatureJSON().readFeatureCollection(inputStream);
	}

}
