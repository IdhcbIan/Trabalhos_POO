package Mapas;

import Auxiliar.Posicao;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Mapa class represents the definition of a game map including
 * villain positions, borders, and fruits.
 */
public class Mapa implements Serializable {
    private int level;
    private ArrayList<Posicao> borderPositions;
    private ArrayList<Posicao> villainPositions;
    private ArrayList<Posicao> fruitPositions;
    private Posicao heroStartPosition;
    
    public Mapa(int level) {
        this.level = level;
        this.borderPositions = new ArrayList<>();
        this.villainPositions = new ArrayList<>();
        this.fruitPositions = new ArrayList<>();
        this.heroStartPosition = new Posicao(1, 1); // Default hero position
    }
    
    /**
     * Adds a border position to the map
     * @param linha Row position
     * @param coluna Column position
     */
    public void addBorderPosition(int linha, int coluna) {
        borderPositions.add(new Posicao(linha, coluna));
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
    
    public ArrayList<Posicao> getBorderPositions() {
        return borderPositions;
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
} 