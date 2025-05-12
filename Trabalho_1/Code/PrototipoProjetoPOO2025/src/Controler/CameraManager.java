package Controler;

import Auxiliar.Consts;
import Modelo.Hero;

public class CameraManager {
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    
    public CameraManager() {
    }
    
    public int getCameraLinha() {
        return cameraLinha;
    }
    
    public int getCameraColuna() {
        return cameraColuna;
    }
    
    public void atualizaCamera(Hero hero) {
        if (hero == null) {
            System.err.println("ERROR: Cannot update camera - hero is null");
            return;
        }
        
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        // Center camera on hero
        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
        
        System.out.println("Camera updated to: " + cameraLinha + "," + cameraColuna);
    }
}