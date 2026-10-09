package net.blueskiez77.lord_of_the_rings__middle_earth.common;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import net.minecraft.network.chat.Component;

/**
 * LOTRDate: the Shire Reckoning, counted in days from 22 Halimath 1401, which dates mini-quests in the
 * red book. The day is kept with the world's LOTR data and sent to every player.
 *
 * <p>NOT ported yet: the day turning (LOTRDate.update, from the Middle-earth world's own clock, D10),
 * and with it the seasons; until then it stays the day it is. The /date command (D16).
 */
public final class LOTRDate {

    private LOTRDate() {
    }

    public enum Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    public static final class ShireReckoning {

        public static final Date START_DATE = new Date(1401, Month.HALIMATH, 22);
        public static int currentDay;
        private static final Map<Integer, Date> CACHED_DATES = new HashMap<>();

        private ShireReckoning() {
        }

        public static Date getShireDate() {
            return getShireDate(currentDay);
        }

        public static synchronized Date getShireDate(int day) {
            Date date = CACHED_DATES.get(day);
            if (date == null) {
                date = START_DATE.copy();
                if (day < 0) {
                    for (int i = 0; i < -day; ++i) {
                        date = date.decrement();
                    }
                } else {
                    for (int i = 0; i < day; ++i) {
                        date = date.increment();
                    }
                }
                CACHED_DATES.put(day, date);
            }
            return date;
        }

        public static boolean isLeapYear(int year) {
            return year % 4 == 0 && year % 100 != 0;
        }

        public enum Day {
            STERDAY("sterday"), SUNDAY("sunday"), MONDAY("monday"), TREWSDAY("trewsday"), HEVENSDAY("hevensday"),
            MERSDAY("mersday"), HIGHDAY("highday");

            public final String name;

            Day(String name) {
                this.name = name;
            }

            public String getDayName() {
                return Component.translatable("lotr.date.shire.day." + this.name).getString();
            }
        }

        public enum Month {
            YULE_2("yule2", 1, Season.WINTER), AFTERYULE("afteryule", 30, Season.WINTER),
            SOLMATH("solmath", 30, Season.WINTER), RETHE("rethe", 30, Season.WINTER),
            ASTRON("astron", 30, Season.SPRING), THRIMIDGE("thrimidge", 30, Season.SPRING),
            FORELITHE("forelithe", 30, Season.SPRING), LITHE_1("lithe1", 1, Season.SPRING),
            MIDYEARSDAY("midyearsday", 1, Season.SUMMER, false, false), OVERLITHE("overlithe", 1, Season.SUMMER, false, true),
            LITHE_2("lithe2", 1, Season.SUMMER), AFTERLITHE("afterlithe", 30, Season.SUMMER),
            WEDMATH("wedmath", 30, Season.SUMMER), HALIMATH("halimath", 30, Season.SUMMER),
            WINTERFILTH("winterfilth", 30, Season.AUTUMN), BLOTMATH("blotmath", 30, Season.AUTUMN),
            FOREYULE("foreyule", 30, Season.AUTUMN), YULE_1("yule1", 1, Season.AUTUMN);

            public final String name;
            public final int days;
            public final boolean hasWeekdayName;
            public final boolean isLeapYear;
            public final Season season;

            Month(String name, int days, Season season) {
                this(name, days, season, true, false);
            }

            Month(String name, int days, Season season, boolean hasWeekdayName, boolean isLeapYear) {
                this.name = name;
                this.days = days;
                this.hasWeekdayName = hasWeekdayName;
                this.isLeapYear = isLeapYear;
                this.season = season;
            }

            public String getMonthName() {
                return Component.translatable("lotr.date.shire.month." + this.name).getString();
            }

            public boolean isSingleDay() {
                return this.days == 1;
            }
        }

        public static final class Date {
            public final int year;
            public final Month month;
            public final int monthDate;
            private @Nullable Day day;

            public Date(int year, Month month, int monthDate) {
                this.year = year;
                this.month = month;
                this.monthDate = monthDate;
            }

            public Date copy() {
                return new Date(this.year, this.month, this.monthDate);
            }

            public Date decrement() {
                int newYear = this.year;
                Month newMonth = this.month;
                int newDate = this.monthDate;
                if (--newDate < 0) {
                    int monthID = newMonth.ordinal();
                    if (--monthID < 0) {
                        monthID = Month.values().length - 1;
                        --newYear;
                    }
                    newMonth = Month.values()[monthID];
                    if (newMonth.isLeapYear && !isLeapYear(newYear)) {
                        --monthID;
                        newMonth = Month.values()[monthID];
                    }
                    newDate = newMonth.days;
                }
                return new Date(newYear, newMonth, newDate);
            }

            public Date increment() {
                int newYear = this.year;
                Month newMonth = this.month;
                int newDate = this.monthDate;
                if (++newDate > newMonth.days) {
                    newDate = 1;
                    int monthID = newMonth.ordinal();
                    if (++monthID >= Month.values().length) {
                        monthID = 0;
                        ++newYear;
                    }
                    newMonth = Month.values()[monthID];
                    if (newMonth.isLeapYear && !isLeapYear(newYear)) {
                        ++monthID;
                        newMonth = Month.values()[monthID];
                    }
                }
                return new Date(newYear, newMonth, newDate);
            }

            public @Nullable Day getDay() {
                if (!this.month.hasWeekdayName) {
                    return null;
                }
                if (this.day == null) {
                    int yearDay = 0;
                    for (int i = 0; i < this.month.ordinal(); ++i) {
                        Month m = Month.values()[i];
                        if (m.hasWeekdayName) {
                            yearDay += m.days;
                        }
                    }
                    this.day = Day.values()[Math.floorMod(yearDay + this.monthDate - 1, Day.values().length)];
                }
                return this.day;
            }

            public String getDateName(boolean longName) {
                String[] dayYear = getDayAndYearNames(longName);
                return dayYear[0] + ", " + dayYear[1];
            }

            public String[] getDayAndYearNames(boolean longName) {
                StringBuilder builder = new StringBuilder();
                if (this.month.hasWeekdayName) {
                    builder.append(getDay().getDayName());
                }
                builder.append(" ");
                if (!this.month.isSingleDay()) {
                    builder.append(this.monthDate).append(" ");
                }
                builder.append(this.month.getMonthName());
                String dateName = builder.toString();
                String yearName = Component.translatable(longName ? "lotr.date.shire.long" : "lotr.date.shire").getString()
                        + " " + this.year;
                return new String[]{dateName, yearName};
            }
        }
    }
}
