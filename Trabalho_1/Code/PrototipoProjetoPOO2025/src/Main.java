import Controler.CameraManager;
import Controler.TelaController;
import Controler.TelaView;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Create the view, controller and camera manager
                TelaView view = new TelaView();
                TelaController controller = new TelaController();
                CameraManager cam = new CameraManager();
                
                // Set up the connections between components
                controller.setView(view);
                controller.setCameraManager(cam);
                
                view.setController(controller);
                view.setCameraManager(cam);
                
                // Load the first phase
                controller.carregarFase(3);
                
                // Start the game loop
                view.setVisible(true);
                view.go();    // start the update+repaint timer
            }
        });
    }
}
