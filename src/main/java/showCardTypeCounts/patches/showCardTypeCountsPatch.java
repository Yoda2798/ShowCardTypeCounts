package showCardTypeCounts.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import javassist.CtBehavior;
import showCardTypeCounts.ShowCardTypeCounts;

@SpirePatch2(
        clz = CardRewardScreen.class,
        method = "render"
)
public class showCardTypeCountsPatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SingleCardViewPopup");
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Top Panel Tips");

    public static void countCardTypes(SpriteBatch sb) {
        int[] cardCounts = new int[5];
        String outString = "";
        //int totalCards = 0;
        boolean countCurses = ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_CURSES_SETTING);

        for (AbstractCard c: AbstractDungeon.player.masterDeck.group) {
            switch (c.type) {
                case ATTACK:
                    cardCounts[0]++;
                    break;
                case SKILL:
                    cardCounts[1]++;
                    break;
                case POWER:
                    cardCounts[2]++;
                    break;
                case CURSE:
                    // TODO: add check for ignoring Ascender's Bane here
                    if (countCurses) {
                        cardCounts[3]++;
                    }
                    break;
                case STATUS:
                    if (countCurses) {
                        cardCounts[4]++;
                    }
                    break;
                default:
                    // ignore card if somehow not one of above
                    break;
            }
        }


        for (int i = 0; i < cardCounts.length; i++) {
            if (cardCounts[i] > 0) {
                //String newLine = outString.length() > 0 ? "\n" : "";
                String cardType = i == 4 ? uiStrings.TEXT[7] : uiStrings.TEXT[i];
                int percentage = Math.round(cardCounts[i] * 100 / AbstractDungeon.player.masterDeck.group.size());
                //outString += uiStrings.TEXT[i] + cardCounts[i];
                outString += String.format("%1$s: %2$d (%3$d%%)\n", cardType, cardCounts[i], percentage);
            }
        }

        if (outString.length() > 0) {
            String deckName = tutorialStrings.LABEL[1]+"\n";
            outString = deckName+outString;
        }
        FontHelper.renderFontLeft(sb, FontHelper.panelNameFont, outString, 0f, Settings.HEIGHT / 2.0F, Color.WHITE.cpy());
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
            if (ShowCardTypeCounts.showCardTypeCountsConfig.getBool(ShowCardTypeCounts.ENABLE_ON_CARD_REWARDS_SETTING)) {
                countCardTypes(sb);
            }
        }
    }
}
