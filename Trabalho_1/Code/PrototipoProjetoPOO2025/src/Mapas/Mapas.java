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
    private ArrayList<Posicao> fogoPositions;
    private Posicao heroStartPosition;
    
    public Mapas(int level) {
        this.level = level;
        this.icePositions = new ArrayList<>();
        this.villainPositions = new ArrayList<>();
        this.fruitPositions = new ArrayList<>();
        this.fogoPositions = new ArrayList<>();
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



    //------------ Level 1 --------------------------------------
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




    //------------ Level 2 --------------------------------------
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




    //------------ Level 3 --------------------------------------
    private void initializeLevel3Map() {

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
        for (int i = 1; i < 4; i++) {
            addIcePosition(3, i);
        }
        for (int i = 6; i < 14; i++) {
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
        for (int i = 1; i < 9; i++) {
            addIcePosition(10, i);
        }
        for (int i = 11; i < 14; i++) {
            addIcePosition(10, i);
        }

        for (int i = 1; i < 2; i++) {
            addIcePosition(12, i);
        }
        for (int i = 4; i < 14; i++) {
            addIcePosition(12, i);
        }

        // Trap 3 (18)
        for (int i = 1; i < 4; i++) {
            addIcePosition(17, i);
        }
        for (int i = 6; i < 14; i++) {
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
        for (int i = 11; i < 14; i++) {
            addIcePosition(24, i);
        }

        for (int i = 1; i < 2; i++) {
            addIcePosition(26, i);
        }
        for (int i = 4; i < 14; i++) {
            addIcePosition(26, i);
        }
    }


    private void initializeLevel4Map() {
        // Placeholder for level 4 map configuration
        setHeroStartPosition(1, 1);
    }
    
    private void initializeLevel5Map() {
        // Hero starts in the northwest corner
        setHeroStartPosition(1, 1);

        // Outer boundary
        int maxRow = 29, maxCol = 14;
        for (int c = 0; c <= maxCol; c++) {
            addIcePosition(0, c);
            addIcePosition(maxRow, c);
        }
        for (int r = 1; r < maxRow; r++) {
            addIcePosition(r, 0);
            addIcePosition(r, maxCol);
        }

        // Create a more interesting maze-like structure with quadrants
        
        // Top-left quadrant: Zigzag pattern
        for (int i = 3; i <= 12; i += 3) {
            // Horizontal zigzag segments
            for (int j = 1; j < 6; j++) {
                addIcePosition(i, j);
            }
        }
        
        // Top-right quadrant: Spiral-like structure (inspired by level 2)
        for (int j = 8; j <= 12; j++) {
            addIcePosition(3, j); // Top horizontal line
        }
        for (int i = 4; i <= 9; i++) {
            addIcePosition(i, 12); // Right vertical line
        }
        for (int j = 11; j >= 8; j--) {
            addIcePosition(9, j); // Bottom horizontal line
        }
        for (int i = 8; i >= 5; i--) {
            addIcePosition(i, 8); // Left vertical line
        }
        for (int j = 9; j <= 10; j++) {
            addIcePosition(5, j); // Inner horizontal line
        }
        
        // Bottom-left quadrant: Room with columns
        for (int i = 17; i <= 26; i += 3) {
            for (int j = 2; j <= 5; j += 3) {
                addIcePosition(i, j);     // Column pattern
                addIcePosition(i, j + 1); // Make columns thicker
            }
        }
        
        // Bottom-right quadrant: Maze with dead ends
        for (int i = 17; i <= 24; i += 7) {
            for (int j = 8; j <= 12; j++) {
                addIcePosition(i, j);     // Horizontal barriers
            }
        }
        for (int i = 18; i <= 26; i++) {
            addIcePosition(i, 10);        // Vertical barrier
        }
        // Add some gaps for navigation
        addIcePosition(20, 10);           // Remove one ice block for path
        addIcePosition(17, 10);           // Remove one ice block for path
        
        // Central chamber with "plus" shape (modified from original)
        int centerR = maxRow / 2, centerC = maxCol / 2;
        
        // Horizontal bar of the plus
        for (int c = centerC - 4; c <= centerC + 4; c++) {
            if (c != centerC - 2 && c != centerC + 2) { // Create gaps for entry
                addIcePosition(centerR, c);
            }
        }
        
        // Vertical bar of the plus
        for (int r = centerR - 3; r <= centerR + 3; r++) {
            if (r != centerR - 1 && r != centerR + 1) { // Create gaps for entry
                addIcePosition(r, centerC);
            }
        }
        
        // Add some interior walls in the central chamber
        addIcePosition(centerR - 1, centerC - 1);
        addIcePosition(centerR + 1, centerC + 1);
        addIcePosition(centerR - 1, centerC + 1);
        addIcePosition(centerR + 1, centerC - 1);
        
        // Add villain positions for Level 5
        // Choose an appropriate position for the Villain_3
        addVillainPosition(13, 7); // Example position - adjust as needed
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
    
    public void addFogoPosition(int linha, int coluna) {
        fogoPositions.add(new Posicao(linha, coluna));
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
    
    public ArrayList<Posicao> getFogoPositions() {
        return fogoPositions;
    }
    
    public Posicao getHeroStartPosition() {
        return heroStartPosition;
    }

    private void initializeLevel() {
        Hero hero = new Hero("Char_1.png");
        
        addIceBarriers();
    }
} 