package ambm.task;

import ambm.goods.GoodsController;

public interface TaskParentsAPI {

     void  init(String website,Integer taskId);

     void editInit(TaskTable taskTable, GoodsController goodsController, ProxyController proxyController);

     void save(String website, GoodsController goodsController, ProxyController proxyController);


}
