package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Hero extends Personagem implements Serializable{
    // Make this static so it can be accessed by all game elements
    private static boolean isGameOver = false;
    
    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean setPosicao(int linha, int coluna){
        // Don't allow position changes if the game is over
        if (isGameOver) {
            return false;
        }
        
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }

    /*TO-DO: este metodo pode ser interessante a todos os personagens que se movem*/
    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public boolean moveUp() {
        if (isGameOver) return false;  // Don't move if game over
        if(super.moveUp())
            return validaPosicao();
        return false;
    }

    public boolean moveDown() {
        if (isGameOver) return false;  // Don't move if game over
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        if (isGameOver) return false;  // Don't move if game over
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        if (isGameOver) return false;  // Don't move if game over
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }    
    
    public void morrer() {
        // Set the game over flag
        isGameOver = true;
        System.out.println("Game Over flag set to: " + isGameOver); // Debug message
        
        // Display the failure message with restart instruction
        FracassoNotification notification = FracassoNotification.getInstance();
        notification.showFailureMessage("Game Over!\nPress R to restart");
        System.out.println("FracassoNotification visibility: " + notification.isVisible()); // Debug message
    }
    
    // Add static method to check if game is over
    public static boolean isGameOver() {
        return isGameOver;
    }
    
    // Add static method to reset game over state
    public static void resetGameOver() {
        isGameOver = false;
    }
    
    // Add static method to set game over state (used when loading a saved game)
    public static void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
