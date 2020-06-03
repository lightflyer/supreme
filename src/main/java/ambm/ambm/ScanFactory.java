package ambm.ambm;


import engine.APIScan;
import engine.palaces.PalacesScan;
import engine.supreme.SupremeneScan;
import org.springframework.stereotype.Component;

@Component
public class ScanFactory {


    public APIScan getObject(String website) {
        switch (website){
            case "palaces-global":{
                return   new PalacesScan();
            }
            case "palaces-usa":{
                return   new PalacesScan();
            }
            case "palaces-jp":{
                return   new PalacesScan();
            }
            case "supreme-jp":{
                return   new SupremeneScan();
            }
            case "supreme-usa":{
                return   new SupremeneScan();
            }
        }
        return null;
    }


}
