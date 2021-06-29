package showCardTypeCounts.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CtBehavior;
import lobotomyMod.patch.AbstractCardEnum;
import showCardTypeCounts.ShowCardTypeCounts;
import theSlumbering.patches.customTags;

@SpirePatch2(
        clz = CardRewardScreen.class,
        method = "render"
)
public class showCardTypeCountsPatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Top Panel Tips");

    private static final UIStrings lobotomyStrings = CardCrawlGame.languagePack.getUIString("CardType");
    private static final UIStrings slumberingStrings = CardCrawlGame.languagePack.getUIString("theSlumbering:RenderType");

    private static final boolean hasLobotomy = Loader.isModLoaded("Lobotomy");
    private static final boolean hasSlumbering = Loader.isModLoaded("slumberingmod");

    public static void countCardTypes(SpriteBatch sb) {
        // 0 attack, 1 skill, 2 power, 3 curse, 4 status, 5 poker, 6 abnormality (lobotomy), 7 passive (slumbering), 8 other
        int[] cardCounts = new int[9];
        String outString = "";
        int deckSize = 0;

        boolean countCurses = ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_CURSES_SETTING);
        boolean countAscendersBane = ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_ASCENDERS_BANE_SETTING);
        boolean showPercentages = ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_PERCENTAGES_SETTING);
        boolean capitaliseDeck = ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_CAPITALISE_DECK_SETTING);
        boolean showOnRight = ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_SHOW_ON_RIGHT_SETTING);


        // count cards of each type in deck
        for (AbstractCard c: AbstractDungeon.player.masterDeck.group) {
            // handle modded types which don't actually change type first
            if (hasLobotomy && c.color == AbstractCardEnum.Lobotomy) {
                cardCounts[6]++;
                deckSize++;
                continue;
            } else if (hasSlumbering && c.hasTag(customTags.Passive)) {
                cardCounts[7]++;
                deckSize++;
                continue;
            }
            switch (c.type) {
                case ATTACK:
                    cardCounts[0]++;
                    deckSize++;
                    break;
                case SKILL:
                    cardCounts[1]++;
                    deckSize++;
                    break;
                case POWER:
                    cardCounts[2]++;
                    deckSize++;
                    break;
                case CURSE:
                    if (countCurses && !(c.cardID.equals("AscendersBane") && !countAscendersBane)) {
                        cardCounts[3]++;
                        deckSize++;
                    }
                    break;
                case STATUS:
                    if (countCurses) {
                        cardCounts[4]++;
                        deckSize++;
                    }
                    break;
                default:
                    //System.out.println(c.type.name());
                    cardCounts[8]++;
                    deckSize++;
                    break;
            }
        }

        // format output for each card type
        for (int i = 0; i < cardCounts.length; i++) {
            if (cardCounts[i] > 0) {
                String cardType;
                switch (i) {
                    // attack, skill, power, curse, status
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if (i == 4) {i = 7;} // status in different spot
                        cardType = uiStrings.TEXT[i];
                        break;
                    // poker
                    case 5:
                        cardType = "Poker";
                        break;
                    // abnormality (lobotomy)
                    case 6:
                        cardType = lobotomyStrings.TEXT[0];
                        break;
                    // passive (slumbering)
                    case 7:
                        cardType = slumberingStrings.TEXT[0];
                        break;
                    // other
                    case 8:
                    default:
                        cardType = "Other";
                        break;
                }
                int percentageNum = Math.round( (float) cardCounts[i] * 100 / deckSize);
                String percentage = showPercentages ? String.format(" (%d%%)", percentageNum) : "";
                outString = outString.concat(String.format("%1$s: %2$d%3$s\n", cardType, cardCounts[i], percentage));
            }
        }

        // add preceding 'Deck' if non-empty output
        if (outString.length() > 0) {
            String deckName = capitaliseDeck ? tutorialStrings.LABEL[1].toUpperCase().concat("\n") : tutorialStrings.LABEL[1].concat("\n");
            outString = deckName.concat(outString);
        }

        // render output, on correct side of screen
        if (showOnRight) {
            FontHelper.renderFontRightAligned(sb, FontHelper.panelNameFont, outString, Settings.WIDTH - 16f * Settings.scale, Settings.HEIGHT / 2.0F, Color.WHITE.cpy());
        } else {
            FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, outString, 16f * Settings.scale, Settings.HEIGHT / 2.0F, Color.WHITE.cpy());
        }
    }

    @SpirePatch2(
            clz = CardRewardScreen.class,
            method = "render"
    )
    public static class cardRewardScreenPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"sb"}
        )
        public static void Insert(SpriteBatch sb) {
            if (ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_ON_CARD_REWARDS_SETTING)) {
                countCardTypes(sb);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CardRewardScreen.class,"renderTwitchVotes");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(
            clz = MasterDeckViewScreen.class,
            method = "render"
    )
    public static class deckViewScreenPatch {
        @SpirePostfixPatch
        public static void Postfix(SpriteBatch sb) {
            if (ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_ON_VIEW_DECK_SETTING)) {
                countCardTypes(sb);
            }
        }
    }

    @SpirePatch2(
            clz = ShopScreen.class,
            method = "render"
    )
    public static class shopScreenPatch {
        @SpirePostfixPatch
        public static void Postfix(SpriteBatch sb) {
            if (ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_ON_SHOP_SETTING)) {
                countCardTypes(sb);
            }
        }
    }
}
