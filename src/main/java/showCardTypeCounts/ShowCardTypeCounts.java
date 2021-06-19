package showCardTypeCounts;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import showCardTypeCounts.util.TexLoader;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ShowCardTypeCounts implements PostInitializeSubscriber /*implements EditStringsSubscriber*/ {

    public static final String modID = "showcardtypecounts";

    public static Properties showCardTypeCountsSettings = new Properties();
    public static final String ENABLE_ON_CARD_REWARDS_SETTING = "enableOnCardRewards";
    public static boolean enableOnCardRewards = true; // The boolean we'll be setting on/off (true/false)

    public static final String BADGE_IMAGE = "showcardtypecountsResources/images/Badge.png";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public ShowCardTypeCounts() {
        BaseMod.subscribe(this);
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        showCardTypeCountsSettings.setProperty(ENABLE_ON_CARD_REWARDS_SETTING, "TRUE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("showCardTypeCounts", "showCardTypeCountsConfig", showCardTypeCountsSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enableOnCardRewards = config.getBool(ENABLE_ON_CARD_REWARDS_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void receivePostInitialize() {
        // Load the Mod Badge
        Texture badgeTexture = TexLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("Show on card reward screen.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enableOnCardRewards, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                }, // thing??????? idk
                (button) -> { // The actual button:

                    enableOnCardRewards = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("showCardTypeCounts", "showCardTypeCountsConfig", showCardTypeCountsSettings);
                        config.setBool(ENABLE_ON_CARD_REWARDS_SETTING, enableOnCardRewards);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, "showCardTypeCounts", "Yoda2798", "Adds counts and percentages for different card types in deck.e", settingsPanel);
    }

    /*@Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/eng/Cardstrings.json");
    }*/
}
