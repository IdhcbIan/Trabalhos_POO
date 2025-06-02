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
    private static boolean isGameOver = false;
    
    public Hero(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean setPosicao(int linha, int coluna){
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

    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao())) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public boolean moveUp() {
        if (isGameOver) return false;  
        if(super.moveUp())
            return validaPosicao();
        return false;
    }

    public boolean moveDown() {
        if (isGameOver) return false;  
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        if (isGameOver) return false;  
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        if (isGameOver) return false;  
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }    
    
    public void morrer() {
        isGameOver = true;
        System.out.println("Game Over flag set to: " + isGameOver); 
        
        FracassoNotification notification = FracassoNotification.getInstance();
        notification.showFailureMessage("Game Over!\nPress R to restart");
        System.out.println("FracassoNotification visibility: " + notification.isVisible()); 
    }
    
    public static boolean isGameOver() {
        return isGameOver;
    }
    
    public static void resetGameOver() {
        isGameOver = false;
    }
    
    public static void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
