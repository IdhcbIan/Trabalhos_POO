package Controler;

import Modelo.Villan_1;
import Modelo.Villan_2;
import Modelo.Villan_3;
import Modelo.Hero;
import Modelo.Personagem;
import Auxiliar.Consts;
import auxiliar.Posicao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class responsible for loading villain characters from zip files
 */
public class VillainLoader {
    private static final String TEMP_DIR = "temp_villains";
    
    private TelaController controller;
    private Hero hero;
    
    public VillainLoader(TelaController controller) {
        this.controller = controller;
        this.hero = controller.getHero();
        
        // Create temp directory if it doesn't exist
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
    }
    
    /**
     * Loads villains from a zip file and adds them to the game
     * @param zipFilePath Path to the zip file containing villain data
     * @return List of loaded villains
     * @throws IOException If there's an error reading the zip file
     */
    public ArrayList<Personagem> loadVillainsFromZip(String zipFilePath) throws IOException {
        ArrayList<Personagem> loadedVillains = new ArrayList<>();
        
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            // Extract config file first
            Map<String, Properties> villainConfigs = extractVillainConfigs(zipFile);
            
            // Extract images
            extractVillainImages(zipFile);
            
            // Create villains based on configs
            for (Map.Entry<String, Properties> entry : villainConfigs.entrySet()) {
                String villainName = entry.getKey();
                Properties config = entry.getValue();
                
                Personagem villain = createVillainFromConfig(villainName, config);
                if (villain != null) {
                    loadedVillains.add(villain);
                    controller.addPersonagem(villain);
                }
            }
        }
        
