package ambm.task;

import ambm.ambm.SpringContextUtil;
import ambm.goods.GoodsController;
import ambm.goods.GoodsView;
import ambm.home.HomeController;
import ambm.task.palaces_usa.PalacesUSATaskController;
import ambm.task.palaces_usa.PalacesUSATaskView;
import ambm.task.supreme_jp.SupremeJPTaskController;
import ambm.task.supreme_jp.SupremeJPTaskView;
import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
public class TaskService {

    private static Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private GoodsView goodsView;

    @Autowired
    private ProxyView proxyView;

    private TaskParentsAPI taskControllerAPI;

    private String  website;

    @Autowired
    private GoodsController goodsController;

    @Autowired
    private ProxyController proxyController;

    @Autowired
    HomeController homeController;

    /**
     * 初始化的时候的下拉菜单...
     *
     * @param websiteOption
     */
    void initSelectWebsite(ComboBox websiteOption) {
        websiteOption.getItems().clear();
        taskDao.allBaseWebs().forEach(e -> {
            websiteOption.getItems().add(e.getWebsite());
        });
    }

    /*****
     * 选择网站pane
     * @param website
     * @throws IOException
     */
    public void selectPane(String website, Pane goodsPane,Pane proxyPane,Pane persionPane) {
        switch (website) {
            case "palaces-global": {
                selectPaneInit(website, PalacesUSATaskController.class, PalacesUSATaskView.class, persionPane);//之前没考虑到，所有选择跟usa用的同一套模板
                break;
            }
            case "palaces-usa": {
                selectPaneInit(website, PalacesUSATaskController.class, PalacesUSATaskView.class,persionPane);
                break;
            }
            case "supreme-jp": {
                selectPaneInit(website, SupremeJPTaskController.class, SupremeJPTaskView.class,persionPane);
                break;
            }
        }
        goodsPane.getChildren().setAll(SpringContextUtil.getBean(GoodsView.class).getView());
        SpringContextUtil.getBean(GoodsController.class).init(website,null);
        proxyPane.getChildren().setAll(SpringContextUtil.getBean(ProxyView.class).getView());
        SpringContextUtil.getBean(ProxyController.class).init(website,null);
        this.website=website;
    }

    /***
     * 将home页面的编辑功能，转译到这里来处理
     */
    public void edit(TaskTable taskTable, Pane goodsPane,Pane proxyPane,Pane persionPane,ComboBox websiteOption){
        //****************/
        website=taskTable.getWebsite();
        websiteOption.setValue(website);
        System.out.println(website+":website");
        goodsPane.getChildren().setAll(SpringContextUtil.getBean(GoodsView.class).getView());
        SpringContextUtil.getBean(GoodsController.class).init(website,taskTable.getId());
        proxyPane.getChildren().setAll(SpringContextUtil.getBean(ProxyView.class).getView());
        SpringContextUtil.getBean(ProxyController.class).init(website,taskTable.getId());
        switch (website) {
            case "palaces-global": {
                selectPaneEditInit(taskTable, PalacesUSATaskController.class, PalacesUSATaskView.class, persionPane);//之前没考虑到，所有选择跟usa用的同一套模板
                break;
            }
            case "palaces-usa": {
                selectPaneEditInit(taskTable, PalacesUSATaskController.class, PalacesUSATaskView.class,persionPane);
                break;
            }
            case "supreme-jp": {
                selectPaneEditInit(taskTable, SupremeJPTaskController.class, SupremeJPTaskView.class,persionPane);
                break;
            }
        }
    }

    private void selectPaneInit(String website, Class<? extends  TaskParentsAPI> controller, Class<? extends AbstractFxmlView> view,Pane persionPane) {
        persionPane.getChildren().setAll(SpringContextUtil.getBean(view).getView());
        persionPane.getChildren().setAll(SpringContextUtil.getBean(view).getView());
        taskControllerAPI=SpringContextUtil.getBean(controller);
        taskControllerAPI.init(website,null);
    }
    private void selectPaneEditInit(TaskTable taskTable, Class<? extends  TaskParentsAPI> controller, Class<? extends AbstractFxmlView> view,Pane persionPane) {
        persionPane.getChildren().setAll(SpringContextUtil.getBean(view).getView());
        persionPane.getChildren().setAll(SpringContextUtil.getBean(view).getView());
        taskControllerAPI=SpringContextUtil.getBean(controller);
        taskControllerAPI.init(website,taskTable.getId());
        taskControllerAPI.editInit(taskTable,goodsController,proxyController);
    }

    void saveTask(){
        taskControllerAPI.save(this.website,goodsController,proxyController);
    }

   public void taskSave(TaskTable task){
        taskDao.taskSave(task);
        homeController.fluchTable();
        logger.info("save & fluchTable");

       Alert information = new Alert(Alert.AlertType.INFORMATION,"保存成功...");
       information.setTitle("information"); //设置标题，不设置默认标题为本地语言的information
       information.setHeaderText("信息"); //设置头标题，默认标题为本地语言的information
       information.initOwner(goodsController.keywords.getScene().getWindow());
       Button infor = new Button("show Information");
       infor.setOnAction((ActionEvent)->{
           information.showAndWait(); //显示弹窗，同时后续代码等挂起
           logger.info("confirm....");
       });
       information.show();
    }

}
