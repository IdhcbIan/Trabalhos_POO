package Controler;

import Modelo.Fruta;
import Modelo.Hero;
import Modelo.Personagem;
import auxiliar.Posicao;
import java.util.ArrayList;

public class ControleDeJogo {
    public void desenhaTudo(ArrayList<Personagem> e){
        for(int i = 0; i < e.size(); i++){
            e.get(i).autoDesenho();
        }
    }
    public void processaTudo(ArrayList<Personagem> umaFase){
        Hero hero = null;
        
        // Find the hero
        for (int i = 0; i < umaFase.size(); i++) {
            Personagem p = umaFase.get(i);
            if (p instanceof Hero) {
                hero = (Hero) p;
                break;
            }
        }
        
        if (hero != null) {
            // Process collisions
            for (int i = 0; i < umaFase.size(); i++) {
                Personagem p = umaFase.get(i);
                if (p instanceof Fruta) {
                    // Check if hero is at the same position as the fruit
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Fruta fruta = (Fruta) p;
                        fruta.coletar();
                        umaFase.remove(p);
                        i--; // Adjust index after removal
                    }
                }
                else if (p != hero && hero.getPosicao().igual(p.getPosicao())) {
                    if (p.isbTransponivel()) {
                        umaFase.remove(p);
                        i--; // Adjust index after removal
                    }
                }
            }
        }
    }
    
    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p){
        Personagem pIesimoPersonagem;
        for(int i = 1; i < umaFase.size(); i++){
            pIesimoPersonagem = umaFase.get(i);            
            if(!pIesimoPersonagem.isbTransponivel())
                if(pIesimoPersonagem.getPosicao().igual(p))
                    return false;
        }        
        return true;
    }
}
