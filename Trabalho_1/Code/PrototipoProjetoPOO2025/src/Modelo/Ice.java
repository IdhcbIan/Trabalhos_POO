package Modelo;

import java.io.Serializable;

/**
 * Ice class represents static barrier objects in the game.
 * These barriers are not transposable and block player and enemy movement.
 */
public class Ice extends Personagem implements Serializable {
    
    /**
     * Constructor for ice barrier
     * @param sNomeImagePNG The image filename
     */
    public Ice(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false; // Ice barriers cannot be passed through
    }
    
    // Ice barriers don't move or have special behavior so no need to override autoDesenho
} 