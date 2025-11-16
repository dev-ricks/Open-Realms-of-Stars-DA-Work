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

/**
 * Utility class for calculating building costs and production balance
 * for extended tech levels (11-20)
 */
public final class BuildingCostCalculator {

  // Base cost multipliers for different building types
  private static final double RESEARCH_BASE_MULTIPLIER = 0.8;
  private static final double PRODUCTION_BASE_MULTIPLIER = 1.2;
  private static final double MILITARY_BASE_MULTIPLIER = 1.5;
  private static final double CULTURE_BASE_MULTIPLIER = 1.0;
  private static final double ECONOMIC_BASE_MULTIPLIER = 1.1;

  // Level scaling factors
  private static final double LEVEL_15_MULTIPLIER = 1.5;
  private static final double LEVEL_18_MULTIPLIER = 2.0;
  private static final double LEVEL_20_MULTIPLIER = 3.0;

  // Cost progression factors
  private static final double METAL_COST_PROGRESSION = 1.3;
  private static final double PRODUCTION_COST_PROGRESSION = 1.25;
  private static final double MAINTENANCE_COST_PROGRESSION = 1.15;

  // Production balance factors
  private static final double RESEARCH_EFFICIENCY_FACTOR = 1.2;
  private static final double PRODUCTION_EFFICIENCY_FACTOR = 1.1;
  private static final double MATERIAL_EFFICIENCY_FACTOR = 1.3;

  /**
   * Calculate metal cost for extended building
   * @param baseCost Base metal cost
   * @param techLevel Technology level (11-20)
   * @param buildingType Type of building
   * @return Calculated metal cost
   */
  public static int calculateMetalCost(int baseCost, int techLevel, BuildingType buildingType) {
    if (techLevel <= 10) {
      return baseCost;
    }

    double cost = baseCost;
    
    // Apply building type multiplier
    cost *= getBuildingTypeMultiplier(buildingType);
    
    // Apply level progression
    int levelsAbove10 = techLevel - 10;
    cost *= Math.pow(METAL_COST_PROGRESSION, levelsAbove10);
    
    // Apply tier multipliers
    if (techLevel >= 15) {
      cost *= LEVEL_15_MULTIPLIER;
    }
    if (techLevel >= 18) {
      cost *= LEVEL_18_MULTIPLIER;
    }
    if (techLevel >= 20) {
      cost *= LEVEL_20_MULTIPLIER;
    }
    
    return (int) Math.round(cost);
  }

  /**
   * Calculate production cost for extended building
   * @param baseCost Base production cost
   * @param techLevel Technology level (11-20)
   * @param buildingType Type of building
   * @return Calculated production cost
   */
  public static int calculateProductionCost(int baseCost, int techLevel, BuildingType buildingType) {
    if (techLevel <= 10) {
      return baseCost;
    }

    double cost = baseCost;
    
    // Apply building type multiplier
    cost *= getBuildingTypeMultiplier(buildingType);
    
    // Apply level progression
    int levelsAbove10 = techLevel - 10;
    cost *= Math.pow(PRODUCTION_COST_PROGRESSION, levelsAbove10);
    
    // Apply tier multipliers
    if (techLevel >= 15) {
      cost *= LEVEL_15_MULTIPLIER;
    }
    if (techLevel >= 18) {
      cost *= LEVEL_18_MULTIPLIER;
    }
    if (techLevel >= 20) {
      cost *= LEVEL_20_MULTIPLIER;
    }
    
    return (int) Math.round(cost);
  }

  /**
   * Calculate maintenance cost for extended building
   * @param baseCost Base maintenance cost
   * @param techLevel Technology level (11-20)
   * @param buildingType Type of building
   * @return Calculated maintenance cost
   */
  public static double calculateMaintenanceCost(double baseCost, int techLevel, BuildingType buildingType) {
    if (techLevel <= 10) {
      return baseCost;
    }

    double cost = baseCost;
    
    // Apply level progression
    int levelsAbove10 = techLevel - 10;
    cost *= Math.pow(MAINTENANCE_COST_PROGRESSION, levelsAbove10);
    
    // Apply tier multipliers
    if (techLevel >= 15) {
      cost *= LEVEL_15_MULTIPLIER;
    }
    if (techLevel >= 18) {
      cost *= LEVEL_18_MULTIPLIER;
    }
    
    return Math.round(cost * 10.0) / 10.0; // Round to 1 decimal place
  }

