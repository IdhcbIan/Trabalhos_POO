package Mapas;

import Modelo.*;
import auxiliar.Posicao;
import java.util.ArrayList;

public class Fases {
    private int level;
    private ArrayList<Personagem> elementos;
    private Hero hero;
    private Mapas mapa;

    public Fases(int level) {
        this.level = level;
        this.elementos = new ArrayList<>();
        this.mapa = new Mapas(level);
        initializeLevel();
    }

    private void initializeLevel() {
        // Create hero
        hero = new Hero("Char_1.png");
        
        // Add ice barriers from map
        addIceBarriers();


        // Configure level based on level number
        switch(level) {
            case 1:
                configureLevel1();
                break;
            case 2:
                configureLevel2();
                break;
            case 3:
                configureLevel3();
                break;
            case 4:
                configureLevel4();
                break;
            case 5:
                configureLevel5();
                break;
            default:
                configureLevel1(); 
        }

        this.elementos.add(hero);
    }
    
    // 11111111111111111111111111111111111111111111111111111111111
    private void configureLevel1() {
        // Set hero position for level 1
        hero.setPosicao(2, 7);
        
        // Add fruits
        addFruta(4, 8);
        addFruta(11, 4);
        addFruta(18, 8);
        addFruta(25, 4);

        // Add villains - uncomment and modify as needed
        addVilao(4, 1, true); 
        addVilao(11, 1, true); 
        addVilao(18, 1, true); 
        addVilao(25, 1, true); 
    }

    // 222222222222222222222222222222222222222222222222222222222
    private void configureLevel2() {
        // Set hero position for level 2
        hero.setPosicao(1, 1);
        
        // Add fruits
        addFruta(1, 10);
        addFruta(3, 8);
        addFruta(5, 6);
        addFruta(7, 4);
    
        addFrutaVert(6, 9);


        // Add villains
        addVilao(3, 1, true);
    }

    // 333333333333333333333333333333333333333333333333333333333
    private void configureLevel3() {
        hero.setPosicao(2, 7);
        
        // Add fruits
        addFruta(4, 8);
        addFruta(11, 4);
        addFruta(18, 8);
        addFruta(25, 4);

        addVilao2(4, 1, 20, true);  
        addVilao2(11, 13, 20, false); 
        
        addVilao2(18, 1, 20, true);  
        addVilao2(25, 13, 20, false);  
    }

    // 444444444444444444444444444444444444444444444444444444444
    private void configureLevel4() {
        hero.setPosicao(1, 1);
        
        addFruta(1, 10);
        addFruta(3, 8);
        addFruta(5, 6);
        addFruta(7, 4);
    }

    // 555555555555555555555555555555555555555555555555555555555
    private void configureLevel5() {
        // Configurando Heroi
        Posicao heroPos = mapa.getHeroStartPosition();
        hero.setPosicao(heroPos.getLinha(), heroPos.getColuna());
        
        addFruta(4, 1);
        addFruta(8, 8);
        addFruta(10, 1);
        addFrutaVert(10, 13);
        addFrutaVert(13, 1);


        addFruta(24, 1);
        addFruta(28, 1);
        addFrutaVert(24, 7);
        addFrutaVert(28, 13);
        
        // Adicionando Fogos
        for (int row = 5; row < 8; row++) {
            for (int col = 1; col < 14; col++) {
                addFogo(row, col);
            }
        }
        
        for (int row = 20; row < 23; row++) {
            for (int col = 1; col < 14; col++) {
                addFogo(row, col);
            }
        }
        
        // Adicionando Viloes
        ArrayList<Posicao> villainPositions = mapa.getVillainPositions();
        
        // Adicionando um Villan_3 na primeira posicao de viloes
        if (!villainPositions.isEmpty()) {
            Posicao firstPos = villainPositions.get(0);
            addVilao3(firstPos.getLinha(), firstPos.getColuna(), 3);
        }

        // Adicionando os Viloes que atiram
        addVilao2(15, 13, 20, false);
        addVilao2(19, 1, 20, true);
    }


    //-------- Helper Functions ------------------------------------------
    
    private void addVilao3(int linha, int coluna, int moveRate) {
        Villan_3 vilao = new Villan_3("Villan_3.png");
        vilao.setPosicao(linha, coluna);
        vilao.setTarget(hero);        
        vilao.setMoveRate(moveRate); 
        this.elementos.add(vilao);
    }

    private void addFruta(int linha, int coluna) {
        Fruta fruta = new Fruta("Fruit_1.png");
        fruta.setPosicao(linha, coluna);
        this.elementos.add(fruta);
    }

    private void addVilao(int linha, int coluna, boolean isZigueZague) {
        Villan_1 vilao = new Villan_1("Villan_2.png");
        vilao.setPosicao(linha, coluna);
        this.elementos.add(vilao);
    }

    private void addIceBarriers() {
        ArrayList<Posicao> icePositions = mapa.getIcePositions();
        for (Posicao pos : icePositions) {
            Ice ice = new Ice("Ice.png");
            ice.setPosicao(pos.getLinha(), pos.getColuna());
            this.elementos.add(ice);
        }
    }

    private void addVilao2(int linha, int coluna, int rate, boolean shootRight) {
        Villan_2 vilao = new Villan_2("Villan_1.png");
        vilao.setPosicao(linha, coluna);
        vilao.setTarget(hero);       
        vilao.setShootRate(rate);    
        vilao.setShootDirection(shootRight); 
        this.elementos.add(vilao);
    }

    private void addFogo(int linha, int coluna) {
        Fogo fogo = new Fogo("Fire.png"); 
        fogo.setPosicao(linha, coluna);
        this.elementos.add(fogo);
    }

    private void addFrutaVert(int linha, int coluna) {
        FrutaVert frutaVert = new FrutaVert("Fruit_2.png");
        frutaVert.setPosicao(linha, coluna);
        this.elementos.add(frutaVert);
    }

    public ArrayList<Personagem> getElementos() {
        return elementos;
    }

    public Hero getHero() {
        return hero;
    }

    public int getLevel() {
        return level;
    }
    
    public Mapas getMapa() {
        return mapa;
    }
}
