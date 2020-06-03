package engine.palaces.usa;

import base.BasePerson;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;

public class PalacesUSAPersion extends BasePerson {
    @Getter
    @Setter
    public  String start_month;
    @Getter @Setter public  String start_year;
    @Getter @Setter public  String issue_number="";


    public String getGlobaleCard() {
        JSONObject child=new JSONObject(true);
        child.put("number",getNumber(number));
        child.put("name",name);
        child.put("start_month",start_month);
        child.put("start_year",start_year);
        child.put("month",Integer.parseInt(month));
        child.put("year",Integer.parseInt(year));
        child.put("verification_value",verification_value);
        child.put("issue_number",verification_value);
        JSONObject jsonObject=new JSONObject(true);
        jsonObject.put("credit_card",child);
        return JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue) ;
    }

    public String getUSACard() {
        JSONObject child=new JSONObject(true);
        child.put("number",getNumber(number));
        child.put("name",name);
        child.put("month",Integer.parseInt(month));
        child.put("year",Integer.parseInt(year));
        child.put("verification_value",verification_value);
        JSONObject jsonObject=new JSONObject(true);
        jsonObject.put("credit_card",child);
        return JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue) ;
    }

    public String getNumber(String number){
        if(number!=null){
            int i=-1;
            StringBuilder stringBuilder=new StringBuilder();
            for(char c:number.trim().toCharArray()){
                if((i=i+1)==4)
                {stringBuilder.append(" ");i=0;}
                stringBuilder.append(c);
            }
            number=stringBuilder.toString();
        }
        return  number;
    }


    public static void main(String[] args) {
        PalacesUSAPersion palacesPersion= new PalacesUSAPersion();
        palacesPersion.number="4221232323423424";
        palacesPersion.month="9";
        palacesPersion.year="2023";
        palacesPersion.verification_value="536";
        palacesPersion.name="zhang san";
        System.out.println(palacesPersion.getGlobaleCard());
        System.out.println(palacesPersion.getUSACard());
        //System.out.println(new PalacesPersion().getCard());
    }
}
