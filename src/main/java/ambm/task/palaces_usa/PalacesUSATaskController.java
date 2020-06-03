package ambm.task.palaces_usa;

import ambm.ambm.TempManage;
import ambm.basic.CountryTable;
import ambm.goods.GoodsController;
import ambm.task.ProxyController;
import ambm.task.TaskParentsAPI;
import ambm.task.TaskService;
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
public class PalacesUSATaskController implements Initializable, TaskParentsAPI {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private TextField apartment;
    @FXML
    private TextField city;
    @FXML
    private ComboBox country;
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
    private RadioButton startCaptcha;
    @FXML
    private ComboBox countyState;//这个是国家地区；不是状态；是美国站的

    private String website;

    private   Integer taskId=null;

    @Autowired
    PalacesUSAService palacesUSAService;

    @Autowired
    TaskService taskService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /***
     * 选择country;
     */
    public void selectCountry() {
        if (country.getSelectionModel() != null && country != null)
            palacesUSAService.provincesByWebsiteAndCountry(website, country.getSelectionModel().getSelectedItem().toString(), countyState);
    }

    /****
     * 这个是历史人的选择...
     * 暂时还没有做
     */
    @FXML
    private void selectPerson() {

    }

    @Override
    public void init(String website, Integer taskId) {
        this.taskId=taskId;
        this.website = website;
        /*****
         * 初始化~country;
         */
        palacesUSAService.countrysByWebsite(website, country);
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
        this.lastName.setText(taskTable.getLash_name());
        this.email.setText(taskTable.getEmail());
        this.address.setText(taskTable.getAddress());
        this.apartment.setText(taskTable.getApartment());
        this.city.setText(taskTable.getCity());
        this.country.setValue(taskTable.getCountry());
        this.countyState.setValue(taskTable.getCounty_state());
        this.postCode.setText(taskTable.getPostcode());
        this.phone.setText(taskTable.getPhone());

        this.cardNumber.setText(taskTable.getCard_number());
        this.cardHolderName.setText(taskTable.getCard_holder_name());
        this.MMYY.setText(taskTable.getMmyy());
        this.CVV.setText(taskTable.getCvv());

    }

    @Override
    public void save(String website, GoodsController goodsController, ProxyController proxyController) {
        String countryV = country.getSelectionModel().getSelectedItem() == null ? "" : country.getSelectionModel().getSelectedItem().toString();
        String countryStateV = countyState.getSelectionModel().getSelectedItem() == null ? "" : countyState.getSelectionModel().getSelectedItem().toString();
        CountryTable countryTable= palacesUSAService.countryByWebsiteAndProvince(website,countryStateV);

        TaskTable taskTable=new TaskTable();
        if(this.taskId!=null)
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

        taskTable.setFirst_name(firstName.getText());
        taskTable.setLash_name(lastName.getText());
        taskTable.setEmail(email.getText());
        taskTable.setAddress(address.getText());
        taskTable.setApartment(apartment.getText());
        taskTable.setCity(city.getText());
        taskTable.setCountry(countryV);
        taskTable.setPostcode(postCode.getText());
        taskTable.setPhone(phone.getText());
        taskTable.setCard_number(cardNumber.getText());
        taskTable.setCard_holder_name(cardHolderName.getText());
        taskTable.setMmyy(MMYY.getText());
        taskTable.setCvv(CVV.getText());
        taskTable.setProvince(countryTable==null?"":countryTable.getProvincesCode());
        taskTable.setWebsite(website);
        taskTable.setUsername(TempManage.temp.get("username"));
        taskTable.setPassword(TempManage.temp.get("password"));

        taskTable.setStart_captcha(goodsController.startCaptcha.isSelected());
        taskTable.setCounty_state(countryStateV);
        taskService.taskSave(taskTable);

        /**
         * 页面上编辑完，一定要及关闭页面，不然有bug；
         */
        this.taskId=null;
    }



}
