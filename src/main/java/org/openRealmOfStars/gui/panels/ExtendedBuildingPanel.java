package org.openRealmOfStars.gui.panels;
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.openRealmOfStars.gui.borders.SimpleBorder;
import org.openRealmOfStars.gui.labels.InfoTextArea;
import org.openRealmOfStars.gui.labels.SpaceLabel;
import org.openRealmOfStars.starMap.planet.construction.Building;
import org.openRealmOfStars.starMap.planet.construction.ExtendedBuildingFactory;

/**
 * Panel for displaying extended buildings with cost scaling and tier information
 */
public class ExtendedBuildingPanel extends BlackPanel {

  /** Serial version UID */
  private static final long serialVersionUID = 1L;

  /** Main panel for building content */
  private JPanel mainPanel;
  /** Scroll pane for building list */
  private JScrollPane scrollPane;

  /**
   * Constructor for ExtendedBuildingPanel
   */
  public ExtendedBuildingPanel() {
    this(11); // Default to showing level 11+ buildings
  }

  /**
   * Constructor for ExtendedBuildingPanel with specific tech level
   * @param maxTechLevel Maximum tech level to display buildings for
   */
  public ExtendedBuildingPanel(int maxTechLevel) {
    setLayout(new BorderLayout());
    setBorder(new SimpleBorder());
    
    createComponents();
    updateBuildingsForTechLevel(maxTechLevel);
  }

