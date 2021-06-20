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
    public static SpireConfig showCardTypeCountsConfig;
    public static final String ENABLE_ON_CARD_REWARDS_SETTING = "enableOnCardRewards";
    public static final String ENABLE_ON_VIEW_DECK_SETTING = "enableOnViewDeck";
    public static final String ENABLE_ON_SHOP_SETTING = "enableOnShopScreen";
    public static final String ENABLE_CURSES_SETTING = "enableCurses";
    public static final String ENABLE_ASCENDERS_BANE_SETTING = "enableAscendersBane";
    public static final String ENABLE_PERCENTAGES_SETTING = "enablePercentages";

    public static final String BADGE_IMAGE = "showcardtypecountsResources/images/Badge.png";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public ShowCardTypeCounts() {
        BaseMod.subscribe(this);
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        showCardTypeCountsSettings.setProperty(ENABLE_ON_CARD_REWARDS_SETTING, "TRUE"); // This is the default setting. It's actually set...
        showCardTypeCountsSettings.setProperty(ENABLE_ON_VIEW_DECK_SETTING, "TRUE"); // This is the default setting. It's actually set...
        showCardTypeCountsSettings.setProperty(ENABLE_ON_SHOP_SETTING, "FALSE"); // This is the default setting. It's actually set...
        showCardTypeCountsSettings.setProperty(ENABLE_CURSES_SETTING, "TRUE"); // This is the default setting. It's actually set...
        showCardTypeCountsSettings.setProperty(ENABLE_ASCENDERS_BANE_SETTING, "TRUE"); // This is the default setting. It's actually set...
        showCardTypeCountsSettings.setProperty(ENABLE_PERCENTAGES_SETTING, "TRUE"); // This is the default setting. It's actually set...
        try {
            showCardTypeCountsConfig = new SpireConfig("showCardTypeCounts", "showCardTypeCountsConfig", showCardTypeCountsSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            showCardTypeCountsConfig.load(); // Load the setting and set the boolean to equal it
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
        Texture badgeTexture = TexLoader.getTexture(BADGE_IMAGE);

        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableCardRewardButton = new ModLabeledToggleButton("Show on card reward screen.",
                350.0f, 750.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(ENABLE_ON_CARD_REWARDS_SETTING), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(ENABLE_ON_CARD_REWARDS_SETTING, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enableCardRewardButton); // Add the button to the settings panel. Button is a go.

        // Create the on/off button:
        ModLabeledToggleButton enableViewDeckButton = new ModLabeledToggleButton("Show on view deck screen.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(ENABLE_ON_VIEW_DECK_SETTING), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(ENABLE_ON_VIEW_DECK_SETTING, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enableViewDeckButton); // Add the button to the settings panel. Button is a go.

        // Create the on/off button:
        ModLabeledToggleButton enableShopButton = new ModLabeledToggleButton("Show on shop screen.",
                350.0f, 650.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(ENABLE_ON_SHOP_SETTING), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(ENABLE_ON_SHOP_SETTING, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enableShopButton); // Add the button to the settings panel. Button is a go.

        // Create the on/off button:
        ModLabeledToggleButton enableCursesButton = new ModLabeledToggleButton("Count Curse and Status cards.",
                350.0f, 600.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(ENABLE_CURSES_SETTING), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(ENABLE_CURSES_SETTING, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enableCursesButton); // Add the button to the settings panel. Button is a go.

        // Create the on/off button:
        ModLabeledToggleButton enableAscendersBaneButton = new ModLabeledToggleButton("Ignore Ascender's Bane.",
                350.0f, 550.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(ENABLE_ASCENDERS_BANE_SETTING), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(ENABLE_ASCENDERS_BANE_SETTING, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enableAscendersBaneButton); // Add the button to the settings panel. Button is a go.

        // Create the on/off button:
        ModLabeledToggleButton enablePercentagesButton = new ModLabeledToggleButton("Show percentages of card types.",
                350.0f, 500.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(ENABLE_PERCENTAGES_SETTING), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(ENABLE_PERCENTAGES_SETTING, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enablePercentagesButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, "showCardTypeCounts", "Yoda2798", "${project.description}", settingsPanel);
    }
}
