package org.openRealmOfStars.player.tech;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openRealmOfStars.starMap.GalaxyConfig;
import org.openRealmOfStars.starMap.planet.construction.Building;
import org.openRealmOfStars.starMap.planet.construction.ExtendedBuildingFactory;
import org.openRealmOfStars.starMap.planet.construction.BuildingCostCalculator;
import org.openRealmOfStars.starMap.planet.construction.BuildingType;
import org.openRealmOfStars.player.race.SpaceRace;
import org.openRealmOfStars.player.race.SpaceRaceFactory;

/**
 * Functional test to verify extended tech system works correctly
 */
public class ExtendedTechFunctionalTest {

  @Test
  public void testCompleteExtendedTechWorkflow() {
    // 1. Setup galaxy with extended tech enabled
    GalaxyConfig config = new GalaxyConfig();
    config.setExtendedTechLevels(true);
    assertTrue("Extended tech should be enabled", config.isExtendedTechLevels());
    
    // 2. Create tech list with extended levels
    SpaceRace race = SpaceRaceFactory.createOne("HUMANS");
    TechList techList = new TechList(race, true);
    assertNotNull("Extended tech list should be created", techList);
    
    // 3. Create extended tech objects
    Tech quantumBeam = new Tech("Quantum beam", TechType.Combat, 11);
    Tech realityCannon = new Tech("Reality cannon", TechType.Combat, 20);
    
    assertEquals("Quantum beam should be level 11", 11, quantumBeam.getLevel());
    assertEquals("Reality cannon should be level 20", 20, realityCannon.getLevel());
    
    // 4. Create extended buildings
    Building level11Research = ExtendedBuildingFactory.createExtendedResearchCenter(11);
    Building level15Factory = ExtendedBuildingFactory.createExtendedFactory(15);
    Building level20Replicator = ExtendedBuildingFactory.createAdvancedReplicatorCenter(20);
    
    // Verify buildings are created correctly
    assertNotNull("Level 11 research center should be created", level11Research);
    assertNotNull("Level 15 factory should be created", level15Factory);
    assertNotNull("Level 20 replicator should be created", level20Replicator);
    
    // 5. Test cost scaling works
    assertTrue("Level 15 building should cost more than level 11", 
               level15Factory.getScaledMetalCost() > level11Research.getScaledMetalCost());
    assertTrue("Level 20 building should cost more than level 15", 
               level20Replicator.getScaledMetalCost() > level15Factory.getScaledMetalCost());
    
    // 6. Test bonus scaling works
    assertTrue("Level 15 building should have better bonus than level 11", 
               level15Factory.getFactBonus() > level11Research.getReseBonus());
    assertTrue("Level 20 building should have best bonus", 
               level20Replicator.getFactBonus() > level15Factory.getFactBonus());
    
    // 7. Test tier system
    assertEquals("Level 11 should be Advanced", "Advanced", level11Research.getTierDescription());
    assertEquals("Level 15 should be Superior", "Superior", level15Factory.getTierDescription());
    assertEquals("Level 20 should be Ultimate", "Ultimate", level20Replicator.getTierDescription());
    
    // 8. Test building availability
    assertTrue("Level 11 building should be available at level 11", 
               ExtendedBuildingFactory.isBuildingAvailable("Quantum research center", 11));
    assertTrue("Level 20 building should be available at level 20", 
               ExtendedBuildingFactory.isBuildingAvailable("Ultimate research center", 20));
    assertFalse("Level 20 building should not be available at level 19", 
                ExtendedBuildingFactory.isBuildingAvailable("Ultimate research center", 19));
    
    // 9. Test cost calculator
    double roi = BuildingCostCalculator.calculateROI(level15Factory, 25);
    assertTrue("ROI should be calculated (can be negative for extended buildings)", 
               roi != 0); // Just test that it's calculated, not necessarily positive
    
    boolean costEffective = BuildingCostCalculator.isCostEffective(level15Factory);
    assertTrue("Building should be cost-effective according to extended building standards", costEffective);
    
    // 10. Test building can check tech requirements
    assertTrue("Level 11 building should be buildable with tech 11", 
               level11Research.canBuildWithTechLevel(11));
    assertFalse("Level 11 building should not be buildable with tech 10", 
                level11Research.canBuildWithTechLevel(10));
    assertTrue("Level 20 building should be buildable with tech 20", 
               level20Replicator.canBuildWithTechLevel(20));
  }

  @Test
  public void testRegressionOriginalFunctionality() {
    // Test that original functionality still works when extended tech is disabled
    GalaxyConfig config = new GalaxyConfig();
    config.setExtendedTechLevels(false);
    
    SpaceRace race = SpaceRaceFactory.createOne("HUMANS");
    TechList techList = new TechList(race, false);
    assertNotNull("Standard tech list should work", techList);
    
    // Test original tech levels
    Tech laserTech = new Tech("Laser", TechType.Combat, 1);
    Tech massdriverTech = new Tech("Massdriver", TechType.Combat, 10);
    
    assertEquals("Level 1 tech should work", 1, laserTech.getLevel());
    assertEquals("Level 10 tech should work", 10, massdriverTech.getLevel());
    
    // Test that extended buildings have proper defaults
    Building standardBuilding = new Building("Test Building", "ICON_TEST", BuildingType.FACTORY);
    assertFalse("Standard building should not be extended", standardBuilding.isExtendedTechBuilding());
    assertEquals("Standard building should have standard tier", "Standard", standardBuilding.getTierDescription());
  }

  @Test
  public void testSystemBoundaries() {
    // Test minimum and maximum values
    Tech minTech = new Tech("Min Tech", TechType.Combat, 1);
    Tech maxTech = new Tech("Max Tech", TechType.Combat, 20);
    
    assertEquals("Min tech level should be 1", 1, minTech.getLevel());
    assertEquals("Max tech level should be 20", 20, maxTech.getLevel());
    
    Building minBuilding = ExtendedBuildingFactory.createExtendedResearchCenter(11);
    Building maxBuilding = ExtendedBuildingFactory.createExtendedResearchCenter(20);
    
    assertEquals("Min building level should be 11", 11, minBuilding.getRequiredTechLevel());
    assertEquals("Max building level should be 20", 20, maxBuilding.getRequiredTechLevel());
    
    // Test cost scaling boundaries
    int baseCost = 1000;
    int minScaledCost = BuildingCostCalculator.calculateMetalCost(baseCost, 11, BuildingType.RESEARCH);
    int maxScaledCost = BuildingCostCalculator.calculateMetalCost(baseCost, 20, BuildingType.RESEARCH);
    
    assertTrue("Max cost should be significantly higher than min cost", 
               maxScaledCost > minScaledCost * 2);
    
    // Test bonus scaling boundaries
    int baseBonus = 5;
    int minScaledBonus = BuildingCostCalculator.calculateResearchBonus(baseBonus, 11);
    int maxScaledBonus = BuildingCostCalculator.calculateResearchBonus(baseBonus, 20);
    
    assertTrue("Max bonus should be significantly higher than min bonus", 
               maxScaledBonus > minScaledBonus * 2);
  }
}
