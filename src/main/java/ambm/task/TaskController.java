package ambm.task;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class TaskController implements Initializable {
    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @FXML
    private ComboBox websiteOption;

    @FXML
    private Pane goodsPane;

    @FXML
    private Pane persionPane;

    @FXML
    private Pane proxyPane;

    private String website;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //初始化的网站列表
        taskService.initSelectWebsite(websiteOption);
    }

    /****
     * 选择网站，然后显示对应的面板
     */
    @FXML
    private void selectWebsite() {
        taskService.selectPane(websiteOption.getSelectionModel().getSelectedItem().toString(),goodsPane,proxyPane,persionPane);
    }

    /***
     * 将home页面的编辑功能，转译到这里来处理
     * 这个是对外提供能力的；本身类并没什么用；
     */
    public void editTask(TaskTable taskTable){
        taskService.edit(taskTable,goodsPane,proxyPane,persionPane,websiteOption);
    }



    @FXML
    private void addTask(){
        taskService.saveTask();
    }

}
