package Modelo;

import java.io.Serializable;

/**
 * Fogo class represents fire blocks in the game.
 * These blocks are transposable (you can move through them),
 * but if the hero stands still on them, they will die.
 */
public class Fogo extends Personagem implements Serializable {
    
    private boolean heroWasHere;
    private int heroLastPositionLine;
    private int heroLastPositionColumn;
    
    /**
     * Constructor for fire block
     * @param sNomeImagePNG The image filename
     */
    public Fogo(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true; // Fire can be passed through
        this.bMortal = true; // But it's deadly if you stand on it
        this.heroWasHere = false;
        this.heroLastPositionLine = -1;
        this.heroLastPositionColumn = -1;
    }
    
    /**
     * Checks if a hero is standing on this fire block
     * @param hero The hero to check
     * @return true if the hero is standing on this fire and should die
     */
    public boolean checkHeroOnFire(Hero hero) {
        if (hero == null || Hero.isGameOver()) {
            return false;
        }
        
        // Get hero's current position
        int heroLine = hero.getPosicao().getLinha();
        int heroColumn = hero.getPosicao().getColuna();
        
        // Check if hero is on this fire block
        boolean isOnFire = heroLine == this.pPosicao.getLinha() && 
                           heroColumn == this.pPosicao.getColuna();
        
        if (isOnFire) {
            // If hero is in the same position as last time, they're standing still on fire
            if (heroWasHere && heroLine == heroLastPositionLine && 
                heroColumn == heroLastPositionColumn) {
                return true; // Hero should die
            }
            
            // Update tracking of hero's position
            heroWasHere = true;
            heroLastPositionLine = heroLine;
            heroLastPositionColumn = heroColumn;
        } else {
            // Hero is not on this fire block
            heroWasHere = false;
        }
        
        return false;
    }
}
