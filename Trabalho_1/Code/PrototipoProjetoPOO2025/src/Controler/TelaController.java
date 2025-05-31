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
    
    public void carregarFase(int numeroFase) {
        Fruta.resetContadores();
        
        this.faseAtual.clear();
        
        this.fase = new Fases(numeroFase);
        this.faseAtual.addAll(this.fase.getElementos());
        this.hero = this.fase.getHero();
        
        if (this.cameraManager != null && this.hero != null) {
            this.cameraManager.atualizaCamera(hero);
        }
        
        if (this.view != null) {
            this.view.repaint();
        }
    }
    
    public void verificarFimDeFase() {
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
        
        if (totalFrutas > 0 && todasFrutasColetadas && !Hero.isGameOver()) {
            SuccessoNotification.getInstance()
                .showSuccessMessage("Level Complete!\nAll fruits collected!");
            view.repaint();   
        }
    }
    
    private boolean isFrutaColetada(Personagem p) {
        if (p instanceof Fruta) {
            return ((Fruta) p).isColetada();
        } else if (p instanceof Modelo.FrutaVert) {
            return ((Modelo.FrutaVert) p).foiColetado();
        }
        return false;
    }
    private long lastPhaseLoadTime = 0;
    private static final long PHASE_LOAD_COOLDOWN = 1000; // 1 second cooldown
    
    public void loadVillainsFromZip() {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Select Villain Zip File");
        
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter("ZIP Files", "zip");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(view);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            try {
                VillainLoader loader = new VillainLoader(this);
                ArrayList<Personagem> loadedVillains = loader.loadVillainsFromZip(selectedFile.getAbsolutePath());
                
                if (!loadedVillains.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(
                        view, 
                        "Successfully loaded " + loadedVillains.size() + " villains!", 
                        "Villains Loaded", 
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    javax.swing.JOptionPane.showMessageDialog(
                        view, 
                        "No villains were found in the zip file.", 
                        "No Villains Found", 
                        javax.swing.JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(
                    view, 
                    "Error loading villains: " + e.getMessage(), 
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }
    
    public boolean saveGame(String fileName) {
        boolean success = GameSaveManager.saveGame(this, fileName);
        if (success) {
            SuccessoNotification.getInstance().showSuccessMessage("Game saved successfully!");
            if (this.view != null) {
                this.view.repaint();
            }
        }
        return success;
    }
    
    public boolean loadGame(String fileName) {
        boolean success = GameSaveManager.loadGame(this, fileName);
        if (success) {
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
        boolean isSuccessNotificationActive = SuccessoNotification.getInstance().isVisible();
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE && isSuccessNotificationActive) {
            if (!SuccessoNotification.getInstance().isLevelCompletion()) {
                SuccessoNotification.getInstance().hide();
                return; 
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_N && isSuccessNotificationActive) {
            if (SuccessoNotification.getInstance().isLevelCompletion()) {
                long currentTime = System.currentTimeMillis();
                
                if (currentTime - lastPhaseLoadTime < PHASE_LOAD_COOLDOWN) {
                    return; 
                }
                
                SuccessoNotification.getInstance().hide();
                
                if (fase != null) {
                    int nextLevel = fase.getLevel() + 1;
                    if (nextLevel <= 5) {
                        lastPhaseLoadTime = currentTime;
                        carregarFase(nextLevel);
                    } else {
                        SuccessoNotification.getInstance().showSuccessMessage("Congratulations!\nYou completed all levels!");
                    }
                }
                return; 
            }
        }
        
        if (isSuccessNotificationActive) {
            return;
        }
        
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_5) {
            carregarFase(e.getKeyCode() - KeyEvent.VK_0);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            if (hero != null) {
                System.out.println("Debug: Forcing game over");
                hero.morrer();
                this.view.repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            System.out.println("R key pressed, isGameOver=" + Hero.isGameOver());
            if (Hero.isGameOver() || fase != null) {
                Hero.resetGameOver();
                FracassoNotification.getInstance().hide();
                if (fase != null) {
                    carregarFase(fase.getLevel());
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F5) {
            saveGame(null); 
        } else if (e.getKeyCode() == KeyEvent.VK_F9) {
            loadGame(null); 
        } else if (e.getKeyCode() == KeyEvent.VK_V) {
            loadVillainsFromZip();
        } else if (hero != null && !Hero.isGameOver()) {  
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                hero.moveUp();
                verificarFimDeFase(); 
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                hero.moveDown();
                verificarFimDeFase();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                hero.moveLeft();
                verificarFimDeFase(); 
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                hero.moveRight();
                verificarFimDeFase();
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                SuccessoNotification.getInstance().showSuccessMessage("SUCESSO!!");
            }
            
            this.cameraManager.atualizaCamera(hero);
            this.view.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                    + (hero.getPosicao().getLinha()));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
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