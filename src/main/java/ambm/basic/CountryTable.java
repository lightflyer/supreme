package ambm.basic;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CountryTable {

    @Getter @Setter public int id;
    @Getter @Setter public String website;
    @Getter @Setter public String countryName;
    @Getter @Setter public String countryCode;
    @Getter @Setter public String provincesName;
    @Getter @Setter public String provincesCode;
    @Getter @Setter public String viewName;

}


