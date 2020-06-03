package ambm.task;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BaseWebTable {

    public BaseWebTable() {
    }

    @Getter @Setter public int id;
    @Getter @Setter public String baseUrl;
    @Getter @Setter public String cartUrl;
    @Getter @Setter public String addCartUrl;
    @Getter @Setter public String website;
    @Getter @Setter public String scanUrl;
    @Getter @Setter public String payUrl;
    @Getter @Setter public String captchaKey;
    @Getter @Setter public String capthchaHost;



}


