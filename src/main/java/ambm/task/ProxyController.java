package ambm.task;

import ambm.goods.GoodsController;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@FXMLController
public class ProxyController implements TaskParentsAPI{

    @FXML
    public TextField ip;
    @FXML
    public TextField port;
    @FXML
    public TextField proxyUserName;
    @FXML
    public PasswordField proxyPassword;
    @FXML
    public CheckBox proxy;

    @FXML
    private void proxyTest(){

    }

    @Override
    public void init(String website, Integer taskId) {

    }

    @Override
    public void editInit(TaskTable taskTable, GoodsController goodsController, ProxyController proxyController) {

    }

    @Override
    public void save(String website, GoodsController goodsController, ProxyController proxyController) {

    }



}
