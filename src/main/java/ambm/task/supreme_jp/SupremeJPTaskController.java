package ambm.task.supreme_jp;

import ambm.ambm.TempManage;
import ambm.goods.GoodsController;
import ambm.task.*;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
public class SupremeJPTaskController implements Initializable, TaskParentsAPI {

    private static Logger logger = LoggerFactory.getLogger(SupremeJPTaskController.class);
    @FXML
    private TextField firstName;

    @FXML
    private  TextField email;
    @FXML
    private  TextField address;

    @FXML
    private  TextField city;

    @FXML
    private TextField postCode;
    @FXML
    private TextField phone;
    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cardHolderName;
    @FXML
    private TextField MMYY;
    @FXML
    private TextField CVV;
    @FXML
    RadioButton savePerson;
    @FXML
    ComboBox historyPerson;

    @FXML
    private ComboBox countyState ;//这个是国家地区；不是状态；

    @FXML
    private ComboBox payMethod ;

    private String website;

    private Integer taskId;

    @Autowired
    SupremeJPService supremeJPService;

    @Autowired
    TaskService taskService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    /***
     * 这个是选择count
     */
    public void selectCountryState(){
    }

    /****
     * 这个是历史人的选择...
     * 暂时还没有做
     */
    @FXML
    private void selectPerson(){

    }


    /***
     * 初始化面板的时候调用的
     * @param website
     */
    @Override
    public void init(String website, Integer taskId) {
        this.website=website;
        this.taskId=taskId;
        /*****
         * 初始化~country;
         * http://127.0.0.1:9999/ProvincesByWebsiteAndCountry?website=supreme-jp&countryName=jp
         */
        supremeJPService.provincesByWebsiteAndCountry(website,"jp",countyState);
        /****
         * 初始化payMethod
         */
        supremeJPService.initSelectPayMethod(payMethod);

    }

    @Override
    public void editInit(TaskTable taskTable, GoodsController goodsController, ProxyController proxyController) {
        goodsController.keywords.setText(taskTable.getKeywords());
        goodsController.size.setValue(taskTable.getSize());
        goodsController.backupSize.setValue(taskTable.getBackup_size());
        goodsController.fuzzyMatch.setSelected(taskTable.isFuzzy_match());
        goodsController.newGoods.setSelected(taskTable.isNew_goods());
        goodsController.startCaptcha.setSelected(taskTable.start_captcha);

        proxyController.ip.setText(taskTable.getIp());
        proxyController.port.setText(taskTable.getPort());
        proxyController.proxyUserName.setText(taskTable.getProxy_username());
        proxyController.proxyPassword.setText(taskTable.getProxy_password());
        proxyController.proxy.setSelected(taskTable.isProxy());

        this.firstName.setText(taskTable.getFirst_name());
        this.email.setText(taskTable.getEmail());
        this.phone.setText(taskTable.getPhone());
        this.postCode.setText(taskTable.getPostcode());
        this.city.setText(taskTable.getCity());
        this.address.setText(taskTable.getAddress());
        this.countyState.setValue(taskTable.getCounty_state());

        this.payMethod.setValue(taskTable.getPay_method());
        this.cardNumber.setText(taskTable.getCard_number());
        this.cardHolderName.setText(taskTable.getCard_holder_name());
        this.MMYY.setText(taskTable.getMmyy());
        this.CVV.setText(taskTable.getCvv());


    }

    @Override
    public void save(String website, GoodsController goodsController, ProxyController proxyController) {
        logger.info("website:"+website);

        TaskTable taskTable= new TaskTable();
        if(taskId!=null)
        taskTable.setId(taskId);
        taskTable.setKeywords(goodsController.keywords.getText());
        taskTable.setSize(goodsController.size.getSelectionModel().getSelectedItem().toString());
        taskTable.setBackup_size(goodsController.backupSize.getSelectionModel().getSelectedItem().toString());
        taskTable.setFuzzy_match(goodsController.fuzzyMatch.isSelected());
        taskTable.setNew_goods(goodsController.newGoods.isSelected());
        taskTable.setStart_captcha(goodsController.startCaptcha.isSelected());

        taskTable.setIp(proxyController.ip.getText());
        taskTable.setPort(proxyController.port.getText());
        taskTable.setProxy_username(proxyController.proxyUserName.getText());
        taskTable.setProxy_password(proxyController.proxyPassword.getText());
        taskTable.setProxy(proxyController.proxy.isSelected());

        taskTable.setFirst_name(this.firstName.getText());
        taskTable.setEmail(this.email.getText());
        taskTable.setPhone(this.phone.getText());
        taskTable.setPostcode(this.postCode.getText());
        taskTable.setCity(this.city.getText());
        taskTable.setAddress(this.address.getText());
        taskTable.setCounty_state(this.countyState.getSelectionModel().getSelectedItem().toString());

        taskTable.setPay_method(this.payMethod.getSelectionModel().getSelectedItem().toString());
        taskTable.setCard_number(this.cardNumber.getText());
        taskTable.setCard_holder_name(this.cardHolderName.getText());
        taskTable.setMmyy(this.MMYY.getText());
        taskTable.setCvv(this.CVV.getText());

        taskTable.setUsername(TempManage.temp.get("username"));
        taskTable.setPassword(TempManage.temp.get("password"));
        taskTable.setWebsite(website);

        taskService.taskSave(taskTable);

        /***
         * this.taskId 用完就置位null;
         */
        this.taskId=null;
    }


}
