package ambm.login;

import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



@FXMLController
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label mess;

    @Autowired
    LoginService loginService;

    @FXML
    public void login(){
        loginService.login(mess,username.getText(),password.getText());
    }

    @FXML
    public  void clearText(){
        username.clear();
        password.clear();
    }

}
