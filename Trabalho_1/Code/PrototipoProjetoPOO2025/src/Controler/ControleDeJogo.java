package Controler;

import Modelo.Fruta;
import Modelo.FrutaVert;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.Villan_1;
import Modelo.Villan_2;
import Modelo.Villan_3;
import Modelo.Fogo;
import Modelo.Fireball;
import Modelo.SuccessoNotification;
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
            // Check if player is invulnerable due to success notification
            boolean playerInvulnerable = SuccessoNotification.getInstance().isPlayerInvulnerable();
            
            // Process ALL villains to generate fireballs, regardless of visibility
            for (Personagem p : umaFase) {
                if (p instanceof Villan_2) {
                    Villan_2 vilao = (Villan_2) p;
                    // Manually process the villain's logic to ensure fireballs are created
                    if (!Hero.isGameOver() && !SuccessoNotification.getInstance().isGameFreeze()) {
                        vilao.processLogic();
                    }
                }
            }
        
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
                    // Only kill the hero if not invulnerable
                    if (!playerInvulnerable) {
                        hero.morrer();
                    } else {
                        System.out.println("Hero is invulnerable due to success notification, cannot be killed by fireball!");
                    }
                    umaFase.remove(p);
                    i--;
                    continue;
                }
                
                // Check for collisions with fruits - improved detection for fast movement
                if (p instanceof Fruta) {
                    // Check if hero is at the same position as the fruit
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Fruta fruta = (Fruta) p;
                        fruta.coletar();
                        umaFase.remove(p);
                        i--; // Adjust index after removal
                    }
                }
                // Check for collisions with vertical fruits - improved detection
                else if (p instanceof FrutaVert) {
                    // Check if hero is at the same position as the fruit
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        FrutaVert frutaVert = (FrutaVert) p;
                        frutaVert.coletar();
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
                else if (p instanceof Villan_2) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Villan_2 villan = (Villan_2) p;
                        // Only kill the hero if not invulnerable
                        if (!playerInvulnerable) {
                            villan.matarHero(hero);
                        } else {
                            System.out.println("Hero is invulnerable due to success notification, cannot be killed by villain 2!");
                        }
                    }
                }
                else if (p instanceof Villan_3) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Villan_3 villan = (Villan_3) p;
                        // Only kill the hero if not invulnerable
                        if (!playerInvulnerable) {
                            villan.matarHero(hero);
                        } else {
                            System.out.println("Hero is invulnerable due to success notification, cannot be killed by villain 3!");
                        }
                    }
                }
                // Check for hero standing on fire
                else if (p instanceof Fogo) {
                    Fogo fogo = (Fogo) p;
                    // Only check for fire if hero is not invulnerable
                    if (!playerInvulnerable && fogo.checkHeroOnFire(hero)) {
                        hero.morrer(); // Kill the hero if standing on fire
                    } else if (playerInvulnerable && fogo.checkHeroOnFire(hero)) {
                        System.out.println("Hero is invulnerable due to success notification, cannot be killed by fire!");
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
