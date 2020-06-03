package ambm.style;

import de.felixroske.jfxsupport.SplashScreen;

public class CustomSplash extends SplashScreen {

    @Override
    public String getImagePath() {
        //return super.getImagePath();
        //return "/Group1x.png";
        return "/ambm.png";
    }

    /**
     * Customize if the splash screen should be visible at all
     *
     * @return true by default
     */
    @Override
    public boolean visible() {
        return super.visible();
    }
}
