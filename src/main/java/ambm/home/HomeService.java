package ambm.home;

import ambm.ambm.ScanFactory;
import ambm.ambm.SpringContextUtil;
import ambm.ambm.TempManage;
import ambm.goods.GoodsController;
import ambm.goods.GoodsView;
import ambm.task.*;
import ambm.task.palaces_usa.PalacesUSATaskController;
import ambm.task.palaces_usa.PalacesUSATaskView;
import ambm.task.supreme_jp.SupremeJPTaskController;
import ambm.task.supreme_jp.SupremeJPTaskView;
import base.BaseWEB;
import engine.APIScan;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class HomeService {

    private Logger logger = LoggerFactory.getLogger(HomeService.class);

    @Autowired
    HomeDao homeDao;

    @Autowired
    HomeController homeController;

    @Autowired
    ScanFactory scanFactory;

    /***
     * 对任务做关闭用；
     * 里面存储所有执行的任务
     */
    HashMap<Integer,ExecutorService> threads=new HashMap<>();

    /***
     * 根据用户名密码刷新列表
     * @param username
     * @param password
     * @param taskTable
     * @param id
     * @param firstName
     * @param size
     * @param keywords
     * @param website
     * @param state
     * @param state1
     */
    void flushTable(String username, String password
            , TableView taskTable, TableColumn id, TableColumn firstName, TableColumn size, TableColumn keywords, TableColumn website, TableColumn state, TableColumn state1
    ) {
        taskTable.getItems().clear();
        id.setCellValueFactory(new PropertyValueFactory("id"));
        firstName.setCellValueFactory(new PropertyValueFactory("first_name"));
        size.setCellValueFactory(new PropertyValueFactory("size"));
        keywords.setCellValueFactory(new PropertyValueFactory("keywords"));
        website.setCellValueFactory(new PropertyValueFactory("website"));
        state.setCellValueFactory(new PropertyValueFactory("state"));
        state.setStyle("-fx-text-fill:#4DC077;");
        state1.setCellValueFactory(new PropertyValueFactory("state1"));
        taskTable.getItems().addAll(homeDao.tasksByUsernameAndPassword(username, password));
        TableColumn<TaskTable, Void> colBtn = new TableColumn("操作");
        Callback<TableColumn<TaskTable, Void>, TableCell<TaskTable, Void>> cellFactory = new Callback<TableColumn<TaskTable, Void>, TableCell<TaskTable, Void>>() {
            @Override
            public TableCell<TaskTable, Void> call(final TableColumn<TaskTable, Void> param) {
                final TableCell<TaskTable, Void> cell = new TableCell<TaskTable, Void>() {
                    HBox paddedButton = new HBox();
                    Hyperlink startTask = new Hyperlink("启动");
                    Hyperlink stopTask = new Hyperlink("停止");
                    Hyperlink editTask = new Hyperlink("编辑");
                    Hyperlink delTask = new Hyperlink("删除");

                    {
                        startTask.setGraphic(new ImageView(new Image("/static/icon/startup.png")));
                        stopTask.setGraphic(new ImageView(new Image("/static/icon/stop-b.png")));
                        editTask.setGraphic(new ImageView(new Image("/static/icon/editing.png")));
                        delTask.setGraphic(new ImageView(new Image("/static/icon/del.png")));

                        paddedButton.getChildren().addAll(startTask,stopTask, editTask, delTask);
                        paddedButton.setFillHeight(true);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            startTask.setOnMouseClicked((i) -> {
                                startTask(getTableView().getItems().get(getIndex()).getId(), getIndex());
                            });
                            stopTask.setOnMouseClicked((i) -> {
                                stopTask(getTableView().getItems().get(getIndex()).getId(), getIndex());
                            });
                            editTask.setOnMouseClicked((i) -> {
                                edit(getTableView().getItems().get(getIndex()).getId());
                            });
                            delTask.setOnMouseClicked((i) -> {
                                remove(getTableView().getItems().get(getIndex()).getId());
                            });
                            paddedButton.setPadding(new Insets(3));
                            setGraphic(paddedButton);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        taskTable.getColumns().set(6, colBtn);
        taskTable.refresh();
    }


    /****
     * 刷新状态
     * 列表的状态列，刷新；
     */
    public void flushTableState(TableView taskTable) {
        ExecutorService executors = Executors.newSingleThreadExecutor();
        executors.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    TableMessQueue.Mess mess = TableMessQueue.getInstance().flushMess();
                    if (mess != null) {
                        Platform.runLater(new Runnable() {//更新ui 要这么写；不能在ui线程之外更新ui
                            @Override
                            public void run() {
                                ((TaskTable) taskTable.getItems().get(mess.rowIdOfUI)).setState(mess.state);
                                taskTable.refresh();
                            }
                        });
                    }
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        logger.error("刷新table错误", e);
                    }
                }
            }
        });
        executors.shutdown();
    }

    /****
     * 批量启动...
     * @param taskTable
     */
    public void startAllTasks(TableView taskTable){
        ObservableList <TaskTable>  taskTables=taskTable.getItems();
        for(int i=0;i<taskTable.getItems().size();i++){
            startTask(taskTables.get(i).getId(),i);
        }
    }



    /****
     * 启动任务;
     * @param id
     * @param index
     */
    private volatile Map<Integer, Boolean> startLock = new HashMap<>();

    private void startTask(int id, int index) {
        if (startLock.get(id) != null)
            return;
        startLock.put(id, true);
        /*****
         * 启动
         */
        ExecutorService pool= Executors.newSingleThreadExecutor();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("启动"+index+">>"+id);
                TableMessQueue.getInstance().addMess(index,"启动....");
                TaskTable taskTable=homeDao.taskById(TempManage.temp.get("username"),TempManage.temp.get("password"),id);
                BaseWebTable baseWebTable=homeDao.baseWebByWebsite(taskTable.getWebsite());
                APIScan apiScan=scanFactory.getObject(taskTable.getWebsite());//这个地方也是写死的；要考虑；
                apiScan.scan(taskTable,new BaseWEB(baseWebTable).setIndex(index));
            }
        });
        threads.put(id,pool);
        pool.shutdown();
    }

    private void stopTask(int id,int index){
        if(threads.get(id)!=null)
        {
            threads.get(id).shutdownNow();
            startLock.remove(id);
            TableMessQueue.getInstance().addMess(index,"任务被强行终止...."+id);
        }
    }

    /***
     * 批量停止任务
     */
    public void stopAllTask(){
        for(Integer key:threads.keySet())
        {
            threads.get(key).shutdownNow();
            TableMessQueue.getInstance().addMess(key,"任务被强行终止....");
        }
    }


    @Autowired
    TaskController taskController;
    //设计此的目的是，每次点击其他的编辑的时候，把上一个页面关掉；
    private Stage stageTask=null;
    private Scene scene=null;
    /**
     * 编辑任务
     * @param id
     */
    private void edit(int id) {
        /**********第一步显示面板***********/
        TaskTable taskTable=homeDao.taskById(TempManage.temp.get("username"),TempManage.temp.get("password"),id);
        if( stageTask!=null)
            stageTask.close();
        if(stageTask==null)
        stageTask = new Stage();
        if(scene==null)
             scene = new Scene(SpringContextUtil.getBean(TaskView.class).getView());
        stageTask.setScene(scene);
        stageTask.show();
                /**********第二步显示对应的面板***********/
        taskController.editTask(taskTable);
    }

    /****
     * 新建任务
     */
    public void  addTask(){
        if( stageTask!=null)
            stageTask.close();
        if(stageTask==null)
            stageTask = new Stage();
        if(scene==null)
            scene = new Scene(SpringContextUtil.getBean(TaskView.class).getView());
        stageTask.setScene(scene);
        stageTask.show();
    }


    /***
     *删除任务
     * @param id
     */
    private void remove(int id) {
        homeDao.deleteTaskByUsernameAndPasswordAndId(TempManage.temp.get("username"),TempManage.temp.get("password"),id);
        homeController.fluchTable();

        Alert information = new Alert(Alert.AlertType.INFORMATION,"删除成功...");
        information.setTitle("information"); //设置标题，不设置默认标题为本地语言的information
        information.setHeaderText("信息"); //设置头标题，默认标题为本地语言的information
        Button infor = new Button("show Information");
        infor.setOnAction((ActionEvent)->{
            information.showAndWait(); //显示弹窗，同时后续代码等挂起
            logger.info("confirm....");
        });
        information.show();
    }





}
