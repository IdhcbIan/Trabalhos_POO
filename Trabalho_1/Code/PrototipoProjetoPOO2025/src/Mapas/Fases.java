package Mapas;

import Modelo.*;
import auxiliar.Posicao;
import java.util.ArrayList;

/**
 * Fases class represents a single game level.
 * It contains the map layout and all game elements for a specific level.
 */
public class Fases {
    private int level;
    private ArrayList<Personagem> elementos;
    private Hero hero;
    private Mapas mapa;

    /**
     * Constructor for a single phase
     * @param level The level number
     */
    public Fases(int level) {
        this.level = level;
        this.elementos = new ArrayList<>();
        this.mapa = new Mapas(level);
        initializeLevel();
    }

    /**
     * Initialize the current level with its elements
     */
    private void initializeLevel() {
        // Create hero
        hero = new Hero("Char_1.png");
        
        // Add ice barriers from map
        addIceBarriers();


        // Configure level based on level number
        switch(level) {
            case 1:
                configureLevel1();
                break;
            case 2:
                configureLevel2();
                break;
            case 3:
                configureLevel3();
                break;
            case 4:
                configureLevel4();
                break;
            case 5:
                configureLevel5();
                break;
            default:
                configureLevel1(); // Default to level 1
        }

        // Add hero to elements list (must be added after level configuration to ensure it's on top)
        this.elementos.add(hero);
    }
    
    /**
     * Configure level 1
     */
    private void configureLevel1() {
        // Set hero position for level 1
        hero.setPosicao(2, 7);
        
        // Add fruits
        addFruta(4, 8);
        addFruta(11, 4);
        addFruta(18, 8);
        addFruta(25, 4);

        // Add villains - uncomment and modify as needed
        addVilao(4, 1, true); 
        addVilao(11, 1, true); 
        addVilao(18, 1, true); 
        addVilao(25, 1, true); 
    }

    /**
     * Configure level 2
     */
    private void configureLevel2() {
        // Set hero position for level 2
        hero.setPosicao(1, 1);
        
        // Add fruits
        addFruta(1, 10);
        addFruta(3, 8);
        addFruta(5, 6);
        addFruta(7, 4);
    
        addFrutaVert(6, 9);


        // Add villains
        addVilao(3, 1, true);
    }

    /**
     * Configure level 3
     */
    private void configureLevel3() {
        hero.setPosicao(2, 7);
        
        // Add fruits
        addFruta(4, 8);
        addFruta(11, 4);
        addFruta(18, 8);
        addFruta(25, 4);

        // Add villains with rate and direction parameters
        // Parameters: (row, column, rate, shootRight)
        addVilao2(4, 1, 20, true);   // Move to column 2 (away from wall) and shoot right
        addVilao2(11, 13, 20, false);  // Move to column 2 (away from wall) and shoot right
        
        // Updated positions for villains 3 and 4
        addVilao2(18, 1, 20, true);  // Move to column 2 (away from wall) and shoot right
        addVilao2(25, 13, 20, false);  // Move to column 2 (away from wall) and shoot right
    }

    /**
     * Configure level 4
     */
    private void configureLevel4() {
        // Set hero position for level 4
        hero.setPosicao(1, 1);
        
        // Add fruits and villains for level 4
        // This is a placeholder - customize as needed
    }

