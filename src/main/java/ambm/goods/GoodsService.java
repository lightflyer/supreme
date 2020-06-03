package ambm.goods;

import javafx.scene.control.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    void sizesByWebsite(String website, ComboBox size,ComboBox backupSize){
        size.getItems().clear();
        backupSize.getItems().clear();
        goodsDao.sizesByWebsite(website).forEach(e->{
            size.getItems().add(e.getSize());
            backupSize.getItems().add(e.getSize());
        });
    }
}
