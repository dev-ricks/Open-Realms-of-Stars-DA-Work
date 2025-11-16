package org.openRealmOfStars.starMap.planet;
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
import org.openRealmOfStars.player.tech.TechFactory;
import org.openRealmOfStars.starMap.planet.construction.Building;
import org.openRealmOfStars.starMap.planet.construction.Construction;
import org.openRealmOfStars.starMap.planet.enums.PlanetTypes;
import org.openRealmOfStars.starMap.planet.enums.RadiationType;

/**
 * Test for Planet.getProductionList() filtering based on building requirements
 */
public class PlanetProductionListFilterTest {

  /**
   * Create a test planet with specified size, population, and planet type
   */
  private Planet createTestPlanet(int size, int population, PlanetTypes planetType) {
    Coordinate coord = new Coordinate(5, 5);
    Planet planet = new Planet(coord, "Test Planet", size, false);
    planet.setRadiationLevel(RadiationType.NO_RADIATION);
    planet.setPlanetType(planetType);
    
    PlayerInfo info = new PlayerInfo(SpaceRaceFactory.createOne("HUMANS"));
    info.setEmpireName("Test Empire");
    planet.setPlanetOwner(0, info);
    
    // Set population by adding workers
    for (int i = 0; i < population; i++) {
      planet.setWorkers(Planet.FOOD_FARMERS, i + 1);
    }
    
    return planet;
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testSmallPlanetFiltersExtendedTechBuildings() {
    // Small planet (size 8) should not have extended tech buildings in production list
    Planet smallPlanet = createTestPlanet(8, 5, PlanetTypes.BARRENWORLD1);
    
    // Add tech that unlocks extended buildings
    smallPlanet.getPlanetPlayerInfo().getTechList().addTech(
        TechFactory.createImprovementTech("Advanced research lab", 11));
    
    Construction[] productionList = smallPlanet.getProductionList();
    
    boolean foundAdvancedLab = false;
    for (Construction c : productionList) {
      if (c instanceof Building) {
        Building b = (Building) c;
        if ("Advanced research lab".equals(b.getName())) {
          foundAdvancedLab = true;
          break;
        }
      }
    }
    
    assertFalse("Advanced research lab should not be in production list for small planet",
        foundAdvancedLab);
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testLargePlanetIncludesExtendedTechBuildings() {
    // Large planet (size 12) with sufficient population should have extended tech buildings
    Planet largePlanet = createTestPlanet(12, 7, PlanetTypes.BARRENWORLD1);
    
    // Add tech that unlocks extended buildings
    largePlanet.getPlanetPlayerInfo().getTechList().addTech(
        TechFactory.createImprovementTech("Advanced research lab", 11));
    largePlanet.getPlanetPlayerInfo().getTechList().addTech(
        TechFactory.createImprovementTech("Quantum factory", 12));
    
    Construction[] productionList = largePlanet.getProductionList();
    
    boolean foundAdvancedLab = false;
    boolean foundQuantumFactory = false;
    
    for (Construction c : productionList) {
      if (c instanceof Building) {
        Building b = (Building) c;
        if ("Advanced research lab".equals(b.getName())) {
          foundAdvancedLab = true;
        }
        if ("Quantum factory".equals(b.getName())) {
          foundQuantumFactory = true;
        }
      }
    }
    
    assertTrue("Advanced research lab should be in production list for large planet",
        foundAdvancedLab);
    assertTrue("Quantum factory should be in production list for large planet",
        foundQuantumFactory);
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testPlanetWithLowPopulationFiltersBuildings() {
    // Planet with correct size but low population
    Planet planet = createTestPlanet(10, 3, PlanetTypes.BARRENWORLD1);
    
    // Add tech that unlocks Advanced research lab (requires pop 5)
    planet.getPlanetPlayerInfo().getTechList().addTech(
        TechFactory.createImprovementTech("Advanced research lab", 11));
    
    Construction[] productionList = planet.getProductionList();
    
    boolean foundAdvancedLab = false;
    for (Construction c : productionList) {
      if (c instanceof Building) {
        Building b = (Building) c;
        if ("Advanced research lab".equals(b.getName())) {
          foundAdvancedLab = true;
          break;
        }
      }
    }
    
    assertFalse("Advanced research lab should not be in production list with low population",
        foundAdvancedLab);
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testWorldTypeFiltering() {
    // Water world should not have Quantum factory (requires BARRENWORLD/VOLCANICWORLD/DESERTWORLD)
    Planet waterPlanet = createTestPlanet(11, 6, PlanetTypes.WATERWORLD1);
    
    waterPlanet.getPlanetPlayerInfo().getTechList().addTech(
        TechFactory.createImprovementTech("Quantum factory", 12));
    
    Construction[] productionList = waterPlanet.getProductionList();
    
    boolean foundQuantumFactory = false;
    for (Construction c : productionList) {
      if (c instanceof Building) {
        Building b = (Building) c;
        if ("Quantum factory".equals(b.getName())) {
          foundQuantumFactory = true;
          break;
        }
      }
    }
    
    assertFalse("Quantum factory should not be in production list for water world",
        foundQuantumFactory);
    
    // But barren world should have it
    Planet barrenPlanet = createTestPlanet(11, 6, PlanetTypes.BARRENWORLD1);
    barrenPlanet.getPlanetPlayerInfo().getTechList().addTech(
        TechFactory.createImprovementTech("Quantum factory", 12));
    
    Construction[] barrenProductionList = barrenPlanet.getProductionList();
    
    boolean foundInBarren = false;
    for (Construction c : barrenProductionList) {
      if (c instanceof Building) {
        Building b = (Building) c;
        if ("Quantum factory".equals(b.getName())) {
          foundInBarren = true;
          break;
        }
      }
    }
    
    assertTrue("Quantum factory should be in production list for barren world",
        foundInBarren);
  }

  @Test
  @Category(org.openRealmOfStars.UnitTest.class)
  public void testBasicBuildingsStillAvailable() {
    // Extended tech buildings should be filtered, but basic buildings should still be available
    Planet smallPlanet = createTestPlanet(8, 3, PlanetTypes.BARRENWORLD1);
    
    Construction[] productionList = smallPlanet.getProductionList();
    
    boolean foundBasicFactory = false;
    boolean foundBasicLab = false;
    
    for (Construction c : productionList) {
      if (c instanceof Building) {
        Building b = (Building) c;
        if ("Basic factory".equals(b.getName())) {
          foundBasicFactory = true;
        }
        if ("Basic lab".equals(b.getName())) {
          foundBasicLab = true;
        }
      }
    }
    
    assertTrue("Basic factory should still be available",
        foundBasicFactory);
    assertTrue("Basic lab should still be available",
        foundBasicLab);
  }
}
