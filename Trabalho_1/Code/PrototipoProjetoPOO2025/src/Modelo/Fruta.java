package Modelo;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fruta extends Personagem implements Serializable {
    private boolean bRight;
    private static final double SCALE_FACTOR = 0.8; 
    private int steps;
    private int maxSteps = 3;
    private static int totalFrutas = 0;  // Track total fruits
    private static int frutasColetadas = 0;  // Track collected fruits
    private boolean coletada = false;  // Track if this fruit has been collected

    public Fruta(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bRight = true;
        steps = 0;
        resizeImage();
        totalFrutas++;  // Increment total fruits when a new one is created
    }
    
    private void resizeImage() {
        if (iImage != null) {
            Image img = iImage.getImage();
            int width = (int)(img.getWidth(null) * SCALE_FACTOR);
            int height = (int)(img.getHeight(null) * SCALE_FACTOR);
            
            BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = resizedImg.createGraphics();
            g.drawImage(img, 0, 0, width, height, null);
            g.dispose();
            
            iImage = new ImageIcon(resizedImg);
        }
    }
    
    public void autoDesenho(){
        if(bRight)
            this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna()+1);
        else
            this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna()-1);           

        super.autoDesenho();
        
        steps++;
        if(steps >= maxSteps) {
            bRight = !bRight;
            steps = 0;
        }
    }

    // Modified method to handle fruit collection
    public void coletar() {
        if (!coletada) {
            coletada = true;
            frutasColetadas++;
            System.out.println("Frutas coletadas: " + frutasColetadas + " de " + totalFrutas);
            verificarSucesso();
        }
    }
    
    // Add method to check for success
    public static void verificarSucesso() {
        if (frutasColetadas >= totalFrutas && totalFrutas > 0) {
            System.out.println("Todas as frutas foram coletadas!");
            mostrarSucesso();
        }
    }
    
    // Modified method to show success message in the middle of the screen
    public static void mostrarSucesso() {
        // Create a reference to the notification system
        SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
    }
    
    // Add method to manually reset counters if needed
    public static void resetContadores() {
        totalFrutas = 0;
        frutasColetadas = 0;
    }
}