    /**
     * Configure level 5
     */
    private void configureLevel5() {
        // Set hero position
        Posicao heroPos = mapa.getHeroStartPosition();
        hero.setPosicao(heroPos.getLinha(), heroPos.getColuna());
        
        // Add fruits based on positions defined in the map
        ArrayList<Posicao> fruitPositions = mapa.getFruitPositions();
        for (Posicao pos : fruitPositions) {
            Fruta fruta = new Fruta("Fruit_1.png");
            fruta.setPosicao(pos.getLinha(), pos.getColuna());
            this.elementos.add(fruta);
        }
        
        // Add two fire strips across the entire map width (3 rows each)
        // First strip - upper part of the map (rows 5-7)
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 14; col++) {
                addFogo(row, col);
            }
        }
        
        // Second strip - lower part of the map (rows 20-22)
        for (int row = 20; row < 23; row++) {
            for (int col = 0; col < 14; col++) {
                addFogo(row, col);
            }
        }
        
        // Add villains based on positions defined in the map
        ArrayList<Posicao> villainPositions = mapa.getVillainPositions();
        
        // CHANGED: Just add a single Villan_3 at the first villain position
        if (!villainPositions.isEmpty()) {
            Posicao firstPos = villainPositions.get(0);
            addVilao3(firstPos.getLinha(), firstPos.getColuna(), 3);
        }
        
        // Then add the Villan_2 instances
        for (Posicao pos : villainPositions) {
            // Create a shooting villain that targets the hero
            Villan_2 vilao = new Villan_2("Villan_1.png");
            vilao.setPosicao(pos.getLinha(), pos.getColuna());
            vilao.setTarget(hero);
            
            // Determine shooting direction based on position relative to center
            int maxRow = 29, maxCol = 14;
            int centerR = maxRow / 2, centerC = maxCol / 2;
            
            boolean shootRight = pos.getColuna() < centerC;
            vilao.setShootRate(20);
            vilao.setShootDirection(shootRight);
            
            this.elementos.add(vilao);
        }
    }

    /**
     * Helper method to add a Villan_3 with specified movement rate
     * @param linha Row position
     * @param coluna Column position
     * @param moveRate Movement rate (lower = faster)
     */
    private void addVilao3(int linha, int coluna, int moveRate) {
        Villan_3 vilao = new Villan_3("Villan_3.png");
        vilao.setPosicao(linha, coluna);
        vilao.setTarget(hero);        // Set the hero as the target
        vilao.setMoveRate(moveRate);  // Set the movement rate (higher = slower)
        this.elementos.add(vilao);
    }

    /**
     * Helper method to add a fruit to the phase
     */
    private void addFruta(int linha, int coluna) {
        Fruta fruta = new Fruta("Fruit_1.png");
        fruta.setPosicao(linha, coluna);
        this.elementos.add(fruta);
    }

    /**
     * Helper method to add a villain to the phase
     * @param linha Row position
     * @param coluna Column position
     * @param isZigueZague If true, creates ZigueZague, otherwise creates Caveira
     */
    private void addVilao(int linha, int coluna, boolean isZigueZague) {
        Villan_1 vilao = new Villan_1("Villan_2.png");
        vilao.setPosicao(linha, coluna);
        this.elementos.add(vilao);
    }

    /**
     * Add ice barriers based on map configuration
     */
    private void addIceBarriers() {
        ArrayList<Posicao> icePositions = mapa.getIcePositions();
        for (Posicao pos : icePositions) {
            Ice ice = new Ice("Ice.png");
            ice.setPosicao(pos.getLinha(), pos.getColuna());
            this.elementos.add(ice);
        }
    }

    /**
     * Helper method to add a villain type 2 with shooting parameters
     * @param linha Row position
     * @param coluna Column position
     * @param rate Shooting rate (higher = slower)
     * @param shootRight Direction to shoot (true = right, false = left)
     */
    private void addVilao2(int linha, int coluna, int rate, boolean shootRight) {
        Villan_2 vilao = new Villan_2("Villan_1.png");
        vilao.setPosicao(linha, coluna);
        vilao.setTarget(hero);       // Set the hero as the target
        vilao.setShootRate(rate);    // Set the shooting rate
        vilao.setShootDirection(shootRight); // Set shooting direction
        this.elementos.add(vilao);
    }

    /**
     * Helper method to add a fire block to the phase
     */
    private void addFogo(int linha, int coluna) {
        Fogo fogo = new Fogo("Fire.png"); // Make sure you have a fire image in your resources
        fogo.setPosicao(linha, coluna);
        this.elementos.add(fogo);
    }

    /**
     * Helper method to add a vertical fruit to the phase
     */
    private void addFrutaVert(int linha, int coluna) {
        FrutaVert frutaVert = new FrutaVert("Fruit_2.png");
        frutaVert.setPosicao(linha, coluna);
        this.elementos.add(frutaVert);
    }

    /**
     * Gets all game elements for this phase
     * @return List of personagem objects for the phase
     */
    public ArrayList<Personagem> getElementos() {
        return elementos;
    }

    /**
     * Gets the hero character
     * @return The hero for this phase
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Gets the current level number
     * @return Current level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Gets the map for this phase
     * @return The map object for this phase
     */
    public Mapas getMapa() {
        return mapa;
    }
}
