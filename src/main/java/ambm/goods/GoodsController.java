package ambm.goods;

import ambm.task.ProxyController;
import ambm.task.TaskParentsAPI;
import ambm.task.TaskTable;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class GoodsController implements Initializable, TaskParentsAPI {



    @FXML
    public TextField keywords;

    @FXML
    public ComboBox size;

    @FXML
    public  ComboBox backupSize;

    @FXML
    public RadioButton fuzzyMatch;

    @FXML
    public RadioButton newGoods;

    @FXML
    public RadioButton startCaptcha;


    @Autowired
    GoodsService goodsService;

    /*******
     * 需要初始化尺寸&；
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    /*****
     * 初始化尺寸
     */
    @Override
    public void init(String website, Integer taskId) {
        goodsService.sizesByWebsite(website, size,backupSize);
    }

    @Override
    public void editInit(TaskTable taskTable, GoodsController goodsController, ProxyController proxyController) {

    }

    @Override
    public void save(String website, GoodsController goodsController, ProxyController proxyController) {

    }


}
