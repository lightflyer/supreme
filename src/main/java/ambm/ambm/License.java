package ambm.ambm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import com.teamdev.jxbrowser.chromium.ba;

public class License {

    public  static void   init(){
        try {
            Field e = ba.class.getDeclaredField("e");
            e.setAccessible(true);
            Field f = ba.class.getDeclaredField("f");
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



}
