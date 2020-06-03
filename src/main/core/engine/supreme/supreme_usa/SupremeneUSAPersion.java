package engine.supreme.supreme_usa;

import base.BasePerson;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;

public class SupremeneUSAPersion extends BasePerson {

    @Getter @Setter public   String start_month;
    @Getter @Setter public   String start_year;
    @Getter @Setter public  String issue_number="";


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
    public String getPhone(){
        if(phone!=null){
            String temp= phone.replaceAll("[^\\d]","").trim();
            if(temp.length()!=10)
                return  phone;
            phone=temp;
            int i=-1,j=0;
            StringBuilder stringBuilder=new StringBuilder();
            for(char c:phone.trim().toCharArray()){
                if((i=i+1)==3)
                {if(j<2){stringBuilder.append("-");}i=0;j++;}
                stringBuilder.append(c);
            }
            this.phone=stringBuilder.toString();
        }
        return  phone;
    }

    @Override
    public String getCard() {
        JSONObject child=new JSONObject(true);
        child.put("number",number);
        child.put("name",name);
        child.put("start_month",start_month);
        child.put("start_year",start_year);
        child.put("month",month);
        child.put("year",year);
        child.put("verification_value",verification_value);
        child.put("issue_number",verification_value);
        JSONObject jsonObject=new JSONObject(true);
        jsonObject.put("credit_card",child);
        return JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue) ;
    }


    public static void main(String[] args) {
        SupremeneUSAPersion supremeneUSAPersion=new SupremeneUSAPersion();
        supremeneUSAPersion.phone="2222222222";
        System.out.println(supremeneUSAPersion.getPhone());
    }


}
