package ambm.task.supreme_jp;

import ambm.task.TaskDao;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SupremeJPService {

    @Autowired
    private SupremeJPDao supremeJPDao;



    void countrysByWebsite(String website, ComboBox country){
        country.getItems().clear();
        supremeJPDao.countryByWebsite(website).forEach(e->{
            country.getItems().add(e);
        });
    }

    void provincesByWebsiteAndCountry(String website,String countryName,ComboBox countyState ){
        countyState.getItems().clear();
        supremeJPDao.provincesByWebsiteAndCountry(website,countryName).forEach(e->{
            if(e!=null&&!e.trim().equals(""))
                countyState.getItems().add(e);
        });
    }

    void initSelectPayMethod(ComboBox payMethod) {
        payMethod.getItems().clear();
        for(String item: Arrays.asList("Visa","American Express","Mastercard","JCB","代金引換")){
            payMethod.getItems().add(item);
        }

    }


}
