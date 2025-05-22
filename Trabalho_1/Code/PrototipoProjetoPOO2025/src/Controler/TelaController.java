package Controler;

import Auxiliar.Consts;
import Mapas.Fases;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.SuccessoNotification;
import Modelo.FracassoNotification;
import auxiliar.Posicao;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import Modelo.Fruta;

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
    
    public Fases getFase() {
        return fase;
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
    public void carregarFase(int numeroFase) {
        // Reset the fruit counters before loading a new phase
        Fruta.resetContadores();
        
        // Clear existing phase elements
        this.faseAtual.clear();
        
        // Load the new phase
        this.fase = new Fases(numeroFase);
        this.faseAtual.addAll(this.fase.getElementos());
        this.hero = this.fase.getHero();
        
        // Update camera and view if available
        if (this.cameraManager != null && this.hero != null) {
            this.cameraManager.atualizaCamera(hero);
        }
        
        if (this.view != null) {
            this.view.repaint();
        }
    }
    
    // Add this method to check if all fruits are collected
    public void verificarFimDeFase() {
        // Check if there are any fruits left in the current phase
        boolean todasFrutasColetadas = true;
        int totalFrutas = 0;
        int coletadas = 0;
        
        for (Personagem p : faseAtual) {
            if (p instanceof Fruta || p instanceof Modelo.FrutaVert) {
                totalFrutas++;
                if (isFrutaColetada(p)) {
                    coletadas++;
                } else {
                    todasFrutasColetadas = false;
                }
            }
        }
        
        System.out.println("DEBUG TelaController: Frutas coletadas: " + coletadas + 
                          " de " + totalFrutas + ", todas coletadas? " + todasFrutasColetadas);
        
        // If all fruits are collected, show success message
        if (totalFrutas > 0 && todasFrutasColetadas && !Hero.isGameOver()) {
            SuccessoNotification.getInstance()
                .showSuccessMessage("Level Complete!\nAll fruits collected!");
            view.repaint();   // <— immediate repaint
        }
    }
    
    // Helper method to check if a fruit has been collected
    private boolean isFrutaColetada(Personagem p) {
        if (p instanceof Fruta) {
            return ((Fruta) p).isColetada();
        } else if (p instanceof Modelo.FrutaVert) {
            return ((Modelo.FrutaVert) p).foiColetado();
        }
        return false;
    }
    // Add a variable to track when the next phase was loaded to prevent rapid skipping
    private long lastPhaseLoadTime = 0;
    private static final long PHASE_LOAD_COOLDOWN = 1000; // 1 second cooldown
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the success notification is visible and game is frozen
        boolean isSuccessNotificationActive = SuccessoNotification.getInstance().isVisible() && 
                                            SuccessoNotification.getInstance().isGameFreeze();
        
        // Handle 'n' key press to advance to next phase when success notification is shown
        if (e.getKeyCode() == KeyEvent.VK_N && isSuccessNotificationActive) {
            // Check if enough time has passed since the last phase load
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPhaseLoadTime < PHASE_LOAD_COOLDOWN) {
                System.out.println("Too soon to load next phase, ignoring N key");
                return; // Ignore the key press if it's too soon
            }
            
            System.out.println("N key pressed, advancing to next phase");
            
            // Properly hide the success notification
            SuccessoNotification.getInstance().hide();
            
            // Force repaint to remove the black overlay
            view.repaint();
            
            // Load the next phase if available
            if (fase != null) {
                int nextLevel = fase.getLevel() + 1;
                // Check if next level exists (assuming max 5 levels)
                if (nextLevel <= 5) {
                    // Update the last phase load time
                    lastPhaseLoadTime = currentTime;
                    carregarFase(nextLevel);
                } else {
                    // If we're at the last level, show a game completion message
                    SuccessoNotification.getInstance().showSuccessMessage("Congratulations!\nYou completed all levels!");
                }
            }
            return; // Exit the method after handling 'n' key
        }
        
        // If game is frozen due to success notification, don't process other inputs
        if (isSuccessNotificationActive) {
            return;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_5) {
            // Load levels 1-5 with number keys
            carregarFase(e.getKeyCode() - KeyEvent.VK_0);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            // Debug key - force game over
            if (hero != null) {
                System.out.println("Debug: Forcing game over");
                hero.morrer();
                // Force repaint to show notification
                this.view.repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            System.out.println("R key pressed, isGameOver=" + Hero.isGameOver());
            // If game is over or R is pressed, restart the current level
            if (Hero.isGameOver() || fase != null) {
                // Reset game over state
                Hero.resetGameOver();
                // Hide the game over message
                FracassoNotification.getInstance().hide();
                // Reload the current phase
                if (fase != null) {
                    carregarFase(fase.getLevel());
                }
            }
        } else if (hero != null && !Hero.isGameOver()) {  // Only process movement if hero exists and game is not over
            // Process movement keys
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                hero.moveUp();
                verificarFimDeFase(); // Check after movement
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                hero.moveDown();
                verificarFimDeFase(); // Check after movement
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                hero.moveLeft();
                verificarFimDeFase(); // Check after movement
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                hero.moveRight();
                verificarFimDeFase(); // Check after movement
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