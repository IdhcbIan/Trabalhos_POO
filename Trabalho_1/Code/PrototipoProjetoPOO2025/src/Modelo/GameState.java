package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import auxiliar.Posicao;

/**
 * Class responsible for storing the game state for serialization.
 * This class encapsulates all the data needed to restore a game session.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Personagem> faseAtual;
    private int currentLevel;
    private int frutasColetadas;
    private int frutasTotais;
    private Posicao heroPosition;
    private boolean gameOver;
    
    // Constructor
    public GameState() {
    }
    
    // Getters and Setters
    public ArrayList<Personagem> getFaseAtual() {
        return faseAtual;
    }
    
    public void setFaseAtual(ArrayList<Personagem> faseAtual) {
        this.faseAtual = faseAtual;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
    
    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public int getFrutasColetadas() {
        return frutasColetadas;
    }
    
    public void setFrutasColetadas(int frutasColetadas) {
        this.frutasColetadas = frutasColetadas;
    }
    
    public int getFrutasTotais() {
        return frutasTotais;
    }
    
    public void setFrutasTotais(int frutasTotais) {
        this.frutasTotais = frutasTotais;
    }
    
    public Posicao getHeroPosition() {
        return heroPosition;
    }
    
    public void setHeroPosition(Posicao heroPosition) {
        this.heroPosition = heroPosition;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
