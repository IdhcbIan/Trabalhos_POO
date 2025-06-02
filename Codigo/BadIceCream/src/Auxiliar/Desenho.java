package Auxiliar;

import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ImageIcon;
import Controler.TelaView;
import Modelo.Hero;
import Modelo.Personagem;

public class Desenho implements Serializable {

    static TelaView jCenario;

    public static void setCenario(TelaView umJCenario) {
        jCenario = umJCenario;
    }

    public static TelaView acessoATelaDoJogo() {
        return jCenario;
    }

    public static Graphics getGraphicsDaTela() {
        return jCenario.getGraphicsBuffer();
    }

    public static void desenhar(ImageIcon iImage, int iColuna, int iLinha) {
        int telaX = (iColuna - jCenario.getCameraColuna()) * Consts.CELL_SIDE;
        int telaY = (iLinha - jCenario.getCameraLinha()) * Consts.CELL_SIDE;

        if (telaX >= 0 && telaX < Consts.RES * Consts.CELL_SIDE
                && telaY >= 0 && telaY < Consts.RES * Consts.CELL_SIDE) {
            iImage.paintIcon(jCenario, getGraphicsDaTela(), telaX, telaY);
        }
    }

    public static Hero acessoAoHeroi() {
        if (jCenario != null) {
            return jCenario.getController().getHero();
        }
        return null;
    }

    public static void removerPersonagem(Personagem p) {
        if (jCenario != null) {
            jCenario.getController().removePersonagem(p);
        }
    }

    public static void desenhar(Personagem p) {
        if (jCenario != null) {
            jCenario.getController().addPersonagem(p);
        }
    }
}
