package base;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tool.RegexParse;

@Data
public class BasePerson {

    @Getter @Setter public  String  phone;
    @Getter @Setter public  String  email;
    @Getter @Setter public  String  first_name;
    @Getter @Setter public  String  last_name;
    @Getter @Setter public  String  address1;
    @Getter @Setter public  String  address2;
    @Getter @Setter public  String  city;
    @Getter @Setter public  String  country;
    @Getter @Setter public  String  province;
    @Getter @Setter public  String  zip;
    @Getter @Setter public  String  countyState;

    //{"credit_card":{"number":"1","name":"Bogus Gateway","month":8,"year":2023,"verification_value":"068"}}
    //{"credit_card":"{\"number\":\"\",\"verification_value\":\"\",\"month\":\"\",\"year\":\"\",\"name\":\"\"}"}
    @Getter @Setter public    String  number="";
    @Getter @Setter public   String  name="";
    @Getter @Setter public   String  month="";
    @Getter @Setter public   String  year="";
    @Getter @Setter public   String  verification_value="";




    public void setCard(String  number,String  name,String  month,String  year,String  verification_value) {
        this.number=number;
        this.name=name;
        this.month=month;
        this.year=year;
        this.verification_value=verification_value;
    }

    public String getCard() {
        JSONObject child=new JSONObject(true);
        child.put("number",number);
        child.put("name",name);
        child.put("month",month);
        child.put("year",year);
        child.put("verification_value",verification_value);
        JSONObject jsonObject=new JSONObject(true);
        jsonObject.put("credit_card",child);
        return jsonObject.toString();
    }



}
