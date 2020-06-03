package engine.palaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.HttpUntil;
import tool.RegexParse;


public class CheckOutId {

    private Logger logger = LoggerFactory.getLogger(CheckOutId.class);


    public String checkOutURL=null,authenticity_token=null;

    public   void getCheckOutUrlAndAuth(HttpUntil httpUntil, String baseUrl){
        while (true){
           try {
               httpUntil.get(baseUrl+"/checkout.json");
               checkOutURL=baseUrl+ RegexParse.baseParse(httpUntil.getResult(),"action=\"(/[\\d]*?/checkouts/[\\S]*?)\"",1);
               authenticity_token=RegexParse.baseParse(httpUntil.getResult(),"name=\"authenticity_token\"\\s*?value=\"([\\S]*?)\"",1);
               if(checkOutURL!=null&&authenticity_token!=null)
               break;
           }catch (Throwable e){
               logger.error("palace获取ID错误,重试:",e);
           }
        }
    }




}
