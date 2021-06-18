package showCardTypeCounts;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.localization.UIStrings;
import java.nio.charset.StandardCharsets;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ShowCardTypeCounts /*implements EditStringsSubscriber*/ {

    public static final String modID = "showcardtypecounts";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public ShowCardTypeCounts() {
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static void initialize() {
        ShowCardTypeCounts thismod = new ShowCardTypeCounts();
    }

    /*@Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/eng/Cardstrings.json");
    }*/
}
