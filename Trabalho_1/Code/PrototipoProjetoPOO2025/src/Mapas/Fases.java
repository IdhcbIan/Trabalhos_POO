package Mapas;

import Auxiliar.Consts;
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
        addFruta(5, 5);
        addFruta(7, 7);
        addFruta(10, 10);
        addFrutaVert(7, 7);


        // Add villains
        addVilao(4, 4, true);
        addVilao(12, 12, false);
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

        // Add villains - uncomment and modify as needed
        addVilao2(4, 1, true); 
        addVilao2(11, 1, true); 
        addVilao2(18, 1, true); 
        addVilao2(25, 1, true); 
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
        // Set hero position for level 5
        hero.setPosicao(1, 1);
        
        // Add fruits and villains for level 5
        // This is a placeholder - customize as needed
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

    private void addVilao2(int linha, int coluna, boolean isZigueZague) {
        Villan_2 vilao = new Villan_2("Villan_1.png");
        vilao.setPosicao(linha, coluna);
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
