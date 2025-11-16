package org.openRealmOfStars.starMap.planet.construction;
/*
 * Open Realm of Stars game project
 * Copyright (C) 2024 Extended Tech Implementation
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

import java.util.ArrayList;
import java.util.List;

/**
 * Extended building factory for levels 11-20 with cost scaling and production balance
 */
public final class ExtendedBuildingFactory {

  /**
   * Create extended research building based on tech level
   * @param techLevel Technology level (11-20)
   * @return Research building with scaled properties
   */
  public static Building createExtendedResearchCenter(int techLevel) {
    String name = getResearchCenterName(techLevel);
    Building building = new Building(name, "ICON_RESEARCH", BuildingType.RESEARCH, techLevel);
    
    // Use balanced cost calculation
    int baseMetalCost = 1000;
    int baseProdCost = 800;
    double baseMaintenanceCost = 1.0;
    
    building.setMetalCost(BuildingCostCalculator.calculateMetalCost(baseMetalCost, techLevel, BuildingType.RESEARCH));
    building.setProdCost(BuildingCostCalculator.calculateProductionCost(baseProdCost, techLevel, BuildingType.RESEARCH));
    building.setMaintenanceCost(BuildingCostCalculator.calculateMaintenanceCost(baseMaintenanceCost, techLevel, BuildingType.RESEARCH));
    
    // Use balanced bonus calculation
    int baseResearchBonus = 6;
    building.setReseBonus(BuildingCostCalculator.calculateResearchBonus(baseResearchBonus, techLevel));
    
    building.setDescription("Advanced research facility providing " + building.getReseBonus() + 
        " research points. Requires technology level " + techLevel + 
        ". ROI: " + BuildingCostCalculator.calculateROI(building, 20) + "%");
    building.setSingleAllowed(true);
    
    return building;
  }

  /**
   * Create extended factory building based on tech level
   * @param techLevel Technology level (11-20)
   * @return Factory building with scaled properties
   */
  public static Building createExtendedFactory(int techLevel) {
    String name = getFactoryName(techLevel);
    Building building = new Building(name, "ICON_FACTORY", BuildingType.FACTORY, techLevel);
    
    // Use balanced cost calculation
    int baseMetalCost = 1200;
    int baseProdCost = 1000;
    double baseMaintenanceCost = 2.0;
    
    building.setMetalCost(BuildingCostCalculator.calculateMetalCost(baseMetalCost, techLevel, BuildingType.FACTORY));
    building.setProdCost(BuildingCostCalculator.calculateProductionCost(baseProdCost, techLevel, BuildingType.FACTORY));
    building.setMaintenanceCost(BuildingCostCalculator.calculateMaintenanceCost(baseMaintenanceCost, techLevel, BuildingType.FACTORY));
    
    // Use balanced bonus calculation
    int baseProductionBonus = 8;
    building.setFactBonus(BuildingCostCalculator.calculateProductionBonus(baseProductionBonus, techLevel, BuildingType.FACTORY));
    
    building.setDescription("Advanced production facility providing " + building.getFactBonus() + 
        " production bonus. Requires technology level " + techLevel + 
        ". ROI: " + BuildingCostCalculator.calculateROI(building, 25) + "%");
    
    return building;
  }

  /**
   * Create advanced replicator center for levels 11+
   * @param techLevel Technology level (11-20)
   * @return Replicator center with material generation capabilities
   */
  public static Building createAdvancedReplicatorCenter(int techLevel) {
    String name = getReplicatorName(techLevel);
    Building building = new Building(name, "ICON_FACTORY", BuildingType.FACTORY, techLevel);
    
    // Replicators are very expensive but powerful - use higher base costs
    int baseMetalCost = 2000;
    int baseProdCost = 1500;
    double baseMaintenanceCost = 3.0;
    
    building.setMetalCost(BuildingCostCalculator.calculateMetalCost(baseMetalCost, techLevel, BuildingType.FACTORY));
    building.setProdCost(BuildingCostCalculator.calculateProductionCost(baseProdCost, techLevel, BuildingType.FACTORY));
    building.setMaintenanceCost(BuildingCostCalculator.calculateMaintenanceCost(baseMaintenanceCost, techLevel, BuildingType.FACTORY));
    
    // Replicators provide both production and material bonuses
    int baseProductionBonus = 6;
    int baseMaterialBonus = 2;
    
    building.setFactBonus(BuildingCostCalculator.calculateProductionBonus(baseProductionBonus, techLevel, BuildingType.FACTORY));
    building.setMaterialBonus(BuildingCostCalculator.calculateMaterialBonus(baseMaterialBonus, techLevel));
    
    building.setDescription("Advanced matter replicator providing " + building.getFactBonus() + 
        " production and " + building.getMaterialBonus() + " material bonus. Requires technology level " + techLevel + 
        ". ROI: " + BuildingCostCalculator.calculateROI(building, 30) + "%");
    building.setSingleAllowed(true);
    
    return building;
  }

  /**
   * Get research center name based on tech level
   * @param techLevel Technology level
   * @return Research center name
   */
  private static String getResearchCenterName(int techLevel) {
    switch (techLevel) {
      case 11:
        return "Quantum research center";
      case 12:
        return "Dimensional research center";
      case 13:
        return "Temporal research center";
      case 14:
        return "Quantum computing center";
      case 15:
        return "Exotic research center";
      case 16:
        return "Transcendent research center";
      case 17:
        return "Omega research center";
      case 18:
        return "Infinity research center";
      case 19:
        return "Eternal research center";
      case 20:
        return "Ultimate research center";
      default:
        return "Advanced research center";
    }
  }

