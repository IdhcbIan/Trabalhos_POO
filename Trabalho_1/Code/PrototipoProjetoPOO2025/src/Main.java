import Controler.CameraManager;
import Controler.TelaController;
import Controler.TelaView;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TelaView view = new TelaView();
                TelaController controller = new TelaController();
                CameraManager cam = new CameraManager();
                
                controller.setView(view);
                controller.setCameraManager(cam);
                
                view.setController(controller);
                view.setCameraManager(cam);
                
                controller.carregarFase(1);
                
                view.setVisible(true);
                view.go();    
            }
        });
    }
}
