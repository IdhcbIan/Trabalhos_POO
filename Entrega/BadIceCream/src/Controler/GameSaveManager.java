package Controler;

import Modelo.Fruta;
import Modelo.FrutaVert;
import Modelo.GameState;
import Modelo.Hero;
import Modelo.Personagem;
import java.io.*;
import java.util.ArrayList;

public class GameSaveManager {
    private static final String DEFAULT_SAVE_DIRECTORY = "saves";
    private static final String DEFAULT_SAVE_FILENAME = "gamestate.dat";
    
    public static boolean saveGame(TelaController controller, String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = DEFAULT_SAVE_DIRECTORY + File.separator + DEFAULT_SAVE_FILENAME;
        }
        
        File saveDir = new File(DEFAULT_SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            
            GameState state = new GameState();
            
            state.setCurrentLevel(controller.getFase().getLevel());
            
            if (controller.getHero() != null) {
                state.setHeroPosition(controller.getHero().getPosicao());
                state.setGameOver(Hero.isGameOver());
            }
            
            state.setFaseAtual(new ArrayList<>(controller.getFaseAtual()));
            
            int frutasColetadas = 0;
            int frutasTotais = 0;
            
            for (Personagem p : controller.getFaseAtual()) {
                if (p instanceof Fruta) {
                    frutasTotais++;
                    if (((Fruta) p).isColetada()) {
                        frutasColetadas++;
                    }
                } else if (p instanceof FrutaVert) {
                    frutasTotais++;
                    if (((FrutaVert) p).foiColetado()) {
                        frutasColetadas++;
                    }
                }
            }
            
            state.setFrutasColetadas(frutasColetadas);
            state.setFrutasTotais(frutasTotais);
            
            out.writeObject(state);
            System.out.println("Game saved successfully to " + fileName);
            
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Game saved successfully!");
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Error saving game!");
            }
            
            return false;
        }
    }
    
    public static boolean loadGame(TelaController controller, String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = DEFAULT_SAVE_DIRECTORY + File.separator + DEFAULT_SAVE_FILENAME;
        }
        
        File saveFile = new File(fileName);
        if (!saveFile.exists()) {
            System.err.println("Save file does not exist: " + fileName);
            
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Save file not found!");
            }
            
            return false;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fileName))) {
            
            GameState state = (GameState) in.readObject();
            
            Fruta.resetContadores();
            
            int currentLevel = state.getCurrentLevel();
            controller.carregarFase(currentLevel);
            
            controller.getFaseAtual().clear();
            controller.getFaseAtual().addAll(state.getFaseAtual());
            
            for (Personagem p : controller.getFaseAtual()) {
                if (p instanceof Hero) {
                    controller.setHero((Hero) p);
                    break;
                }
            }
            
            if (state.isGameOver()) {
                Hero.setGameOver(true);
            } else {
                Hero.resetGameOver();
            }
            
            int normalFrutas = 0;
            int normalColetadas = 0;
            int vertFrutas = 0;
            int vertColetadas = 0;
            
            for (Personagem p : controller.getFaseAtual()) {
                if (p instanceof Fruta) {
                    normalFrutas++;
                    if (((Fruta) p).isColetada()) {
                        normalColetadas++;
                    }
                } else if (p instanceof FrutaVert) {
                    vertFrutas++;
                    if (((FrutaVert) p).foiColetado()) {
                        vertColetadas++;
                    }
                }
            }
            
            try {
                java.lang.reflect.Field totalFrutasField = Fruta.class.getDeclaredField("totalFrutas");
                totalFrutasField.setAccessible(true);
                totalFrutasField.set(null, normalFrutas);
                
                java.lang.reflect.Field frutasColeatadasField = Fruta.class.getDeclaredField("frutasColetadas");
                frutasColeatadasField.setAccessible(true);
                frutasColeatadasField.set(null, normalColetadas);
                
                java.lang.reflect.Field totalFrutasVertField = FrutaVert.class.getDeclaredField("totalFrutasVert");
                totalFrutasVertField.setAccessible(true);
                totalFrutasVertField.set(null, vertFrutas);
                
                java.lang.reflect.Field frutasVertColeatadasField = FrutaVert.class.getDeclaredField("frutasVertColetadas");
                frutasVertColeatadasField.setAccessible(true);
                frutasVertColeatadasField.set(null, vertColetadas);
            } catch (Exception e) {
                System.err.println("Error setting fruit counters: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (controller.getCameraManager() != null && controller.getHero() != null) {
                controller.getCameraManager().atualizaCamera(controller.getHero());
            }
            
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Game loaded successfully!");
                controller.getView().repaint();
            }
            
            System.out.println("Game loaded successfully from " + fileName);
            return true;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Error loading game!");
            }
            
            return false;
        }
    }
}
