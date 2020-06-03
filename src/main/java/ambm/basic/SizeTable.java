package ambm.basic;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SizeTable {

    @Getter @Setter public int id;
    @Getter @Setter public String website;
    @Getter @Setter public String size;
    @Getter @Setter public String sizeCode;

}


