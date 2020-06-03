package ambm.setting;

import ambm.ambm.TempManage;
import ambm.captcha.CaptchaController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService  {

    private static Logger logger = LoggerFactory.getLogger(SettingService.class);

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private CaptchaController captchaController;

    /***
     * 初始化的时候展示到页面上
     * @param username
     * @param password
     * @param ip
     * @param port
     * @param proxyUsername
     * @param proxyPassword
     */
    public void initSetting(String username, String password, TextField ip, TextField port, TextField proxyUsername, TextField proxyPassword){
        SettingTable settingTable= settingDao.proxyByUsernameAndPassword(username,password);
        ip.setText(settingTable.getIp());
        port.setText(settingTable.getPort());
        proxyUsername.setText(settingTable.getProxyUsername());
        proxyPassword.setText(settingTable.getProxyPassword());
    }

    public void save(String username, String password, TextField ip, TextField port, TextField proxyUsername, TextField proxyPassword, CheckBox startProxy){
        if(startProxy.isSelected()){
            captchaController.setProxy(ip.getText(),port.getText(),proxyUsername.getText(),proxyPassword.getText());
        }
        settingDao.proxySaveOrUpdate(ip.getText(), port.getText(),  proxyUsername.getText(),  proxyPassword.getText(), TempManage.temp.get("username"),TempManage.temp.get("password"),startProxy.isSelected());
        Stage stage = (Stage) ip.getScene().getWindow();
        stage.close();
    }


}
