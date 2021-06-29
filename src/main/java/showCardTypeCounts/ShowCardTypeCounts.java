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
import showCardTypeCounts.util.TexLoader;

import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ShowCardTypeCounts implements PostInitializeSubscriber {

    public static final String modID = "showcardtypecounts";

    public static Properties defaultSettings = new Properties();
    public static SpireConfig showCardTypeCountsConfig;
    public static final String ENABLE_ON_CARD_REWARDS_SETTING = "enableOnCardRewards";
    public static final String ENABLE_ON_VIEW_DECK_SETTING = "enableOnViewDeck";
    public static final String ENABLE_ON_SHOP_SETTING = "enableOnShopScreen";
    public static final String ENABLE_CURSES_SETTING = "enableCurses";
    public static final String ENABLE_ASCENDERS_BANE_SETTING = "enableAscendersBane";
    public static final String ENABLE_MODDED_TYPES_SETTING = "enableModdedTypes";
    public static final String ENABLE_PERCENTAGES_SETTING = "enablePercentages";
    public static final String ENABLE_CAPITALISE_DECK_SETTING = "enableCapitaliseDeck";
    public static final String ENABLE_SHOW_ON_RIGHT_SETTING = "enableShowOnRight";

    public static final String BADGE_IMAGE = makeImagePath("Badge.png");

    private static final float xPos = 350;
    private static float yPos = 750;
    private static final float yJump = 50;

    public ShowCardTypeCounts() {
        BaseMod.subscribe(this);

        defaultSettings.setProperty(ENABLE_ON_CARD_REWARDS_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_ON_VIEW_DECK_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_ON_SHOP_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_CURSES_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_ASCENDERS_BANE_SETTING, "FALSE");
        defaultSettings.setProperty(ENABLE_MODDED_TYPES_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_PERCENTAGES_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_CAPITALISE_DECK_SETTING, "TRUE");
        defaultSettings.setProperty(ENABLE_SHOW_ON_RIGHT_SETTING, "FALSE");

        try {
            showCardTypeCountsConfig = new SpireConfig("showCardTypeCounts", "showCardTypeCountsConfig", defaultSettings);
            showCardTypeCountsConfig.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    private static void makeToggleButton(ModPanel settingsPanel, String setting, String labelText) {
        settingsPanel.addUIElement(new ModLabeledToggleButton(labelText, xPos, yPos, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                showCardTypeCountsConfig.getBool(setting), // initial value
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                },
                (button) -> { // The actual button:
                    try {
                        showCardTypeCountsConfig.setBool(setting, button.enabled);
                        showCardTypeCountsConfig.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
        yPos -= yJump;
    }

    public static void initialize() {
        ShowCardTypeCounts thismod = new ShowCardTypeCounts();
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TexLoader.getTexture(BADGE_IMAGE);

        ModPanel settingsPanel = new ModPanel();

        makeToggleButton(settingsPanel, ENABLE_ON_CARD_REWARDS_SETTING, "Show on card reward screen.");
        makeToggleButton(settingsPanel, ENABLE_ON_VIEW_DECK_SETTING, "Show on view deck screen.");
        makeToggleButton(settingsPanel, ENABLE_ON_SHOP_SETTING, "Show on shop screen.");
        makeToggleButton(settingsPanel, ENABLE_CURSES_SETTING, "Count Curse and Status cards.");
        makeToggleButton(settingsPanel, ENABLE_ASCENDERS_BANE_SETTING, "Count Ascender's Bane.");
        makeToggleButton(settingsPanel, ENABLE_MODDED_TYPES_SETTING, "Count modded card types.");
        makeToggleButton(settingsPanel, ENABLE_PERCENTAGES_SETTING, "Show percentages of card types.");
        makeToggleButton(settingsPanel, ENABLE_CAPITALISE_DECK_SETTING, "Show 'DECK' in all capitals.");
        makeToggleButton(settingsPanel, ENABLE_SHOW_ON_RIGHT_SETTING, "Show information on right-hand side of screen.");

        BaseMod.registerModBadge(badgeTexture, "showCardTypeCounts", "Yoda2798", "${project.description}", settingsPanel);
    }
}
