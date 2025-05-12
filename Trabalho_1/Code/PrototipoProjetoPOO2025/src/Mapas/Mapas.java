package Mapas;

import auxiliar.Posicao;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Mapa class represents the definition of a game map including
 * ice barriers, villain positions, and fruits.
 */
public class Mapas implements Serializable {
    private int level;
    private ArrayList<Posicao> icePositions;
    private ArrayList<Posicao> villainPositions;
    private ArrayList<Posicao> fruitPositions;
    private Posicao heroStartPosition;
    
    public Mapas(int level) {
        this.level = level;
        this.icePositions = new ArrayList<>();
        this.villainPositions = new ArrayList<>();
        this.fruitPositions = new ArrayList<>();
        this.heroStartPosition = new Posicao(1, 1); // Default hero position
        
        // Initialize map layout based on level
        initializeMap();
    }
    
    /**
     * Initialize map layout for the current level
     */
    private void initializeMap() {
        switch(level) {
            case 1:
                initializeLevel1Map();
                break;
            case 2:
                initializeLevel2Map();
                break;
            case 3:
                initializeLevel3Map();
                break;
            case 4:
                initializeLevel4Map();
                break;
            case 5:
                initializeLevel5Map();
                break;
            default:
                initializeLevel1Map(); // Default to level 1
        }
    }
    
    /**
     * Initialize map layout for level 1
     */
    private void initializeLevel1Map() {
        // Set hero start position


        // Add ice barriers (example layout)
        // Create a border around the edges of the map
        // Top edge
        for (int i = 0; i < 15; i++) {
            addIcePosition(0, i);
        }
        
        // Bottom edge
        for (int i = 0; i < 15; i++) {
            addIcePosition(39, i);
        }
        
        // Left edge
        for (int i = 1; i < 39; i++) {
            addIcePosition(i, 0);
        }
        
        // Right edge
        for (int i = 1; i < 39; i++) {
            addIcePosition(i, 14);
        }
        
        // Add some internal ice barriers
        addIcePosition(5, 5);
        addIcePosition(5, 6);
        addIcePosition(5, 7);
        addIcePosition(9, 8);
        addIcePosition(9, 9);
        addIcePosition(9, 10);
    }
    
    /**
     * Initialize map layout for level 2
     */
    private void initializeLevel2Map() {
        // Set hero start position
        setHeroStartPosition(1, 1);
        
        // Add ice barriers (example layout)
        // Create a border around the edges of the map
        // Top edge
        for (int i = 0; i < 15; i++) {
            addIcePosition(0, i);
        }
        
        // Bottom edge
        for (int i = 0; i < 15; i++) {
            addIcePosition(14, i);
        }
        
        // Left edge
        for (int i = 1; i < 14; i++) {
            addIcePosition(i, 0);
        }
        
        // Right edge
        for (int i = 1; i < 14; i++) {
            addIcePosition(i, 14);
        }
        
        // Add some internal ice barriers in a maze-like pattern
        for (int i = 3; i < 12; i += 2) {
            for (int j = 3; j < 12; j += 4) {
                addIcePosition(i, j);
                addIcePosition(i, j+1);
            }
        }
    }
    
    /**
     * Initialize map layout for level 3
     */
    private void initializeLevel3Map() {
        // Placeholder for level 3 map configuration
        setHeroStartPosition(1, 1);
    }
    
    /**
     * Initialize map layout for level 4
     */
    private void initializeLevel4Map() {
        // Placeholder for level 4 map configuration
        setHeroStartPosition(1, 1);
    }
    
    /**
     * Initialize map layout for level 5
     */
    private void initializeLevel5Map() {
        // Placeholder for level 5 map configuration
        setHeroStartPosition(1, 1);
    }
    
    /**
     * Adds an ice barrier position to the map
     * @param linha Row position
     * @param coluna Column position
     */
    public void addIcePosition(int linha, int coluna) {
        icePositions.add(new Posicao(linha, coluna));
    }
    
    /**
     * Adds a villain position to the map
     * @param linha Row position
     * @param coluna Column position
     */
    public void addVillainPosition(int linha, int coluna) {
        villainPositions.add(new Posicao(linha, coluna));
    }
    
    /**
     * Adds a fruit position to the map
     * @param linha Row position
     * @param coluna Column position
     */
    public void addFruitPosition(int linha, int coluna) {
        fruitPositions.add(new Posicao(linha, coluna));
    }
    
    /**
     * Sets the hero's starting position
     * @param linha Row position
     * @param coluna Column position
     */
    public void setHeroStartPosition(int linha, int coluna) {
        this.heroStartPosition = new Posicao(linha, coluna);
    }
    
    // Getters
    public int getLevel() {
        return level;
    }
    
    public ArrayList<Posicao> getIcePositions() {
        return icePositions;
    }
    
    public ArrayList<Posicao> getVillainPositions() {
        return villainPositions;
    }
    
    public ArrayList<Posicao> getFruitPositions() {
        return fruitPositions;
    }
    
    public Posicao getHeroStartPosition() {
        return heroStartPosition;
    }

    /**
     * Initialize the current level with its elements
     */
    private void initializeLevel() {
        // Create hero
        Hero hero = new Hero("Char_1.png");
        
        // Set hero position based on map - REMOVE THIS LINE
        // Posicao heroPos = mapa.getHeroStartPosition();
        // hero.setPosicao(heroPos.getLinha(), heroPos.getColuna());

        // Add ice barriers from map
        addIceBarriers();

        // ... existing code ...
    }
} 