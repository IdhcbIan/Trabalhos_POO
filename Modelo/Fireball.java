package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;

public class Fireball extends Personagem {
    private boolean movingRight;
    private int speed = 1;
    
    public Fireball(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }
    
    public void setDirection(boolean right) {
        this.movingRight = right;
    }
    
    @Override
    public void autoPilot() {
        // Move in the set direction
        if (movingRight) {
            moveRight();
        } else {
            moveLeft();
        }
    }
    
    private void moveRight() {
        Posicao p = new Posicao(this.getPosicao().getLinha(), this.getPosicao().getColuna() + speed);
        this.setPosicao(p);
    }
    
    private void moveLeft() {
        Posicao p = new Posicao(this.getPosicao().getLinha(), this.getPosicao().getColuna() - speed);
        this.setPosicao(p);
    }
} 