  /**
   * Calculate production bonus for extended building
   * @param baseBonus Base production bonus
   * @param techLevel Technology level (11-20)
   * @param buildingType Type of building
   * @return Calculated production bonus
   */
  public static int calculateProductionBonus(int baseBonus, int techLevel, BuildingType buildingType) {
    if (techLevel <= 10) {
      return baseBonus;
    }

    double bonus = baseBonus;
    
    // Apply efficiency factors based on building type
    if (buildingType == BuildingType.RESEARCH) {
      bonus *= RESEARCH_EFFICIENCY_FACTOR;
    } else if (buildingType == BuildingType.FACTORY) {
      bonus *= PRODUCTION_EFFICIENCY_FACTOR;
    }
    
    // Apply level scaling with diminishing returns
    int levelsAbove10 = techLevel - 10;
    if (levelsAbove10 <= 4) {
      bonus *= (1.0 + levelsAbove10 * 0.3); // Levels 11-14: +30% per level
    } else if (levelsAbove10 <= 8) {
      bonus *= (2.2 + (levelsAbove10 - 4) * 0.25); // Levels 15-18: +25% per level
    } else {
      bonus *= (3.2 + (levelsAbove10 - 8) * 0.2); // Levels 19-20: +20% per level
    }
    
    return (int) Math.round(bonus);
  }

  /**
   * Calculate research bonus for extended building
   * @param baseBonus Base research bonus
   * @param techLevel Technology level (11-20)
   * @return Calculated research bonus
   */
  public static int calculateResearchBonus(int baseBonus, int techLevel) {
    if (techLevel <= 10) {
      return baseBonus;
    }

    double bonus = baseBonus;
    
    // Research buildings get better scaling to encourage tech progression
    int levelsAbove10 = techLevel - 10;
    if (levelsAbove10 <= 4) {
      bonus *= (1.0 + levelsAbove10 * 0.35); // Levels 11-14: +35% per level
    } else if (levelsAbove10 <= 8) {
      bonus *= (2.4 + (levelsAbove10 - 4) * 0.3); // Levels 15-18: +30% per level
    } else {
      bonus *= (3.6 + (levelsAbove10 - 8) * 0.25); // Levels 19-20: +25% per level
    }
    
    return (int) Math.round(bonus);
  }

  /**
   * Calculate material bonus for extended building
   * @param baseBonus Base material bonus
   * @param techLevel Technology level (11-20)
   * @return Calculated material bonus
   */
  public static int calculateMaterialBonus(int baseBonus, int techLevel) {
    if (techLevel <= 10) {
      return baseBonus;
    }

    double bonus = baseBonus;
    
    // Material production scales well to support higher-level construction
    int levelsAbove10 = techLevel - 10;
    bonus *= (1.0 + levelsAbove10 * 0.4); // +40% per level
    
    // Apply material efficiency factor for better balance
    bonus *= MATERIAL_EFFICIENCY_FACTOR;
    
    return (int) Math.round(bonus);
  }

  /**
   * Get building type cost multiplier
   * @param buildingType Type of building
   * @return Cost multiplier
   */
  private static double getBuildingTypeMultiplier(BuildingType buildingType) {
    switch (buildingType) {
      case RESEARCH:
        return RESEARCH_BASE_MULTIPLIER;
      case FACTORY:
        return PRODUCTION_BASE_MULTIPLIER;
      case MILITARY:
        return MILITARY_BASE_MULTIPLIER;
      case CULTURE:
        return CULTURE_BASE_MULTIPLIER;
      case MINE:
      case FARM:
        return ECONOMIC_BASE_MULTIPLIER;
      default:
        return 1.0;
    }
  }

