package Mapas;

import auxiliar.Posicao;
import java.util.ArrayList;

import Modelo.Hero;

import java.io.Serializable;

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
                initializeLevel1Map(); 
        }
    }
    
    private void initializeLevel1Map() {

        // Top Edge
        for (int i = 0; i < 15; i++) {
            addIcePosition(0, i);
        }
        
        // Bottom edge
        for (int i = 0; i < 15; i++) {
            addIcePosition(29, i);
        }
        
        // Left edge
        for (int i = 1; i < 29; i++) {
            addIcePosition(i, 0);
        }
        
        // Right edge
        for (int i = 1; i < 29; i++) {
            addIcePosition(i, 14);
        }




        // Trap 1 (4)
        for (int i = 1; i < 8; i++) {
            addIcePosition(3, i);
        }
        for (int i = 10; i < 14; i++) {
            addIcePosition(3, i);
        }

        for (int i = 1; i < 11; i++) {
            addIcePosition(5, i);
        }
        for (int i = 13; i < 14; i++) {
            addIcePosition(5, i);
        }
        //-----------------------------


        // Trap 2 (11)
        for (int i = 1; i < 4; i++) {
            addIcePosition(10, i);
        }
        for (int i = 6; i < 14; i++) {
            addIcePosition(10, i);
        }

        for (int i = 1; i < 7; i++) {
            addIcePosition(12, i);
        }
        for (int i = 9; i < 14; i++) {
            addIcePosition(12, i);
        }

        // Trap 3 (18)
        for (int i = 1; i < 8; i++) {
            addIcePosition(17, i);
        }
        for (int i = 10; i < 14; i++) {
            addIcePosition(17, i);
        }

        for (int i = 1; i < 11; i++) {
            addIcePosition(19, i);
        }
        for (int i = 13; i < 14; i++) {
            addIcePosition(19, i);
        }

        // Trap 4  (25)
        for (int i = 1; i < 4; i++) {
            addIcePosition(24, i);
        }
        for (int i = 6; i < 14; i++) {
            addIcePosition(24, i);
        }

        for (int i = 1; i < 7; i++) {
            addIcePosition(26, i);
        }
        for (int i = 9; i < 14; i++) {
            addIcePosition(26, i);
        }
    }
    
    private void initializeLevel2Map() {
        
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

        // Create a spiral pattern of ice blocks
        // Outer layer of spiral (clockwise from top-left)
        for (int i = 2; i <= 12; i++) {
            addIcePosition(2, i); // Top horizontal line
        }
        for (int i = 3; i <= 12; i++) {
            addIcePosition(i, 12); // Right vertical line
        }
        for (int i = 11; i >= 2; i--) {
            addIcePosition(12, i); // Bottom horizontal line
        }
        for (int i = 11; i >= 4; i--) {
            addIcePosition(i, 2); // Left vertical line (leaving an entrance)
        }

        // Second layer of spiral (clockwise from top-left)
        for (int i = 4; i <= 10; i++) {
            addIcePosition(4, i); // Top horizontal line
        }
        for (int i = 5; i <= 10; i++) {
            addIcePosition(i, 10); // Right vertical line
        }
        for (int i = 9; i >= 4; i--) {
            addIcePosition(10, i); // Bottom horizontal line
        }
        for (int i = 9; i >= 6; i--) {
            addIcePosition(i, 4); // Left vertical line (leaving an entrance)
        }

        // Inner layer of spiral (clockwise from top-left)
        for (int i = 6; i <= 8; i++) {
            addIcePosition(6, i); // Top horizontal line
        }
        for (int i = 7; i <= 8; i++) {
            addIcePosition(i, 8); // Right vertical line
        }
        addIcePosition(8, 7); // Bottom horizontal line (partial)
        addIcePosition(8, 6); // Bottom horizontal line (partial)
    }
    
    private void initializeLevel3Map() {
        // Placeholder for level 3 map configuration
        setHeroStartPosition(1, 1);
    }
    
    private void initializeLevel4Map() {
        // Placeholder for level 4 map configuration
        setHeroStartPosition(1, 1);
    }
    
    private void initializeLevel5Map() {
        // Placeholder for level 5 map configuration
        setHeroStartPosition(1, 1);
    }
    
    public void addIcePosition(int linha, int coluna) {
        icePositions.add(new Posicao(linha, coluna));
    }
    
    public void addVillainPosition(int linha, int coluna) {
        villainPositions.add(new Posicao(linha, coluna));
    }
    
    public void addFruitPosition(int linha, int coluna) {
        fruitPositions.add(new Posicao(linha, coluna));
    }
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

    private void initializeLevel() {
        Hero hero = new Hero("Char_1.png");
        
        addIceBarriers();

    }
} 