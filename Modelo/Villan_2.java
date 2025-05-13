package Modelo;

import Auxiliar.Consts;
import Mapas.Fases;
import auxiliar.Posicao;

public class Villan_2 extends Personagem {
    private int shootingRate = 30;
    private boolean shootRight = true;
    private int shootingCounter = 0;
    private Fases fases; // Reference to the Fases object
    
    public Villan_2(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }
    
    public void setFases(Fases fases) {
        this.fases = fases;
    }
    
    public void setShootingRate(int rate) {
        this.shootingRate = rate;
    }
    
    public void setShootingDirection(boolean right) {
        this.shootRight = right;
    }
    
    @Override
    public void autoPilot() {
        // Increment counter
        shootingCounter++;
        
        // Check if it's time to shoot
        if (shootingCounter >= shootingRate && fases != null) {
            shootFireball();
            shootingCounter = 0;
        }
    }
    
    private void shootFireball() {
        // Create a new fireball
        Fireball fireball = new Fireball("Fireball.png"); // Use your fireball image name
        
        // Set position to start from villain
        int startX = this.getPosicao().getColuna();
        int startY = this.getPosicao().getLinha();
        
        // Adjust starting position based on direction
        if (shootRight) {
            startX += 1; // Start fireball to the right of villain
        } else {
            startX -= 1; // Start fireball to the left of villain
        }
        
        fireball.setPosicao(startY, startX);
        fireball.setDirection(shootRight);
        
        // Add fireball to game elements
        fases.addFireball(fireball);
    }
} 