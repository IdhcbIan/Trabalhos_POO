package Mapas;

import auxiliar.Posicao;
import java.util.ArrayList;
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


        // Labirinth
        for (int i = 1; i < 9; i++) {
            addIcePosition(6, i);
        }
        for (int i = 10; i < 14; i++) {
            addIcePosition(6, i);
        }

        for (int i = 1; i < 12; i++) {
            addIcePosition(8, i);
        }
        for (int i = 13; i < 14; i++) {
            addIcePosition(8, i);
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
        
        // Add some internal ice barriers in a maze-like pattern
        for (int i = 3; i < 12; i += 2) {
            for (int j = 3; j < 12; j += 4) {
                addIcePosition(i, j);
                addIcePosition(i, j+1);
            }
        }
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