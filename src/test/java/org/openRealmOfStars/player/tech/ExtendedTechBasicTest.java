package org.openRealmOfStars.player.tech;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.openRealmOfStars.starMap.GalaxyConfig;
import org.openRealmOfStars.starMap.planet.construction.Building;
import org.openRealmOfStars.starMap.planet.construction.ExtendedBuildingFactory;
import org.openRealmOfStars.starMap.planet.construction.BuildingCostCalculator;
import org.openRealmOfStars.starMap.planet.construction.BuildingType;
import org.openRealmOfStars.player.race.SpaceRace;
import org.openRealmOfStars.player.race.SpaceRaceFactory;

/**
 * Basic test class for extended tech system functionality
 */
public class ExtendedTechBasicTest {

  private GalaxyConfig config;
  private SpaceRace race;

  @Before
  public void setUp() {
    config = new GalaxyConfig();
    race = SpaceRaceFactory.createOne("HUMANS");
  }

  /**
   * Test that extended tech levels can be enabled/disabled
   */
  @Test
  public void testExtendedTechLevelsConfiguration() {
    // Test default (disabled)
    assertFalse("Extended tech should be disabled by default", 
                config.isExtendedTechLevels());
    
    // Test enabling
    config.setExtendedTechLevels(true);
    assertTrue("Extended tech should be enabled", 
               config.isExtendedTechLevels());
  }

  /**
   * Test that Tech class supports levels up to 20
   */
  @Test
  public void testTechLevelRange() {
    // Test levels 1-10 (original range)
    for (int level = 1; level <= 10; level++) {
      Tech tech = new Tech("Test Tech", TechType.Combat, level);
      assertEquals("Tech level should be set correctly", level, tech.getLevel());
    }
    
    // Test levels 11-20 (extended range)
    for (int level = 11; level <= 20; level++) {
      Tech tech = new Tech("Test Tech", TechType.Combat, level);
      assertEquals("Extended tech level should be set correctly", level, tech.getLevel());
    }
  }

  /**
   * Test that TechList handles extended tech levels correctly
   */
  @Test
  public void testTechListExtendedLevels() {
    // Test with extended tech disabled
    TechList standardTechList = new TechList(race, false);
    assertNotNull("Standard tech list should be created", standardTechList);
    
    // Test with extended tech enabled
    TechList extendedTechList = new TechList(race, true);
    assertNotNull("Extended tech list should be created", extendedTechList);
  }

  /**
   * Test extended building creation
   */
  @Test
  public void testExtendedBuildingCreation() {
    // Test research center creation
    Building quantumResearch = ExtendedBuildingFactory.createExtendedResearchCenter(11);
    assertNotNull("Quantum research center should be created", quantumResearch);
    assertEquals("Should have correct name", "Quantum research center", quantumResearch.getName());
    assertEquals("Should have correct tech level", 11, quantumResearch.getRequiredTechLevel());
    assertTrue("Should be extended tech building", quantumResearch.isExtendedTechBuilding());
    
    // Test factory creation
    Building megaFactory = ExtendedBuildingFactory.createExtendedFactory(11);
    assertNotNull("Mega factory should be created", megaFactory);
    assertEquals("Should have correct name", "Mega factory", megaFactory.getName());
    assertEquals("Should have correct tech level", 11, megaFactory.getRequiredTechLevel());
    
    // Test replicator creation
    Building advancedReplicator = ExtendedBuildingFactory.createAdvancedReplicatorCenter(11);
    assertNotNull("Advanced replicator should be created", advancedReplicator);
    assertEquals("Should have correct name", "Advanced replicator center", advancedReplicator.getName());
    assertEquals("Should have correct tech level", 11, advancedReplicator.getRequiredTechLevel());
  }

  /**
   * Test building cost scaling
   */
  @Test
  public void testBuildingCostScaling() {
    // Test that higher level buildings cost more
    Building level11Building = ExtendedBuildingFactory.createExtendedResearchCenter(11);
    Building level15Building = ExtendedBuildingFactory.createExtendedResearchCenter(15);
    Building level20Building = ExtendedBuildingFactory.createExtendedResearchCenter(20);
    
    assertTrue("Level 15 building should cost more than level 11", 
               level15Building.getScaledMetalCost() > level11Building.getScaledMetalCost());
    assertTrue("Level 20 building should cost more than level 15", 
               level20Building.getScaledMetalCost() > level15Building.getScaledMetalCost());
    
    // Test that bonuses also scale
    assertTrue("Level 15 building should have better bonus than level 11", 
               level15Building.getReseBonus() > level11Building.getReseBonus());
    assertTrue("Level 20 building should have best bonus", 
               level20Building.getReseBonus() > level15Building.getReseBonus());
  }