  /**
   * Calculate return on investment (ROI) for building
   * @param building Building to calculate ROI for
   * @param turnsToBuild Expected turns to complete
   * @return ROI percentage
   */
  public static double calculateROI(Building building, int turnsToBuild) {
    double totalCost = building.getScaledMetalCost() + building.getScaledProdCost();
    double totalBenefit = 0;
    
    // Calculate benefit per turn based on building type
    // Use more realistic valuation for extended buildings
    totalBenefit += building.getReseBonus() * 5.0; // Research valued at 5x (higher for extended)
    totalBenefit += building.getFactBonus() * 3.0; // Production valued at 3x (higher for extended)
    totalBenefit += building.getMaterialBonus() * 8.0; // Materials valued at 8x (critical for extended)
    totalBenefit += building.getFarmBonus() * 2.0; // Food valued at 2x
    totalBenefit += building.getMineBonus() * 2.5; // Mining valued at 2.5x
    totalBenefit += building.getCultBonus() * 2.0; // Culture valued at 2x
    totalBenefit += building.getCredBonus() * 1.5; // Credits valued at 1.5x
    
    // Subtract maintenance cost (but don't penalize too heavily for extended buildings)
    totalBenefit -= building.getScaledMaintenanceCost() * 0.5; // Reduced maintenance impact
    
    // Calculate ROI over 100 turns
    double totalBenefitOverTime = totalBenefit * 100;
    double roi = ((totalBenefitOverTime - totalCost) / totalCost) * 100;
    
    return Math.round(roi * 10.0) / 10.0; // Round to 1 decimal place
  }

  /**
   * Check if building is cost-effective for its tech level
   * @param building Building to check
   * @return True if cost-effective
   */
  public static boolean isCostEffective(Building building) {
    if (!building.isExtendedTechBuilding()) {
      return true; // Standard buildings are assumed balanced
    }
    
    double roi = calculateROI(building, getEstimatedBuildTime(building));
    
    // ROI thresholds based on tech level - adjusted for extended building costs
    int techLevel = building.getRequiredTechLevel();
    double threshold;
    
    switch (techLevel) {
      case 11:
      case 12:
        threshold = 25.0; // Lower threshold for early extended buildings
        break;
      case 13:
      case 14:
        threshold = 0.0; // Break-even for early-mid tier
        break;
      case 15:
      case 16:
        threshold = -100.0; // Accept negative ROI for mid-tier
        break;
      case 17:
      case 18:
        threshold = -200.0; // Accept more negative ROI for high-tier
        break;
      case 19:
      case 20:
        threshold = -300.0; // Accept significant negative ROI for ultimate buildings
        break;
      default:
        threshold = 0.0;
        break;
    }
    
    return roi >= threshold;
  }

  /**
   * Estimate build time based on production costs
   * @param building Building to estimate for
   * @return Estimated build time in turns
   */
  private static int getEstimatedBuildTime(Building building) {
    // Assume average production of 20 per turn for estimation
    return (int) Math.ceil(building.getScaledProdCost() / 20.0);
  }

  /**
   * Get balance recommendations for building
   * @param building Building to analyze
   * @return Balance recommendations
   */
  public static String getBalanceRecommendations(Building building) {
    StringBuilder recommendations = new StringBuilder();
    
    if (!building.isExtendedTechBuilding()) {
      return "Standard building - no extended balance needed.";
    }
    
    double roi = calculateROI(building, getEstimatedBuildTime(building));
    
    if (roi < 100) {
      recommendations.append("Low ROI - consider reducing costs or increasing bonuses. ");
    } else if (roi > 500) {
      recommendations.append("Very high ROI - consider increasing costs for better balance. ");
    } else {
      recommendations.append("Good ROI balance. ");
    }
    
    int techLevel = building.getRequiredTechLevel();
    if (techLevel >= 18) {
      recommendations.append("Mega-tier building - ensure it provides game-changing benefits. ");
    }
    
    if (building.getScaledMaintenanceCost() > 10) {
      recommendations.append("High maintenance - ensure benefits justify ongoing cost. ");
    }
    
    return recommendations.toString().trim();
  }
}
