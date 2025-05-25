package Controler;

import Modelo.Fruta;
import Modelo.FrutaVert;
import Modelo.GameState;
import Modelo.Hero;
import Modelo.Personagem;

import java.io.*;
import java.util.ArrayList;

/**
 * Class responsible for saving and loading game state.
 * This class provides methods to serialize and deserialize the game state to/from a .dat file.
 */
public class GameSaveManager {
    private static final String DEFAULT_SAVE_DIRECTORY = "saves";
    private static final String DEFAULT_SAVE_FILENAME = "gamestate.dat";
    
    /**
     * Saves the current game state to a file.
     * 
     * @param controller The TelaController containing the current game state
     * @param fileName Optional custom filename, uses default if null
     * @return true if save was successful, false otherwise
     */
    public static boolean saveGame(TelaController controller, String fileName) {
        // Use default filename if none provided
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = DEFAULT_SAVE_DIRECTORY + File.separator + DEFAULT_SAVE_FILENAME;
        }
        
        // Create directory if it doesn't exist
        File saveDir = new File(DEFAULT_SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(fileName))) {
            
            // Create a GameState object with current state
            GameState state = new GameState();
            
            // Save current level
            state.setCurrentLevel(controller.getFase().getLevel());
            
            // Save hero position if hero exists
            if (controller.getHero() != null) {
                state.setHeroPosition(controller.getHero().getPosicao());
                state.setGameOver(Hero.isGameOver());
            }
            
            // Save all characters in the current phase
            state.setFaseAtual(new ArrayList<>(controller.getFaseAtual()));
            
            // Count fruits
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
            
            // Write the object to file
            out.writeObject(state);
            System.out.println("Game saved successfully to " + fileName);
            
            // Show save message in the UI if view is available
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Game saved successfully!");
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message in the UI if view is available
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Error saving game!");
            }
            
            return false;
        }
    }
    
    /**
     * Loads a game state from a file.
     * 
     * @param controller The TelaController to load the game state into
     * @param fileName Optional custom filename, uses default if null
     * @return true if load was successful, false otherwise
     */
    public static boolean loadGame(TelaController controller, String fileName) {
        // Use default filename if none provided
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = DEFAULT_SAVE_DIRECTORY + File.separator + DEFAULT_SAVE_FILENAME;
        }
        
        // Check if file exists
        File saveFile = new File(fileName);
        if (!saveFile.exists()) {
            System.err.println("Save file does not exist: " + fileName);
            
            // Show error message in the UI if view is available
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Save file not found!");
            }
            
            return false;
        }
        
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(fileName))) {
            
            // Read the GameState object
            GameState state = (GameState) in.readObject();
            
            // First, reset the fruit counters
            Fruta.resetContadores();
            
            // IMPORTANT: We need to load the level first, but without using the saved state
            // This is because carregarFase() resets everything
            int currentLevel = state.getCurrentLevel();
            controller.carregarFase(currentLevel);
            
            // Now replace the current phase with the saved one
            controller.getFaseAtual().clear();
            controller.getFaseAtual().addAll(state.getFaseAtual());
            
            // Make sure the hero reference is updated
            for (Personagem p : controller.getFaseAtual()) {
                if (p instanceof Hero) {
                    controller.setHero((Hero) p);
                    break;
                }
            }
            
            // Restore game over state if needed
            if (state.isGameOver()) {
                Hero.setGameOver(true);
            } else {
                Hero.resetGameOver();
            }
            
            // Count fruits and update static counters
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
            
            // Manually set the static counters to match the loaded state
            // We need to use reflection since these are private static fields
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
            
            // Update camera to focus on hero if available
            if (controller.getCameraManager() != null && controller.getHero() != null) {
                controller.getCameraManager().atualizaCamera(controller.getHero());
            }
            
            // Update view if available
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Game loaded successfully!");
                controller.getView().repaint();
            }
            
            System.out.println("Game loaded successfully from " + fileName);
            return true;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message in the UI if view is available
            if (controller.getView() != null) {
                controller.getView().showSaveLoadMessage("Error loading game!");
            }
            
            return false;
        }
    }
}