  /**
   * Get factory name based on tech level
   * @param techLevel Technology level
   * @return Factory name
   */
  private static String getFactoryName(int techLevel) {
    switch (techLevel) {
      case 11:
        return "Mega factory";
      case 12:
        return "Ultra factory";
      case 13:
        return "Giga factory";
      case 14:
        return "Terra factory";
      case 15:
        return "Peta factory";
      case 16:
        return "Exa factory";
      case 17:
        return "Zetta factory";
      case 18:
        return "Yotta factory";
      case 19:
        return "Bronto factory";
      case 20:
        return "Geoponto factory";
      default:
        return "Advanced factory";
    }
  }

  /**
   * Get replicator name based on tech level
   * @param techLevel Technology level
   * @return Replicator name
   */
  private static String getReplicatorName(int techLevel) {
    switch (techLevel) {
      case 11:
        return "Advanced replicator center";
      case 12:
        return "Matter synthesizer";
      case 13:
        return "Nanite assembler";
      case 14:
        return "Molecular printer";
      case 15:
        return "Zero-point fabricator";
      case 16:
        return "Antimatter converter";
      case 17:
        return "Dark matter fabricator";
      case 18:
        return "Reality synthesizer";
      case 19:
        return "Cosmic assembler";
      case 20:
        return "Genesis device";
      default:
        return "Advanced replicator";
    }
  }

  /**
   * Get all available buildings for a tech level
   * @param techLevel Maximum tech level available
   * @return List of buildings that can be built
   */
  public static List<Building> getAvailableBuildings(int techLevel) {
    List<Building> buildings = new ArrayList<>();
    
    if (techLevel >= 11) {
      buildings.add(createExtendedResearchCenter(11));
      buildings.add(createExtendedFactory(11));
      buildings.add(createAdvancedReplicatorCenter(11));
    }
    
    if (techLevel >= 12) {
      buildings.add(createExtendedResearchCenter(12));
      buildings.add(createExtendedFactory(12));
      buildings.add(createAdvancedReplicatorCenter(12));
    }
    
    if (techLevel >= 13) {
      buildings.add(createExtendedResearchCenter(13));
      buildings.add(createExtendedFactory(13));
      buildings.add(createAdvancedReplicatorCenter(13));
    }
    
    if (techLevel >= 14) {
      buildings.add(createExtendedResearchCenter(14));
      buildings.add(createExtendedFactory(14));
      buildings.add(createAdvancedReplicatorCenter(14));
    }
    
    if (techLevel >= 15) {
      buildings.add(createExtendedResearchCenter(15));
      buildings.add(createExtendedFactory(15));
      buildings.add(createAdvancedReplicatorCenter(15));
    }
    
    if (techLevel >= 16) {
      buildings.add(createExtendedResearchCenter(16));
      buildings.add(createExtendedFactory(16));
      buildings.add(createAdvancedReplicatorCenter(16));
    }
    
    if (techLevel >= 17) {
      buildings.add(createExtendedResearchCenter(17));
      buildings.add(createExtendedFactory(17));
      buildings.add(createAdvancedReplicatorCenter(17));
    }
    
    if (techLevel >= 18) {
      buildings.add(createExtendedResearchCenter(18));
      buildings.add(createExtendedFactory(18));
      buildings.add(createAdvancedReplicatorCenter(18));
    }
    
    if (techLevel >= 19) {
      buildings.add(createExtendedResearchCenter(19));
      buildings.add(createExtendedFactory(19));
      buildings.add(createAdvancedReplicatorCenter(19));
    }
    
    if (techLevel >= 20) {
      buildings.add(createExtendedResearchCenter(20));
      buildings.add(createExtendedFactory(20));
      buildings.add(createAdvancedReplicatorCenter(20));
    }
    
    return buildings;
  }

  /**
   * Calculate cost multiplier for balancing
   * @param techLevel Technology level
   * @param buildingType Type of building
   * @return Cost multiplier for balance
   */
  public static double getCostMultiplier(int techLevel, BuildingType buildingType) {
    return BuildingCostCalculator.calculateMetalCost(100, techLevel, buildingType) / 100.0;
  }

  /**
   * Check if building is available for tech level
   * @param buildingName Building name to check
   * @param techLevel Current tech level
   * @return True if building is available
   */
  public static boolean isBuildingAvailable(String buildingName, int techLevel) {
    if (techLevel < 11) {
      return false;
    }
    
    switch (buildingName) {
      case "Quantum research center":
      case "Mega factory":
      case "Advanced replicator center":
        return techLevel >= 11;
      case "Dimensional research center":
      case "Ultra factory":
      case "Matter synthesizer":
        return techLevel >= 12;
      case "Temporal research center":
      case "Giga factory":
      case "Nanite assembler":
        return techLevel >= 13;
      case "Quantum computing center":
      case "Terra factory":
      case "Molecular printer":
        return techLevel >= 14;
      case "Exotic research center":
      case "Peta factory":
      case "Zero-point fabricator":
        return techLevel >= 15;
      case "Transcendent research center":
      case "Exa factory":
      case "Antimatter converter":
        return techLevel >= 16;
      case "Omega research center":
      case "Zetta factory":
      case "Dark matter fabricator":
        return techLevel >= 17;
      case "Infinity research center":
      case "Yotta factory":
      case "Reality synthesizer":
        return techLevel >= 18;
      case "Eternal research center":
      case "Bronto factory":
      case "Cosmic assembler":
        return techLevel >= 19;
      case "Ultimate research center":
      case "Geoponto factory":
      case "Genesis device":
        return techLevel >= 20;
      default:
        return false;
    }
  }
}