        return loadedVillains;
    }
    
    /**
     * Extract villain configuration files from the zip
     */
    private Map<String, Properties> extractVillainConfigs(ZipFile zipFile) throws IOException {
        Map<String, Properties> configs = new HashMap<>();
        
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            
            if (entry.getName().endsWith(".properties")) {
                String villainName = entry.getName().replace(".properties", "");
                
                Properties props = new Properties();
                try (InputStream is = zipFile.getInputStream(entry)) {
                    props.load(is);
                    configs.put(villainName, props);
                }
            }
        }
        
        return configs;
    }
    
    /**
     * Extract villain images from the zip to the imgs directory
     */
    private void extractVillainImages(ZipFile zipFile) throws IOException {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            
            if (isImageFile(entry.getName())) {
                String fileName = new File(entry.getName()).getName();
                
                // Get the correct path for the images directory
                String imgsPath;
                try {
                    imgsPath = new File(".").getCanonicalPath() + File.separator + "imgs";
                } catch (IOException e) {
                    System.out.println("Error getting canonical path: " + e.getMessage());
                    imgsPath = "imgs"; // Fallback
                }
                
                // Make sure the imgs directory exists
                File imgDir = new File(imgsPath);
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                
                Path targetPath = Paths.get(imgsPath, fileName);
                System.out.println("Extracting image to: " + targetPath.toAbsolutePath());
                
                try (InputStream is = zipFile.getInputStream(entry)) {
                    Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    
    protected Personagem createVillainFromConfig(String villainName, Properties config) {
        try {
            String className = config.getProperty("class");
            if (className == null) {
                // Backward compatibility: use type if class is not specified
                String type = config.getProperty("type");
                if (type == null) {
                    System.out.println("Neither class nor type specified for " + villainName);
                    return null;
                }
                
                // Convert type to class name
                switch (type) {
                    case "villan_1":
                        className = "Modelo.Villan_1";
                        break;
                    case "villan_2":
                        className = "Modelo.Villan_2";
                        break;
                    case "villan_3":
                        className = "Modelo.Villan_3";
                        break;
                    default:
                        System.out.println("Unknown villain type: " + type);
                        return null;
                }
            }
            
            String imageName = config.getProperty("image");
            if (imageName == null) {
                System.out.println("Image not specified for " + villainName);
                return null;
            }
            
            // Check if the image file exists in the imgs directory
            String imgsPath;
            try {
                imgsPath = new File(".").getCanonicalPath() + Consts.PATH;
            } catch (IOException e) {
                System.out.println("Error getting canonical path: " + e.getMessage());
                imgsPath = "imgs" + File.separator; // Fallback
            }
            
            File imageFile = new File(imgsPath + imageName);
            if (!imageFile.exists()) {
                System.out.println("Image file not found: " + imageFile.getAbsolutePath());
                // Try to use a default image instead
                imageName = "Villan_1.png"; // Default villain image
            } else {
                System.out.println("Using image: " + imageFile.getAbsolutePath());
            }
            
            // Parse linha and coluna with better error handling
            int linha = 1;
            int coluna = 1;
            
            try {
                String linhaStr = config.getProperty("linha", "1").trim();
                // Remove any comments (text after #)
                if (linhaStr.contains("#")) {
                    linhaStr = linhaStr.substring(0, linhaStr.indexOf("#")).trim();
                }
                linha = Integer.parseInt(linhaStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid linha value for " + villainName + ": " + e.getMessage());
                // Use default value
                linha = 1;
            }
            
            try {
                String colunaStr = config.getProperty("coluna", "1").trim();
                // Remove any comments (text after #)
                if (colunaStr.contains("#")) {
                    colunaStr = colunaStr.substring(0, colunaStr.indexOf("#")).trim();
                }
                coluna = Integer.parseInt(colunaStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid coluna value for " + villainName + ": " + e.getMessage());
                // Use default value
                coluna = 1;
            }
            
            // Dynamically load the villain class
            try {
                Class<?> villainClass = Class.forName(className);
                
                // Check if it's a Personagem subclass
                if (!Personagem.class.isAssignableFrom(villainClass)) {
                    System.out.println("Class " + className + " is not a subclass of Personagem");
                    return null;
                }
                
                // Create the villain instance
                Personagem villain = null;
                
                // Try to find a constructor that takes just the image name
                try {
                    java.lang.reflect.Constructor<?> constructor = villainClass.getConstructor(String.class);
                    villain = (Personagem) constructor.newInstance(imageName);
                } catch (NoSuchMethodException e) {
                    // Try other constructors based on the available properties
                    if (className.equals("Modelo.Villan_1")) {
                        // Try Villan_1 specific constructors
                        int walkBlocks = 12;
                        int moveRate = 1;
                        
                        try {
                            String walkBlocksStr = config.getProperty("walkBlocks", "12").trim();
                            if (walkBlocksStr.contains("#")) {
                                walkBlocksStr = walkBlocksStr.substring(0, walkBlocksStr.indexOf("#")).trim();
                            }
                            walkBlocks = Integer.parseInt(walkBlocksStr);
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid walkBlocks value for " + villainName + ": " + ex.getMessage());
                        }
                        
                        try {
                            String moveRateStr = config.getProperty("moveRate", "1").trim();
                            if (moveRateStr.contains("#")) {
                                moveRateStr = moveRateStr.substring(0, moveRateStr.indexOf("#")).trim();
                            }
                            moveRate = Integer.parseInt(moveRateStr);
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid moveRate value for " + villainName + ": " + ex.getMessage());
                        }
                        
                        try {
                            java.lang.reflect.Constructor<?> constructor = villainClass.getConstructor(String.class, int.class, int.class);
                            villain = (Personagem) constructor.newInstance(imageName, walkBlocks, moveRate);
                        } catch (NoSuchMethodException ex) {
                            // Try with just walkBlocks
                            try {
                                java.lang.reflect.Constructor<?> constructor = villainClass.getConstructor(String.class, int.class);
                                villain = (Personagem) constructor.newInstance(imageName, walkBlocks);
                            } catch (NoSuchMethodException ex2) {
                                System.out.println("No suitable constructor found for " + className);
                                return null;
                            }
                        }
                    } else if (className.equals("Modelo.Villan_2")) {
                        // Create Villan_2 instance
                        villain = (Personagem) villainClass.getConstructor(String.class).newInstance(imageName);
                        
                        // Set additional properties
                        int shootRate = 20;
                        boolean shootRight = true;
                        
                        try {
                            String shootRateStr = config.getProperty("shootRate", "20").trim();
                            if (shootRateStr.contains("#")) {
                                shootRateStr = shootRateStr.substring(0, shootRateStr.indexOf("#")).trim();
                            }
                            shootRate = Integer.parseInt(shootRateStr);
                        } catch (NumberFormatException ex) {
                            System.out.println("Invalid shootRate value for " + villainName + ": " + ex.getMessage());
                        }
                        
                        try {
                            String shootRightStr = config.getProperty("shootRight", "true").trim();
                            if (shootRightStr.contains("#")) {
                                shootRightStr = shootRightStr.substring(0, shootRightStr.indexOf("#")).trim();
                            }
                            shootRight = Boolean.parseBoolean(shootRightStr);
                        } catch (Exception ex) {
                            System.out.println("Invalid shootRight value for " + villainName + ": " + ex.getMessage());
                        }
                        
                        // Call setter methods using reflection
                        try {
                            java.lang.reflect.Method setTarget = villainClass.getMethod("setTarget", Hero.class);
                            setTarget.invoke(villain, hero);
                            
                            java.lang.reflect.Method setShootRate = villainClass.getMethod("setShootRate", int.class);
                            setShootRate.invoke(villain, shootRate);
                            
                            java.lang.reflect.Method setShootDirection = villainClass.getMethod("setShootDirection", boolean.class);
                            setShootDirection.invoke(villain, shootRight);
                        } catch (Exception ex) {
                            System.out.println("Error setting properties for " + className + ": " + ex.getMessage());
                        }
                    } else {
                        // For other villain types, try to create with default constructor
                        try {
                            villain = (Personagem) villainClass.getConstructor(String.class).newInstance(imageName);
                            
                            // Try to set target if method exists
                            try {
                                java.lang.reflect.Method setTarget = villainClass.getMethod("setTarget", Hero.class);
                                setTarget.invoke(villain, hero);
                            } catch (NoSuchMethodException ex) {
                                // Method doesn't exist, that's fine
                            }
                            
                            // Try to set moveRate if method exists
                            try {
                                int moveRate = 1;
                                try {
                                    String moveRateStr = config.getProperty("moveRate", "1").trim();
                                    if (moveRateStr.contains("#")) {
                                        moveRateStr = moveRateStr.substring(0, moveRateStr.indexOf("#")).trim();
                                    }
                                    moveRate = Integer.parseInt(moveRateStr);
                                } catch (NumberFormatException ex) {
                                    System.out.println("Invalid moveRate value for " + villainName + ": " + ex.getMessage());
                                }
                                
                                java.lang.reflect.Method setMoveRate = villainClass.getMethod("setMoveRate", int.class);
                                setMoveRate.invoke(villain, moveRate);
                            } catch (NoSuchMethodException ex) {
                                // Method doesn't exist, that's fine
                            }
                        } catch (NoSuchMethodException ex) {
                            System.out.println("No suitable constructor found for " + className);
                            return null;
                        }
                    }
                }
                
                // Set position
                if (villain != null) {
                    villain.setPosicao(linha, coluna);
                }
                
                return villain;
            } catch (ClassNotFoundException e) {
                System.out.println("Villain class not found: " + className);
                return null;
            } catch (Exception e) {
                System.out.println("Error creating villain instance: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error creating villain " + villainName + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Check if a file is an image based on its extension
     */
    private boolean isImageFile(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".png") || 
               lowerCaseName.endsWith(".jpg") || 
               lowerCaseName.endsWith(".jpeg") || 
               lowerCaseName.endsWith(".gif");
    }
    
    /**
     * Clean up temporary files
     */
    public void cleanup() {
        try {
            File tempDir = new File(TEMP_DIR);
            if (tempDir.exists()) {
                for (File file : tempDir.listFiles()) {
                    file.delete();
                }
                tempDir.delete();
            }
        } catch (Exception e) {
            System.out.println("Error cleaning up temp files: " + e.getMessage());
        }
    }
}
