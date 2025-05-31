package Modelo;

import java.io.Serializable;

public class Ice extends Personagem implements Serializable {
    
   public Ice(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false; 
    }
    
} 