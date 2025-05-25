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
    
    public void setHero(Hero hero) {
        this.hero = hero;
    }
    
    public CameraManager getCameraManager() {
        return cameraManager;
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
    
    /**
     * Saves the current game state to a file.
     * 
     * @param fileName Optional custom filename, uses default if null
     * @return true if save was successful, false otherwise
     */
    public boolean saveGame(String fileName) {
        boolean success = GameSaveManager.saveGame(this, fileName);
        if (success) {
            // Show a success message
            SuccessoNotification.getInstance().showSuccessMessage("Game saved successfully!");
            if (this.view != null) {
                this.view.repaint();
            }
        }
        return success;
    }
    
    /**
     * Loads a game state from a file.
     * 
     * @param fileName Optional custom filename, uses default if null
     * @return true if load was successful, false otherwise
     */
    public boolean loadGame(String fileName) {
        boolean success = GameSaveManager.loadGame(this, fileName);
        if (success) {
            // Show a success message
            SuccessoNotification.getInstance().showSuccessMessage("Game loaded successfully!");
            if (this.view != null) {
                this.view.repaint();
            }
        }
        return success;
    }
    
    public TelaView getView() {
        return view;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // Check if success notification is active
        boolean isSuccessNotificationActive = SuccessoNotification.getInstance().isVisible();
        
        // Handle spacebar to dismiss save/load notifications
        if (e.getKeyCode() == KeyEvent.VK_SPACE && isSuccessNotificationActive) {
            // Only dismiss if it's not a level completion notification
            if (!SuccessoNotification.getInstance().isLevelCompletion()) {
                SuccessoNotification.getInstance().hide();
                return; // Exit the method after handling spacebar
            }
        }
        
        // Handle 'n' key to load next level when success notification is showing
        if (e.getKeyCode() == KeyEvent.VK_N && isSuccessNotificationActive) {
            // Only proceed to next level if it's a level completion notification
            if (SuccessoNotification.getInstance().isLevelCompletion()) {
                // Get current time to check cooldown
                long currentTime = System.currentTimeMillis();
                
                // Check if cooldown has passed
                if (currentTime - lastPhaseLoadTime < PHASE_LOAD_COOLDOWN) {
                    return; // Skip if cooldown hasn't passed
                }
                
                // Hide the success notification
                SuccessoNotification.getInstance().hide();
                
                // Load the next level
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
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            // Save game with F5 key
            saveGame(null); // Use default filename
        } else if (e.getKeyCode() == KeyEvent.VK_F9) {
            // Load game with F9 key
            loadGame(null); // Use default filename
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