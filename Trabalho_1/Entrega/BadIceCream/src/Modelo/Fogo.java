package Modelo;

import java.io.Serializable;

public class Fogo extends Personagem implements Serializable {
    
    private boolean heroWasHere;
    private int heroLastPositionLine;
    private int heroLastPositionColumn;
    private int ticksOnFire; 
    
    public Fogo(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true; 
        this.bMortal = true; 
        this.heroWasHere = false;
        this.heroLastPositionLine = -1;
        this.heroLastPositionColumn = -1;
        this.ticksOnFire = 0;
    }
    public boolean checkHeroOnFire(Hero hero) {
        if (hero == null || Hero.isGameOver()) {
            return false;
        }
        
        int heroLine = hero.getPosicao().getLinha();
        int heroColumn = hero.getPosicao().getColuna();
        
        boolean isOnFire = heroLine == this.pPosicao.getLinha() && 
                           heroColumn == this.pPosicao.getColuna();
        
        if (isOnFire) {
            if (heroWasHere && heroLine == heroLastPositionLine && 
                heroColumn == heroLastPositionColumn) {
                ticksOnFire++;
                if (ticksOnFire >= 2) {
                    return true; 
                }
            } else {
                ticksOnFire = 0;
            }
            
            heroWasHere = true;
            heroLastPositionLine = heroLine;
            heroLastPositionColumn = heroColumn;
        } else {
            heroWasHere = false;
            ticksOnFire = 0; 
        }
        
        return false;
    }
}
