package org.openRealmOfStars.starMap.planet.construction;
/*
 * Open Realm of Stars game project
 * Copyright (C) 2024
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/
 */

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openRealmOfStars.player.PlayerInfo;
import org.openRealmOfStars.player.race.SpaceRaceFactory;
import org.openRealmOfStars.starMap.Coordinate;
import org.openRealmOfStars.starMap.planet.Planet;
import org.openRealmOfStars.starMap.planet.enums.PlanetTypes;
import org.openRealmOfStars.starMap.planet.enums.RadiationType;
import org.openRealmOfStars.starMap.planet.enums.WorldType;

/**
 * Test for Building planet requirements validation
 */
public class BuildingRequirementTest {

  /**
   * Create a test planet with specified size, population, and planet type
   */
  private Planet createTestPlanet(int size, int population, PlanetTypes planetType) {
    Coordinate coord = new Coordinate(5, 5);
    Planet planet = new Planet(coord, "Test Planet", size, false);
    planet.setRadiationLevel(RadiationType.NO_RADIATION);
    planet.setPlanetType(planetType);
    
    PlayerInfo info = new PlayerInfo(SpaceRaceFactory.createOne("HUMANS"));
    planet.setPlanetOwner(0, info);
    
    // Set population by adding workers
    for (int i = 0; i < population; i++) {
      planet.setWorkers(Planet.FOOD_FARMERS, i + 1);
    }
    
    return planet;
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testBuildingWithoutRequirements() {
    // Test that buildings without requirements can be built on any planet
    Building building = BuildingFactory.createByName("Basic lab");
    assertNotNull("Basic lab should exist", building);
    
    Planet smallPlanet = createTestPlanet(5, 1, PlanetTypes.BARRENWORLD1);
    Planet largePlanet = createTestPlanet(15, 10, PlanetTypes.WATERWORLD1);
    
    assertTrue("Basic lab should be buildable on small planet",
        building.canBuildOnPlanet(smallPlanet));
    assertTrue("Basic lab should be buildable on large planet",
        building.canBuildOnPlanet(largePlanet));
    assertNull("Basic lab should have no failure reason",
        building.getBuildRequirementFailureReason(smallPlanet));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testAdvancedResearchLabRequirements() {
    Building building = BuildingFactory.createByName("Advanced research lab");
    assertNotNull("Advanced research lab should exist", building);
    
    // Test planet size requirement
    assertEquals("Advanced research lab should require planet size 10",
        10, building.getMinPlanetSize());
    assertEquals("Advanced research lab should require population 5",
        5, building.getMinPopulation());
    
    // Test with planet that's too small
    Planet smallPlanet = createTestPlanet(9, 5, PlanetTypes.BARRENWORLD1);
    assertFalse("Should not be buildable on planet size 9",
        building.canBuildOnPlanet(smallPlanet));
    String reason = building.getBuildRequirementFailureReason(smallPlanet);
    assertNotNull("Should have failure reason", reason);
    assertTrue("Failure reason should mention planet size",
        reason.contains("planet size"));
    assertTrue("Failure reason should mention 10",
        reason.contains("10"));
    
    // Test with planet that has too little population
    Planet lowPopPlanet = createTestPlanet(10, 4, PlanetTypes.BARRENWORLD1);
    assertFalse("Should not be buildable with population 4",
        building.canBuildOnPlanet(lowPopPlanet));
    reason = building.getBuildRequirementFailureReason(lowPopPlanet);
    assertNotNull("Should have failure reason", reason);
    assertTrue("Failure reason should mention population",
        reason.contains("population"));
    assertTrue("Failure reason should mention 5",
        reason.contains("5"));
    
    // Test with planet that meets all requirements
    Planet validPlanet = createTestPlanet(10, 5, PlanetTypes.BARRENWORLD1);
    assertTrue("Should be buildable on valid planet",
        building.canBuildOnPlanet(validPlanet));
    assertNull("Should have no failure reason on valid planet",
        building.getBuildRequirementFailureReason(validPlanet));
    
    // Test with planet that exceeds requirements
    Planet largePlanet = createTestPlanet(15, 10, PlanetTypes.BARRENWORLD1);
    assertTrue("Should be buildable on larger planet",
        building.canBuildOnPlanet(largePlanet));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testQuantumFactoryRequirements() {
    Building building = BuildingFactory.createByName("Quantum factory");
    assertNotNull("Quantum factory should exist", building);
    
    // Test requirements
    assertEquals("Quantum factory should require planet size 11",
        11, building.getMinPlanetSize());
    assertEquals("Quantum factory should require population 6",
        6, building.getMinPopulation());
    assertNotNull("Quantum factory should have allowed world types",
        building.getAllowedWorldTypes());
    assertTrue("Quantum factory should allow BARRENWORLD",
        containsWorldType(building.getAllowedWorldTypes(), WorldType.BARRENWORLD));
    
    // Test with wrong world type
    Planet waterPlanet = createTestPlanet(11, 6, PlanetTypes.WATERWORLD1);
    assertFalse("Should not be buildable on water world",
        building.canBuildOnPlanet(waterPlanet));
    String reason = building.getBuildRequirementFailureReason(waterPlanet);
    assertNotNull("Should have failure reason", reason);
    assertTrue("Failure reason should mention world type",
        reason.contains("world type"));
    
    // Test with correct world type
    Planet barrenPlanet = createTestPlanet(11, 6, PlanetTypes.BARRENWORLD1);
    assertTrue("Should be buildable on barren world",
        building.canBuildOnPlanet(barrenPlanet));
    
    // Test with desert world (also allowed)
    Planet desertPlanet = createTestPlanet(11, 6, PlanetTypes.DESERTWORLD1);
    assertTrue("Should be buildable on desert world",
        building.canBuildOnPlanet(desertPlanet));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testNanoFactoryRequirements() {
    Building building = BuildingFactory.createByName("Nano factory");
    assertNotNull("Nano factory should exist", building);
    
    assertEquals("Nano factory should require planet size 12",
        12, building.getMinPlanetSize());
    assertEquals("Nano factory should require population 7",
        7, building.getMinPopulation());
    
    // Test with planet that meets requirements
    Planet validPlanet = createTestPlanet(12, 7, PlanetTypes.BARRENWORLD1);
    assertTrue("Should be buildable on valid planet",
        building.canBuildOnPlanet(validPlanet));
    
    // Test with planet that's too small
    Planet smallPlanet = createTestPlanet(11, 7, PlanetTypes.BARRENWORLD1);
    assertFalse("Should not be buildable on planet size 11",
        building.canBuildOnPlanet(smallPlanet));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testDysonSphereComponentRequirements() {
    Building building = BuildingFactory.createByName("Dyson sphere component");
    assertNotNull("Dyson sphere component should exist", building);
    
    assertEquals("Dyson sphere component should require planet size 13",
        13, building.getMinPlanetSize());
    assertEquals("Dyson sphere component should require population 8",
        8, building.getMinPopulation());
    
    // Test world type restrictions (only BARRENWORLD and DESERTWORLD)
    Planet validBarrenPlanet = createTestPlanet(13, 8, PlanetTypes.BARRENWORLD1);
    assertTrue("Should be buildable on barren world",
        building.canBuildOnPlanet(validBarrenPlanet));
    
    Planet validDesertPlanet = createTestPlanet(13, 8, PlanetTypes.DESERTWORLD1);
    assertTrue("Should be buildable on desert world",
        building.canBuildOnPlanet(validDesertPlanet));
    
    // Should not be buildable on volcanic world
    Planet volcanicPlanet = createTestPlanet(13, 8, PlanetTypes.VOLCANICPLANET1);
    assertFalse("Should not be buildable on volcanic world",
        building.canBuildOnPlanet(volcanicPlanet));
    
    // Should not be buildable on water world
    Planet waterPlanet = createTestPlanet(13, 8, PlanetTypes.WATERWORLD1);
    assertFalse("Should not be buildable on water world",
        building.canBuildOnPlanet(waterPlanet));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testBuildingWithNullPlanet() {
    Building building = BuildingFactory.createByName("Advanced research lab");
    assertFalse("Should return false for null planet",
        building.canBuildOnPlanet(null));
    assertEquals("Should return error message for null planet",
        "Invalid planet", building.getBuildRequirementFailureReason(null));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testMultipleRequirementFailures() {
    // Test that the first failing requirement is reported
    Building building = BuildingFactory.createByName("Advanced research lab");
    
    // Planet fails both size and population requirements
    // Size check comes first, so it should report size failure
    Planet badPlanet = createTestPlanet(9, 3, PlanetTypes.BARRENWORLD1);
    assertFalse("Should not be buildable", building.canBuildOnPlanet(badPlanet));
    String reason = building.getBuildRequirementFailureReason(badPlanet);
    assertNotNull("Should have failure reason", reason);
    // Size check is first, so should report size failure
    assertTrue("Should report size failure first",
        reason.contains("planet size"));
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testBuildingDescriptionShowsRequirements() {
    Building building = BuildingFactory.createByName("Advanced research lab");
    String description = building.getFullDescription(null);
    
    assertNotNull("Description should not be null", description);
    assertTrue("Description should contain requirements",
        description.contains("Requirements:"));
    assertTrue("Description should mention planet size",
        description.contains("Planet size"));
    assertTrue("Description should mention population",
        description.contains("Population"));
  }

  /**
   * Helper method to check if world type array contains a specific world type
   */
  private boolean containsWorldType(WorldType[] types, WorldType type) {
    if (types == null) {
      return false;
    }
    for (WorldType t : types) {
      if (t == type) {
        return true;
      }
    }
    return false;
  }
}
