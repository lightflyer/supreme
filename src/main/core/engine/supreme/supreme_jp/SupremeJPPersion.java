package engine.supreme.supreme_jp;

import base.BasePerson;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;

public class SupremeJPPersion extends BasePerson {


   // {"credit_card":{"number":"6258 0917 2006 0818","name":"ji hongyu","start_month":null,"start_year":null,"month":8,"year":2023,"verification_value":"680","issue_number":""}}

//    @Getter @Setter public String start_month;
//    @Getter @Setter public   String start_year;
    //@Getter @Setter public  String issue_number="";
    @Getter @Setter public  String cartType="cod";
//    @Setter private  String  number="";
//    @Getter @Setter public   String  name="";
//    @Getter @Setter public   String  month="01";
//    @Getter @Setter public   String  year="2019";
//    @Getter @Setter public   String  verification_value="";

   public String getNumber(){
       if(number!=null){
           int i=-1;
           StringBuilder stringBuilder=new StringBuilder();
           for(char c:number.trim().toCharArray()){
                if((i=i+1)==4)
                {stringBuilder.append(" ");i=0;}
               stringBuilder.append(c);
           }
           this.number=stringBuilder.toString();
       }
      return  number;
   }


}
