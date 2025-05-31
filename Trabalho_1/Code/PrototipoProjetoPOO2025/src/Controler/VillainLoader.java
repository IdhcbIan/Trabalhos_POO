package Controler;

import Auxiliar.Consts;
import Modelo.Fireball;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.Villan_1;
import Modelo.Villan_2;
import java.io.File;
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

public class VillainLoader {
    private static final String TEMP_DIR = "temp_villains";
    
    private TelaController controller;
    private Hero hero;
    
    public VillainLoader(TelaController controller) {
        this.controller = controller;
        this.hero = controller.getHero();
        
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
    }
    
    public ArrayList<Personagem> loadVillainsFromZip(String zipFilePath) throws IOException {
        ArrayList<Personagem> loadedVillains = new ArrayList<>();
        
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Map<String, Properties> villainConfigs = extractVillainConfigs(zipFile);
            
            extractVillainImages(zipFile);
            
            for (Map.Entry<String, Properties> entry : villainConfigs.entrySet()) {
                String villainName = entry.getKey();
                Properties config = entry.getValue();
                
                Personagem villain = createVillainFromConfig(villainName, config);
                if (villain != null) {
                    loadedVillains.add(villain);
                    controller.addPersonagem(villain);
                    
                    if (villain instanceof Modelo.Villan_2) {
                        Modelo.Villan_2 v2 = (Modelo.Villan_2) villain;
                        v2.setTarget(hero);
                        
                        if (!Hero.isGameOver() && !Modelo.SuccessoNotification.getInstance().isGameFreeze()) {
                            v2.processLogic();
                        }
                        
                        ArrayList<Fireball> fireballs = Villan_2.getPendingFireballs();
                        for (Fireball fireball : fireballs) {
                            controller.addPersonagem(fireball);
                        }
                    }
                    
                    try {
                        Class<?> villan3Class = Class.forName("Modelo.Villan_3");
                        if (villan3Class.isInstance(villain)) {
                            try {
                                java.lang.reflect.Method setTarget = villan3Class.getMethod("setTarget", Hero.class);
                                setTarget.invoke(villain, hero);
                            } catch (Exception e) {
                                System.out.println("Error setting target for Villan_3: " + e.getMessage());
                            }
                        }
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        }
        
        return loadedVillains;
    }
    
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
    
    private void extractVillainImages(ZipFile zipFile) throws IOException {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            
            if (isImageFile(entry.getName())) {
                String fileName = new File(entry.getName()).getName();
                
                String imgsPath;
                try {
                    imgsPath = new File(".").getCanonicalPath() + File.separator + "imgs";
                } catch (IOException e) {
                    System.out.println("Error getting canonical path: " + e.getMessage());
                    imgsPath = "imgs"; 
                }
                
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
                String type = config.getProperty("type");
                if (type == null) {
                    System.out.println("Neither class nor type specified for " + villainName);
                    return null;
                }
                
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
            
            String imgsPath;
            try {
                imgsPath = new File(".").getCanonicalPath() + Consts.PATH;
            } catch (IOException e) {
                System.out.println("Error getting canonical path: " + e.getMessage());
                imgsPath = "imgs" + File.separator; 
            }
            
            File imageFile = new File(imgsPath + imageName);
            if (!imageFile.exists()) {
                System.out.println("Image file not found: " + imageFile.getAbsolutePath());
                imageName = "Villan_1.png";
            } else {
                System.out.println("Using image: " + imageFile.getAbsolutePath());
            }
            
            int linha = 1;
            int coluna = 1;
            
            try {
                String linhaStr = config.getProperty("linha", "1").trim();
                if (linhaStr.contains("#")) {
                    linhaStr = linhaStr.substring(0, linhaStr.indexOf("#")).trim();
                }
                linha = Integer.parseInt(linhaStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid linha value for " + villainName + ": " + e.getMessage());
                linha = 1;
            }
            
            try {
                String colunaStr = config.getProperty("coluna", "1").trim();
                if (colunaStr.contains("#")) {
                    colunaStr = colunaStr.substring(0, colunaStr.indexOf("#")).trim();
                }
                coluna = Integer.parseInt(colunaStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid coluna value for " + villainName + ": " + e.getMessage());
                coluna = 1;
            }
            
            Personagem villain = null;
            
            if (className.equals("Modelo.Villan_1")) {
                int walkBlocks = 12;
                int moveRate = 1;
                
                try {
                    String walkBlocksStr = config.getProperty("walkBlocks", "12").trim();
                    if (walkBlocksStr.contains("#")) {
                        walkBlocksStr = walkBlocksStr.substring(0, walkBlocksStr.indexOf("#")).trim();
                    }
                    walkBlocks = Integer.parseInt(walkBlocksStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid walkBlocks value for " + villainName + ": " + e.getMessage());
                }
                
                try {
                    String moveRateStr = config.getProperty("moveRate", "1").trim();
                    if (moveRateStr.contains("#")) {
                        moveRateStr = moveRateStr.substring(0, moveRateStr.indexOf("#")).trim();
                    }
                    moveRate = Integer.parseInt(moveRateStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid moveRate value for " + villainName + ": " + e.getMessage());
                }
                
                Villan_1 v1 = new Villan_1(imageName, walkBlocks, moveRate);
                v1.setPosicao(linha, coluna);
                villain = v1;
                
            } else if (className.equals("Modelo.Villan_2")) {
                Villan_2 v2 = new Villan_2(imageName);
                v2.setPosicao(linha, coluna);
                
                int shootRate = 20;
                boolean shootRight = true;
                
                try {
                    String shootRateStr = config.getProperty("shootRate", "20").trim();
                    if (shootRateStr.contains("#")) {
                        shootRateStr = shootRateStr.substring(0, shootRateStr.indexOf("#")).trim();
                    }
                    shootRate = Integer.parseInt(shootRateStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid shootRate value for " + villainName + ": " + e.getMessage());
                }
                
                try {
                    String shootRightStr = config.getProperty("shootRight", "true").trim();
                    if (shootRightStr.contains("#")) {
                        shootRightStr = shootRightStr.substring(0, shootRightStr.indexOf("#")).trim();
                    }
                    shootRight = Boolean.parseBoolean(shootRightStr);
                } catch (Exception e) {
                    System.out.println("Invalid shootRight value for " + villainName + ": " + e.getMessage());
                }
                
                v2.setTarget(hero);
                v2.setShootRate(shootRate);
                v2.setShootDirection(shootRight);
                
                villain = v2;
                
            } else if (className.equals("Modelo.Villan_3")) {
                try {
                    Class.forName("Modelo.Villan_3");
                    
                    Class<?> villan3Class = Class.forName(className);
                    java.lang.reflect.Constructor<?> constructor = villan3Class.getConstructor(String.class);
                    villain = (Personagem) constructor.newInstance(imageName);
                    
                    villain.setPosicao(linha, coluna);
                    
                    int moveRate = 1;
                    try {
                        String moveRateStr = config.getProperty("moveRate", "1").trim();
                        if (moveRateStr.contains("#")) {
                            moveRateStr = moveRateStr.substring(0, moveRateStr.indexOf("#")).trim();
                        }
                        moveRate = Integer.parseInt(moveRateStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid moveRate value for " + villainName + ": " + e.getMessage());
                    }
                    
                    try {
                        java.lang.reflect.Method setTarget = villan3Class.getMethod("setTarget", Hero.class);
                        setTarget.invoke(villain, hero);
                        
                        java.lang.reflect.Method setMoveRate = villan3Class.getMethod("setMoveRate", int.class);
                        setMoveRate.invoke(villain, moveRate);
                    } catch (Exception e) {
                        System.out.println("Error setting properties for Villan_3: " + e.getMessage());
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Villan_3 class not found. Skipping this villain.");
                    return null;
                } catch (Exception e) {
                    System.out.println("Error creating Villan_3: " + e.getMessage());
                    return null;
                }
            } else {
                try {
                    Class<?> villainClass = Class.forName(className);
                    java.lang.reflect.Constructor<?> constructor = villainClass.getConstructor(String.class);
                    villain = (Personagem) constructor.newInstance(imageName);
                    villain.setPosicao(linha, coluna);
                } catch (Exception e) {
                    System.out.println("Error creating villain of class " + className + ": " + e.getMessage());
                    return null;
                }
            }
            
            return villain;
        } catch (Exception e) {
            System.out.println("Error creating villain " + villainName + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private boolean isImageFile(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".png") || 
               lowerCaseName.endsWith(".jpg") || 
               lowerCaseName.endsWith(".jpeg") || 
               lowerCaseName.endsWith(".gif");
    }
    
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
