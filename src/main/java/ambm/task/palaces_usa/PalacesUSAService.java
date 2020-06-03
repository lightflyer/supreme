package ambm.task.palaces_usa;

import ambm.basic.CountryTable;
import ambm.task.TaskDao;
import ambm.task.TaskService;
import ambm.task.TaskTable;
import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PalacesUSAService {

    @Autowired
    private   PalacesUSADao palacesUSADao;



    void countrysByWebsite(String website, ComboBox country){
        country.getItems().clear();
        palacesUSADao.countryByWebsite(website).forEach(e->{
            country.getItems().add(e);
        });
    }

    void provincesByWebsiteAndCountry(String website,String countryName,ComboBox countyState ){
        countyState.getItems().clear();
        palacesUSADao.provincesByWebsiteAndCountry(website,countryName).forEach(e->{
            if(e!=null&&!e.trim().equals(""))
            countyState.getItems().add(e);
        });
    }


    CountryTable countryByWebsiteAndProvince(String website, String provincesName){
        return  palacesUSADao.countryByWebsiteAndProvince(website,provincesName);
    }


}
