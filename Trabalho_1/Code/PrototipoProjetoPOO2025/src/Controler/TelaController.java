package Controler;

import Auxiliar.Consts;
import Mapas.Fases;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.SuccessoNotification;
import auxiliar.Posicao;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class TelaController implements MouseListener, KeyListener {
    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj;
    private Fases fase;
    private TelaView view;
    private CameraManager cameraManager;
    
    public TelaController() {
        this.cj = new ControleDeJogo();
        this.faseAtual = new ArrayList<Personagem>();
    }
    
    public void setView(TelaView view) {
        this.view = view;
    }
    
    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }
    
    public ArrayList<Personagem> getFaseAtual() {
        return faseAtual;
    }
    
    public ControleDeJogo getControleDeJogo() {
        return cj;
    }
    
    public Hero getHero() {
        return hero;
    }
    
    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }
    
    // Add this method to load phases
    public void carregarFase(int nivel) {
        // Create a new phase instance
        fase = new Fases(nivel);
        
        // Clear any existing elements
        faseAtual.clear();
        
        // Get all elements from the phase
        faseAtual.addAll(fase.getElementos());
        
        // Get hero reference for camera and controls
        hero = fase.getHero();
        
        // Make sure hero is not null
        if (hero == null) {
            System.err.println("ERROR: Hero is null in phase " + nivel);
            return;
        }
        
        // Debugging: Print all elements loaded in the phase
        System.out.println("Phase " + nivel + " loaded with " + faseAtual.size() + " elements");
        for (Personagem p : faseAtual) {
            System.out.println("  - " + p.getClass().getSimpleName() + 
                    " at (" + p.getPosicao().getLinha() + "," + p.getPosicao().getColuna() + ")");
        }
        
        // Update camera to hero position
        this.cameraManager.atualizaCamera(hero);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_5) {
            // Load levels 1-5 with number keys
            carregarFase(e.getKeyCode() - KeyEvent.VK_0);
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            // Reload current phase
            if (fase != null) {
                carregarFase(fase.getLevel());
            }
        } else if (hero != null) {
            // Only process movement if hero exists
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                hero.moveUp();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                hero.moveDown();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                hero.moveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                hero.moveRight();
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                // Test the success notification with S key
                SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
            }
            
            this.cameraManager.atualizaCamera(hero);
            this.view.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                    + (hero.getPosicao().getLinha()));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
        int x = e.getX();
        int y = e.getY();

        this.view.setTitle("X: " + x + ", Y: " + y
                + " -> Cell: " + (y / Consts.CELL_SIDE) + ", " + (x / Consts.CELL_SIDE));

        this.hero.getPosicao().setPosicao(y / Consts.CELL_SIDE, x / Consts.CELL_SIDE);

        this.view.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}