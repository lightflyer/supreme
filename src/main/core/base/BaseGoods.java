package base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public  class BaseGoods {

    @Getter @Setter public  String id;
    @Getter @Setter public  String quantity;
    @Getter @Setter public  String keywords;
    @Getter @Setter public  String newGoods;

}