  /**
   * Create UI components
   */
  private void createComponents() {
    // Title panel
    JPanel titlePanel = new SpaceGreyPanel();
    titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    JLabel titleLabel = new SpaceLabel("Extended Buildings (Levels 11-20)");
    titleLabel.setForeground(Color.CYAN);
    titlePanel.add(titleLabel);
    
    // Main scrollable panel
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setOpaque(false);
    
    scrollPane = new JScrollPane(mainPanel);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBorder(null);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    
    add(titlePanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Update buildings display for specific tech level
   * @param maxTechLevel Maximum tech level
   */
  public void updateBuildingsForTechLevel(int maxTechLevel) {
    mainPanel.removeAll();
    
    if (maxTechLevel < 11) {
      showNoExtendedBuildingsMessage();
      return;
    }
    
    List<Building> buildings = ExtendedBuildingFactory.getAvailableBuildings(maxTechLevel);
    
    if (buildings.isEmpty()) {
      showNoExtendedBuildingsMessage();
      return;
    }
    
    // Group buildings by tier
    addBuildingTier("Advanced Tier (Levels 11-12)", buildings, 11, 12, new Color(100, 200, 255));
    addBuildingTier("Elite Tier (Levels 13-14)", buildings, 13, 14, new Color(150, 255, 150));
    addBuildingTier("Superior Tier (Levels 15-16)", buildings, 15, 16, new Color(255, 255, 100));
    addBuildingTier("Mega Tier (Levels 17-18)", buildings, 17, 18, new Color(255, 150, 100));
    addBuildingTier("Ultimate Tier (Levels 19-20)", buildings, 19, 20, new Color(255, 100, 255));
    
    mainPanel.revalidate();
    mainPanel.repaint();
  }

  /**
   * Show message when no extended buildings are available
   */
  private void showNoExtendedBuildingsMessage() {
    JPanel messagePanel = new SpaceGreyPanel();
    messagePanel.setLayout(new BorderLayout());
    
    InfoTextArea messageArea = new InfoTextArea();
    messageArea.setText("Extended buildings (levels 11-20) are not available.\n\n" +
        "To unlock extended buildings:\n" +
        "1. Enable extended tech levels in galaxy configuration\n" +
        "2. Research improvement technologies to level 11 or higher\n\n" +
        "Extended buildings provide powerful bonuses but require significant resources.");
    messageArea.setEditable(false);
    messageArea.setOpaque(false);
    
    messagePanel.add(messageArea, BorderLayout.CENTER);
    mainPanel.add(messagePanel);
  }

  /**
   * Add building tier section
   * @param tierName Name of the tier
   * @param buildings List of all buildings
   * @param minLevel Minimum level for this tier
   * @param maxLevel Maximum level for this tier
   * @param tierColor Color for this tier
   */
  private void addBuildingTier(String tierName, List<Building> buildings, 
                               int minLevel, int maxLevel, Color tierColor) {
    JPanel tierPanel = new SpaceGreyPanel();
    tierPanel.setLayout(new BorderLayout());
    tierPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(tierColor, 2),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    
    // Tier title
    JLabel tierLabel = new SpaceLabel(tierName);
    tierLabel.setForeground(tierColor);
    tierLabel.setFont(tierLabel.getFont().deriveFont(14f));
    tierPanel.add(tierLabel, BorderLayout.NORTH);
    
    // Buildings grid
    JPanel buildingsGrid = new JPanel();
    buildingsGrid.setLayout(new GridBagLayout());
    buildingsGrid.setOpaque(false);
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    int row = 0;
    for (Building building : buildings) {
      int level = building.getRequiredTechLevel();
      if (level >= minLevel && level <= maxLevel) {
        addBuildingToGrid(buildingsGrid, building, row, tierColor);
        row++;
      }
    }
    
    if (row > 0) {
      tierPanel.add(buildingsGrid, BorderLayout.CENTER);
      mainPanel.add(tierPanel);
      mainPanel.add(Box.createVerticalStrut(10));
    }
  }

  /**
   * Add building to grid panel
   * @param grid Grid panel to add to
   * @param building Building to add
   * @param row Row position
   * @param tierColor Tier color
   */
  private void addBuildingToGrid(JPanel grid, Building building, int row, Color tierColor) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.weightx = 0.0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.WEST;
    
    // Building name with tier indicator
    JLabel nameLabel = new SpaceLabel(building.getName());
    nameLabel.setForeground(tierColor);
    nameLabel.setFont(nameLabel.getFont().deriveFont(12f));
    grid.add(nameLabel, gbc);
    
    // Building type
    gbc.gridx = 1;
    gbc.weightx = 0.0;
    JLabel typeLabel = new SpaceLabel("[" + building.getType() + "]");
    typeLabel.setForeground(Color.GRAY);
    grid.add(typeLabel, gbc);
    
    // Level requirement
    gbc.gridx = 2;
    gbc.weightx = 0.0;
    JLabel levelLabel = new SpaceLabel("Level " + building.getRequiredTechLevel());
    levelLabel.setForeground(Color.YELLOW);
    grid.add(levelLabel, gbc);
    
    // Costs
    gbc.gridx = 3;
    gbc.weightx = 0.0;
    String costText = String.format("Metal: %d | Prod: %d | Maint: %.1f", 
        building.getScaledMetalCost(), 
        building.getScaledProdCost(), 
        building.getScaledMaintenanceCost());
    JLabel costLabel = new SpaceLabel(costText);
    costLabel.setForeground(Color.WHITE);
    grid.add(costLabel, gbc);
    
    // Bonuses
    gbc.gridx = 4;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    String bonusText = getBonusText(building);
    JLabel bonusLabel = new SpaceLabel(bonusText);
    bonusLabel.setForeground(Color.CYAN);
    grid.add(bonusLabel, gbc);
  }

  /**
   * Get formatted bonus text for building
   * @param building Building to get bonuses for
   * @return Formatted bonus text
   */
  private String getBonusText(Building building) {
    StringBuilder bonuses = new StringBuilder();
    
    if (building.getReseBonus() > 0) {
      bonuses.append("Research: +").append(building.getReseBonus()).append(" ");
    }
    if (building.getFactBonus() > 0) {
      bonuses.append("Production: +").append(building.getFactBonus()).append(" ");
    }
    if (building.getMaterialBonus() > 0) {
      bonuses.append("Material: +").append(building.getMaterialBonus()).append(" ");
    }
    if (building.getFarmBonus() > 0) {
      bonuses.append("Food: +").append(building.getFarmBonus()).append(" ");
    }
    if (building.getMineBonus() > 0) {
      bonuses.append("Mining: +").append(building.getMineBonus()).append(" ");
    }
    if (building.getCultBonus() > 0) {
      bonuses.append("Culture: +").append(building.getCultBonus()).append(" ");
    }
    if (building.getCredBonus() > 0) {
      bonuses.append("Credits: +").append(building.getCredBonus()).append(" ");
    }
    
    return bonuses.toString().trim();
  }

  /**
   * Get preferred size for the panel
   * @return Preferred dimension
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(800, 600);
  }

  /**
   * Set the tech level and update display
   * @param techLevel New tech level
   */
  public void setTechLevel(int techLevel) {
    updateBuildingsForTechLevel(techLevel);
  }
}
