package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRModTextFiles;
import net.blueskiez77.lord_of_the_rings__middle_earth.common.entity.npc.LOTRNames;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * LOTRLore: the lore books, read from {@code assets/lotr/lore}. Each file is
 * a book: {@code #title:}, {@code #author:}, {@code #types:} (its categories,
 * comma-separated, or {@code all}) and {@code #reward} (a mini-quest may give
 * it) above the text; other {@code #} lines are notes. Made into a written
 * book, its {@code {num:a,b}}, {@code {name:bank}} and {@code {choose:a/b/c}}
 * are filled in at random and its text is laid out into pages.
 */
public final class LOTRLore {

    private static final String NEWLINE = "\n";
    private static final String CODE_METADATA = "#";
    private static final String CODE_TITLE = "title:";
    private static final String CODE_AUTHOR = "author:";
    private static final String CODE_CATEGORY = "types:";
    private static final String CODE_CATEGORY_SEPARATOR = ",";
    private static final String CODE_REWARD = "reward";

    public final String loreName;
    public final String loreTitle;
    public final String loreAuthor;
    public final String loreText;
    public final List<LoreCategory> loreCategories;
    public final boolean isRewardable;

    private LOTRLore(String name, String title, String author, String text, List<LoreCategory> categories, boolean reward) {
        this.loreName = name;
        this.loreTitle = title;
        this.loreAuthor = author;
        this.loreText = text;
        this.loreCategories = categories;
        this.isRewardable = reward;
    }

    /** getMultiRandomLore: one book from any of these categories, each book counted once. */
    public static @Nullable LOTRLore getMultiRandomLore(Iterable<LoreCategory> categories, RandomSource random,
                                                        boolean rewardsOnly) {
        List<LOTRLore> allLore = new ArrayList<>();
        for (LoreCategory c : categories) {
            for (LOTRLore lore : c.loreList) {
                if (allLore.contains(lore) || rewardsOnly && !lore.isRewardable) {
                    continue;
                }
                allLore.add(lore);
            }
        }
        return allLore.isEmpty() ? null : allLore.get(random.nextInt(allLore.size()));
    }

    /** loadAllLore. Called once from mod init, after the name banks. */
    public static void loadAllLore() {
        int count = 0;
        for (Map.Entry<String, List<String>> entry : LOTRModTextFiles.readAll("assets/lotr/lore", ".txt").entrySet()) {
            String title = "";
            String author = "";
            List<LoreCategory> categories = new ArrayList<>();
            StringBuilder text = new StringBuilder();
            boolean reward = false;
            for (String line : entry.getValue()) {
                if (line.startsWith(CODE_METADATA)) {
                    String metadata = line.substring(CODE_METADATA.length());
                    if (metadata.startsWith(CODE_TITLE)) {
                        title = metadata.substring(CODE_TITLE.length());
                    } else if (metadata.startsWith(CODE_AUTHOR)) {
                        author = metadata.substring(CODE_AUTHOR.length());
                    } else if (metadata.startsWith(CODE_CATEGORY)) {
                        for (String categoryName : metadata.substring(CODE_CATEGORY.length()).split(CODE_CATEGORY_SEPARATOR)) {
                            if (LoreCategory.ALL_CODE.equals(categoryName)) {
                                for (LoreCategory category : LoreCategory.values()) {
                                    if (!categories.contains(category)) {
                                        categories.add(category);
                                    }
                                }
                                continue;
                            }
                            LoreCategory category = LoreCategory.forName(categoryName);
                            if (category != null && !categories.contains(category)) {
                                categories.add(category);
                            }
                        }
                    } else if (metadata.startsWith(CODE_REWARD)) {
                        reward = true;
                    }
                    continue;
                }
                text.append(line).append(NEWLINE);
            }
            LOTRLore lore = new LOTRLore(entry.getKey(), title, author, text.toString(), categories, reward);
            for (LoreCategory category : categories) {
                category.loreList.add(lore);
            }
            ++count;
        }
        LOTRMod.LOGGER.info("LOTR: loaded {} lore books", count);
    }

    /**
     * organisePages: the text laid out a page at a time -- at most 256
     * characters and 13 lines, a line ending once it reaches 17 characters,
     * with the text's own line breaks kept.
     */
    public static List<String> organisePages(String loreText) {
        List<String> loreTextPages = new ArrayList<>();
        String remainingText = loreText;
        List<String> splitTxtWords = new ArrayList<>();
        while (!remainingText.isEmpty()) {
            String part;
            if (remainingText.startsWith(NEWLINE)) {
                part = NEWLINE;
                if (!splitTxtWords.isEmpty()) {
                    splitTxtWords.add(part);
                }
                remainingText = remainingText.substring(part.length());
                continue;
            }
            int indexOf = remainingText.indexOf(NEWLINE);
            part = indexOf >= 0 ? remainingText.substring(0, indexOf) : remainingText;
            Collections.addAll(splitTxtWords, StringUtils.split(part, " "));
            remainingText = remainingText.substring(part.length());
        }
        while (!splitTxtWords.isEmpty()) {
            StringBuilder pageText = new StringBuilder();
            int numLines = 0;
            StringBuilder currentLine = new StringBuilder();
            int usedWords = 0;
            for (int i = 0; i < splitTxtWords.size(); ++i) {
                String word = splitTxtWords.get(i);
                if (pageText.length() + word.length() > 256) {
                    break;
                }
                if (NEWLINE.equals(word)) {
                    if (!currentLine.isEmpty()) {
                        pageText.append(currentLine);
                        currentLine = new StringBuilder();
                        if (++numLines >= 13) {
                            break;
                        }
                    }
                    ++usedWords;
                    if (pageText.isEmpty()) {
                        continue;
                    }
                    pageText.append(word);
                    if (++numLines < 13) {
                        continue;
                    }
                    break;
                }
                currentLine.append(word);
                ++usedWords;
                if (i < splitTxtWords.size() - 1) {
                    currentLine.append(" ");
                }
                if (currentLine.length() < 17) {
                    continue;
                }
                pageText.append(currentLine);
                currentLine = new StringBuilder();
                if (++numLines >= 13) {
                    break;
                }
            }
            if (!currentLine.isEmpty()) {
                pageText.append(currentLine);
            }
            splitTxtWords.subList(0, usedWords).clear();
            loreTextPages.add(pageText.toString());
        }
        return loreTextPages;
    }

    /** createLoreBook: a written book of this lore, its blanks filled in. */
    public ItemStack createLoreBook(RandomSource random) {
        String title = formatRandom(this.loreTitle, random);
        String author = formatRandom(this.loreAuthor, random);
        List<Filterable<Component>> pages = new ArrayList<>();
        for (String page : organisePages(formatRandom(this.loreText, random))) {
            pages.add(Filterable.passThrough(Component.literal(page)));
        }
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.set(DataComponents.WRITTEN_BOOK_CONTENT,
                new WrittenBookContent(Filterable.passThrough(title), author, 0, pages, true));
        return book;
    }

    /** formatRandom: each {num:a,b}, {name:bank} and {choose:a/b/c}, in turn, filled in. */
    public static String formatRandom(String text, RandomSource random) {
        int lastIndexStart = -1;
        while (true) {
            int indexStart = text.indexOf('{', lastIndexStart + 1);
            int indexEnd = text.indexOf('}');
            lastIndexStart = indexStart;
            if (indexStart < 0 || indexEnd <= indexStart) {
                return text;
            }
            String unformatted = text.substring(indexStart, indexEnd + 1);
            String formatted = unformatted.substring(1, unformatted.length() - 1);
            try {
                if (formatted.startsWith("num:")) {
                    String range = formatted.substring("num:".length());
                    int comma = range.indexOf(CODE_CATEGORY_SEPARATOR);
                    int min = Integer.parseInt(range.substring(0, comma));
                    int max = Integer.parseInt(range.substring(comma + CODE_CATEGORY_SEPARATOR.length()));
                    formatted = String.valueOf(Mth.randomBetweenInclusive(random, min, max));
                } else if (formatted.startsWith("name:")) {
                    String nameBank = formatted.substring("name:".length());
                    if (LOTRNames.nameBankExists(nameBank)) {
                        formatted = LOTRNames.getRandomName(nameBank, random);
                    }
                } else if (formatted.startsWith("choose:")) {
                    String[] words = formatted.substring("choose:".length()).split("/", -1);
                    formatted = words[random.nextInt(words.length)];
                }
            } catch (RuntimeException e) {
                LOTRMod.LOGGER.error("LOTR: could not fill in {} in a lore book", unformatted, e);
            }
            text = Pattern.compile(unformatted, Pattern.LITERAL).matcher(text).replaceFirst(Matcher.quoteReplacement(formatted));
        }
    }

    public enum LoreCategory {
        RUINS("ruins"), SHIRE("shire"), BREE("bree"), BLUE_MOUNTAINS("blue_mountains"), LINDON("lindon"),
        ERIADOR("eriador"), RIVENDELL("rivendell"), EREGION("eregion"), DUNLAND("dunland"), GUNDABAD("gundabad"),
        ANGMAR("angmar"), WOODLAND_REALM("woodland_realm"), DOL_GULDUR("dol_guldur"), DALE("dale"),
        DURIN("durins_folk"), LOTHLORIEN("lothlorien"), ROHAN("rohan"), ISENGARD("isengard"), GONDOR("gondor"),
        MORDOR("mordor"), DORWINION("dorwinion"), RHUN("rhun"), HARNENNOR("harnennor"), SOUTHRON("southron"),
        UMBAR("umbar"), NOMAD("nomad"), GULF("gulf"), FAR_HARAD("far_harad"), FAR_HARAD_JUNGLE("far_harad_jungle"),
        HALF_TROLL("half_troll");

        public static final String ALL_CODE = "all";
        public final String categoryName;
        final Collection<LOTRLore> loreList = new ArrayList<>();

        LoreCategory(String name) {
            this.categoryName = name;
        }

        public static @Nullable LoreCategory forName(String name) {
            for (LoreCategory category : values()) {
                if (name.equalsIgnoreCase(category.categoryName)) {
                    return category;
                }
            }
            return null;
        }
    }
}