  /**
   * Test building tier descriptions
   */
  @Test
  public void testBuildingTierDescriptions() {
    Building level11Building = ExtendedBuildingFactory.createExtendedResearchCenter(11);
    Building level13Building = ExtendedBuildingFactory.createExtendedResearchCenter(13);
    Building level15Building = ExtendedBuildingFactory.createExtendedResearchCenter(15);
    Building level17Building = ExtendedBuildingFactory.createExtendedResearchCenter(17);
    Building level19Building = ExtendedBuildingFactory.createExtendedResearchCenter(19);
    
    assertEquals("Level 11 should be Advanced tier", "Advanced", level11Building.getTierDescription());
    assertEquals("Level 13 should be Elite tier", "Elite", level13Building.getTierDescription());
    assertEquals("Level 15 should be Superior tier", "Superior", level15Building.getTierDescription());
    assertEquals("Level 17 should be Mega tier", "Mega", level17Building.getTierDescription());
    assertEquals("Level 19 should be Ultimate tier", "Ultimate", level19Building.getTierDescription());
  }

  /**
   * Test building availability checking
   */
  @Test
  public void testBuildingAvailability() {
    // Test that buildings are available at correct tech levels
    assertTrue("Quantum research center should be available at level 11", 
               ExtendedBuildingFactory.isBuildingAvailable("Quantum research center", 11));
    assertFalse("Quantum research center should not be available at level 10", 
                ExtendedBuildingFactory.isBuildingAvailable("Quantum research center", 10));
    
    assertTrue("Ultimate research center should be available at level 20", 
               ExtendedBuildingFactory.isBuildingAvailable("Ultimate research center", 20));
    assertFalse("Ultimate research center should not be available at level 19", 
                ExtendedBuildingFactory.isBuildingAvailable("Ultimate research center", 19));
  }

  /**
   * Test cost calculator functionality
   */
  @Test
  public void testBuildingCostCalculator() {
    // Test cost scaling
    int baseCost = 1000;
    int scaledCost11 = BuildingCostCalculator.calculateMetalCost(baseCost, 11, BuildingType.RESEARCH);
    int scaledCost15 = BuildingCostCalculator.calculateMetalCost(baseCost, 15, BuildingType.RESEARCH);
    assertTrue("Level 15 should cost more than level 11", scaledCost15 > scaledCost11);
    
    // Test bonus scaling
    int baseBonus = 5;
    int scaledBonus11 = BuildingCostCalculator.calculateResearchBonus(baseBonus, 11);
    int scaledBonus15 = BuildingCostCalculator.calculateResearchBonus(baseBonus, 15);
    assertTrue("Level 15 should have better bonus than level 11", scaledBonus15 > scaledBonus11);
  }

  /**
   * Test regression: ensure original tech levels still work
   */
  @Test
  public void testRegressionOriginalTechLevels() {
    // Test that Tech class still works with original levels
    Tech level1Tech = new Tech("Test Tech", TechType.Combat, 1);
    Tech level10Tech = new Tech("Test Tech", TechType.Combat, 10);
    
    assertNotNull("Level 1 tech should work", level1Tech);
    assertNotNull("Level 10 tech should work", level10Tech);
    assertEquals("Level 1 tech should have correct level", 1, level1Tech.getLevel());
    assertEquals("Level 10 tech should have correct level", 10, level10Tech.getLevel());
    
    // Test that TechList works with original levels
    TechList standardList = new TechList(race, false);
    assertNotNull("Standard tech list should work", standardList);
  }

  /**
   * Test edge cases and boundary conditions
   */
  @Test
  public void testEdgeCases() {
    // Test boundary levels
    Tech level10Tech = new Tech("Test Tech", TechType.Combat, 10);
    Tech level11Tech = new Tech("Test Tech", TechType.Combat, 11);
    
    assertNotNull("Level 10 tech should work", level10Tech);
    assertNotNull("Level 11 tech should work", level11Tech);
    assertNotEquals("Level 10 and 11 should be different", 
                    level10Tech.getLevel(), level11Tech.getLevel());
    
    // Test building creation at boundary levels
    Building level11Building = ExtendedBuildingFactory.createExtendedResearchCenter(11);
    Building level20Building = ExtendedBuildingFactory.createExtendedResearchCenter(20);
    
    assertEquals("Level 11 building should have correct level", 11, level11Building.getRequiredTechLevel());
    assertEquals("Level 20 building should have correct level", 20, level20Building.getRequiredTechLevel());
  }
}
