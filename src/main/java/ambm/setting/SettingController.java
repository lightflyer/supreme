package ambm.setting;

import ambm.ambm.TempManage;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class SettingController implements Initializable {

    @FXML
    private TextField ip;
    @FXML
    private TextField port;

    @FXML
    private TextField proxyUsername;

    @FXML
    private TextField proxyPassword;

    @FXML
    private CheckBox startProxy;

    @Autowired
    private SettingService settingService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * 当打开的时候初始化内容;
         */
        settingService.initSetting(TempManage.temp.get("username"), TempManage.temp.get("password"), ip, port, proxyUsername, proxyPassword);
    }

    /***
     * 保存
     */
    public void save() {
        settingService.save(TempManage.temp.get("username"), TempManage.temp.get("password"), ip, port, proxyUsername, proxyPassword, startProxy);
    }


}
