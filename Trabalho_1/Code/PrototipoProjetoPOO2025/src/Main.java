import Controler.CameraManager;
import Controler.TelaController;
import Controler.TelaView;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Create the view
                TelaView view = new TelaView();
                
                // Create the controller and camera manager
                TelaController controller = new TelaController();
                CameraManager cameraManager = new CameraManager();
                
                // Set up the connections between components
                controller.setView(view);
                controller.setCameraManager(cameraManager);
                
                view.setController(controller);
                view.setCameraManager(cameraManager);
                
                // Load the first phase
                controller.carregarFase(5);
                
                // Start the game loop
                view.setVisible(true);
                view.go();
            }
        });
    }
}


