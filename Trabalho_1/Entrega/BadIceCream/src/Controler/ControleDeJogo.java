package Controler;

import Modelo.Fireball;
import Modelo.Fogo;
import Modelo.Fruta;
import Modelo.FrutaVert;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.SuccessoNotification;
import Modelo.Villan_1;
import Modelo.Villan_2;
import Modelo.Villan_3;
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
        
        for (int i = 0; i < umaFase.size(); i++) {
            Personagem p = umaFase.get(i);
            if (p instanceof Hero) {
                hero = (Hero) p;
                break;
            }
        }
        
        if (hero != null) {
            boolean playerInvulnerable = SuccessoNotification.getInstance().isPlayerInvulnerable();
            
            for (Personagem p : umaFase) {
                if (p instanceof Villan_2) {
                    Villan_2 vilao = (Villan_2) p;
                    if (!Hero.isGameOver() && !SuccessoNotification.getInstance().isGameFreeze()) {
                        vilao.processLogic();
                    }
                }
            }
        
            ArrayList<Fireball> fireballs = Villan_2.getPendingFireballs();
            for (Fireball fireball : fireballs) {
                umaFase.add(fireball);
            }
            
            for (int i = 0; i < umaFase.size(); i++) {
                Personagem p = umaFase.get(i);
                
                if (p instanceof Fireball) {
                    Fireball fireball = (Fireball) p;
                    if (fireball.shouldRemove()) {
                        umaFase.remove(i);
                        i--; 
                        continue;
                    }
                }
                
                if (p instanceof Fireball && hero.getPosicao().igual(p.getPosicao())) {
                    if (!playerInvulnerable) {
                        hero.morrer();
                    } else {
                        System.out.println("Hero is invulnerable due to success notification, cannot be killed by fireball!");
                    }
                    umaFase.remove(p);
                    i--;
                    continue;
                }
                
                if (p instanceof Fruta) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Fruta fruta = (Fruta) p;
                        fruta.coletar();
                        umaFase.remove(p);
                        i--; 
                    }
                }
                else if (p instanceof FrutaVert) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        FrutaVert frutaVert = (FrutaVert) p;
                        frutaVert.coletar();
                        umaFase.remove(p);
                        i--; 
                    }
                }
                else if (p instanceof Villan_1) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Villan_1 villan = (Villan_1) p;
                        villan.matarHero(hero);
                    }
                }
                else if (p instanceof Villan_2) {
                    if (hero.getPosicao().igual(p.getPosicao())) {
                        Villan_2 villan = (Villan_2) p;
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
                        if (!playerInvulnerable) {
                            villan.matarHero(hero);
                        } else {
                            System.out.println("Hero is invulnerable due to success notification, cannot be killed by villain 3!");
                        }
                    }
                }
                else if (p instanceof Fogo) {
                    Fogo fogo = (Fogo) p;
                    if (!playerInvulnerable && fogo.checkHeroOnFire(hero)) {
                        hero.morrer(); 
                    } else if (playerInvulnerable && fogo.checkHeroOnFire(hero)) {
                        System.out.println("Hero is invulnerable due to success notification, cannot be killed by fire!");
                    }
                }
                else if (p != hero && hero.getPosicao().igual(p.getPosicao())) {
                    if (p.isbTransponivel()) {
                        umaFase.remove(p);
                        i--; 
                    }
                }
            }
        }
    }
    
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
