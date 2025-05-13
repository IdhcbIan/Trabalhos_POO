package Controler;

import Modelo.Fruta;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.Villan_1;
import Modelo.Villan_2;
import Modelo.Fogo;
import Modelo.Fireball;
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
            // Add any pending fireballs
            ArrayList<Fireball> fireballs = Villan_2.getPendingFireballs();
            for (Fireball fireball : fireballs) {
                umaFase.add(fireball);
            }
            
            // Process collisions and remove expired fireballs
            for (int i = 0; i < umaFase.size(); i++) {
                Personagem p = umaFase.get(i);
                
                // Check for fireballs that need to be removed
                if (p instanceof Fireball) {
                    Fireball fireball = (Fireball) p;
                    if (fireball.shouldRemove()) {
                        umaFase.remove(i);
                        i--; // Adjust index after removal
                        continue;
                    }
                }
                
                // Check for hero collision with fireball
                if (p instanceof Fireball && hero.getPosicao().igual(p.getPosicao())) {
                    hero.morrer();
                    umaFase.remove(p);
                    i--;
                    continue;
                }
                
                // Check for collisions with fruits
                if (p instanceof Fruta) {
                    // Check if hero is at the same position as the fruit
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Fruta fruta = (Fruta) p;
                        fruta.coletar();
                        umaFase.remove(p);
                        i--; // Adjust index after removal
                    }
                }
                // Check for collisions with villains
                else if (p instanceof Villan_1) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Villan_1 villan = (Villan_1) p;
                        villan.matarHero(hero);
                    }
                }
                // Check for hero standing on fire
                else if (p instanceof Fogo) {
                    Fogo fogo = (Fogo) p;
                    if (fogo.checkHeroOnFire(hero)) {
                        hero.morrer(); // Kill the hero if standing on fire
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
