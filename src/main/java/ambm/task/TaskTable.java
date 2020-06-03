package ambm.task;

import javafx.concurrent.Task;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Data
public class TaskTable {
    public TaskTable() {
    }

    @Setter public @Getter int id;
    public TaskTable(String keywords, String size, String backup_size, boolean fuzzy_match,
                     boolean new_goods, String username, String password, String first_name, String lash_name,
                     String email, String address, String apartment, String city, String country, String postcode,
                     String phone, String pay_method,String card_number, String card_holder_name, String mmyy, String cvv,
                     String captcha, boolean start_captcha, boolean proxy, String ip, String port, String proxy_username,
                     String proxy_password, String website, String state, String county_state,String province) {
        this.keywords = keywords;
        this.size = size;
        this.backup_size = backup_size;
        this.fuzzy_match = fuzzy_match;
        this.new_goods = new_goods;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.lash_name = lash_name;
        this.email = email;
        this.address = address;
        this.apartment = apartment;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
        this.phone = phone;
        this.pay_method=pay_method;
        this.card_number = card_number;
        this.card_holder_name = card_holder_name;
        this.mmyy = mmyy;
        this.cvv = cvv;
        this.captcha = captcha;
        this.start_captcha = start_captcha;
        this.proxy = proxy;
        this.ip = ip;
        this.port = port;
        this.proxy_username = proxy_username;
        this.proxy_password = proxy_password;
        this.website = website;
        this.state = state;
        this.county_state = county_state;
        this.province=province;
    }

     @Setter public @Getter String index;
     @Setter public @Getter String keywords;
     @Setter public @Getter String size;
     @Setter public @Getter String backup_size;
     @Setter public @Getter boolean fuzzy_match;
     @Setter public @Getter boolean new_goods;
     @Setter public @Getter String username;
     @Setter public @Getter String password;
     @Setter public @Getter String first_name;
     @Setter public @Getter String lash_name;
     @Setter public @Getter String email;
     @Setter public @Getter String address;
     @Setter public @Getter String apartment;
     @Setter public @Getter String city;
     @Setter public @Getter String country;
     @Setter public @Getter String postcode;
     @Setter public @Getter String phone;
     @Setter public @Getter String pay_method;
     @Setter public @Getter String card_number;
     @Setter public @Getter String card_holder_name;
     @Setter public @Getter String mmyy;
     @Setter public @Getter String cvv;
     @Setter public @Getter String captcha;
     @Setter public @Getter boolean start_captcha;
     @Setter public @Getter boolean proxy;
     @Setter public @Getter String ip;
     @Setter public @Getter String port;
     @Setter public @Getter String proxy_username;
     @Setter public @Getter String proxy_password;
     @Setter public @Getter String website;
     @Setter public @Getter String state;
     @Setter public @Getter String county_state;
    @Setter public @Getter String  province;


}


