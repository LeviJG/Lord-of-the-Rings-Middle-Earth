package net.blueskiez77.lord_of_the_rings__middle_earth.common.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.blueskiez77.lord_of_the_rings__middle_earth.LOTRMod;

import org.jspecify.annotations.Nullable;

/**
 * Forge 1.7.10's {@code Configuration}, as far as LOTRConfig used it: a
 * {@code .cfg} of named categories holding typed properties, read with a
 * default for anything missing and written back in Forge's own layout, so a
 * {@code config/lotr.cfg} from the original mod carries straight over.
 *
 * <p>The layout: categories in name order, each under a banner of hashes, its
 * properties in name order as {@code B:"Name"=value} (B boolean, I int, D
 * double, S string), each after its comment with the default (and, for
 * numbers, the range) appended as Forge did. Anything in the file the mod did
 * not ask for -- the dimension ids in {@code dimension}, whose options come
 * with the dimensions -- is kept and written back untouched.
 */
public final class LOTRConfigFile {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String ALLOWED_NAME_CHARS = "._-";

    /** A property as the file has it: its type letter and value text, and the comment Forge would write. */
    private static final class Property {
        final char type;
        String value;
        @Nullable String comment;

        Property(char type, String value, @Nullable String comment) {
            this.type = type;
            this.value = value;
            this.comment = comment;
        }
    }

    private final Path path;
    private final Map<String, Map<String, Property>> categories = new TreeMap<>();
    private boolean changed;

    public LOTRConfigFile(Path path) {
        this.path = path;
    }

    // ------------------------------------------------------------ reading

    /** load: reads the file if there is one; a missing or unreadable file leaves every option at its default. */
    public void load() {
        this.categories.clear();
        this.changed = false;
        if (!Files.exists(this.path)) {
            this.changed = true;
            return;
        }
        try {
            List<String> lines = Files.readAllLines(this.path, StandardCharsets.UTF_8);
            List<String> stack = new ArrayList<>();
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.endsWith("{")) {
                    stack.add(unquote(line.substring(0, line.length() - 1).trim()));
                    continue;
                }
                if (line.equals("}")) {
                    if (!stack.isEmpty()) {
                        stack.removeLast();
                    }
                    continue;
                }
                int colon = line.indexOf(':');
                int equals = line.indexOf('=');
                if (stack.isEmpty() || colon != 1 || equals < 0) {
                    continue;
                }
                char type = line.charAt(0);
                String name = unquote(line.substring(2, equals).trim());
                String value = line.substring(equals + 1).trim();
                category(String.join(".", stack)).put(name, new Property(type, value, null));
            }
        } catch (IOException e) {
            LOTRMod.LOGGER.error("LOTR: could not read {}; using defaults", this.path, e);
            this.categories.clear();
        }
    }

    private Map<String, Property> category(String name) {
        return this.categories.computeIfAbsent(name, k -> new TreeMap<>());
    }

    private static String unquote(String s) {
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    /**
     * The property of this name, or a new one at the default. As Forge's
     * {@code get}, the comment is replaced by the given one (the file's own
     * comments are never read), and a value of the wrong type is reset.
     */
    private Property get(String category, String name, char type, String defaultValue, String comment) {
        Map<String, Property> props = category(category);
        Property prop = props.get(name);
        if (prop == null || prop.type != type) {
            prop = new Property(type, defaultValue, comment);
            props.put(name, prop);
            this.changed = true;
        }
        prop.comment = comment;
        return prop;
    }

    /** Configuration.get(category, key, boolean, comment).getBoolean(). */
    public boolean getBoolean(String category, String name, boolean defaultValue, @Nullable String comment) {
        Property prop = get(category, name, 'B', Boolean.toString(defaultValue),
                withDefault(comment, "[default: " + defaultValue + "]"));
        if (prop.value.equalsIgnoreCase("true")) {
            return true;
        }
        if (prop.value.equalsIgnoreCase("false")) {
            return false;
        }
        return defaultValue;
    }

    /** Configuration.get(category, key, int, comment).getInt(). */
    public int getInt(String category, String name, int defaultValue, @Nullable String comment) {
        Property prop = get(category, name, 'I', Integer.toString(defaultValue),
                withDefault(comment, "[range: " + Integer.MIN_VALUE + " ~ " + Integer.MAX_VALUE + ", default: "
                        + defaultValue + "]"));
        try {
            return Integer.parseInt(prop.value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static String withDefault(@Nullable String comment, String suffix) {
        return comment == null || comment.isEmpty() ? suffix : comment + " " + suffix;
    }

    /** Property.set: a new value, saved with the next {@link #save}. */
    public void set(String category, String name, boolean value) {
        setValue(category, name, 'B', Boolean.toString(value));
    }

    public void set(String category, String name, int value) {
        setValue(category, name, 'I', Integer.toString(value));
    }

    private void setValue(String category, String name, char type, String value) {
        Property prop = category(category).get(name);
        if (prop == null) {
            category(category).put(name, new Property(type, value, null));
        } else {
            prop.value = value;
        }
        this.changed = true;
    }

    public boolean hasChanged() {
        return this.changed;
    }

    // ------------------------------------------------------------ writing

    /** save: the whole file in Forge's layout. */
    public void save() {
        StringBuilder out = new StringBuilder();
        out.append("# Configuration file").append(NEW_LINE).append(NEW_LINE);
        for (Map.Entry<String, Map<String, Property>> cat : this.categories.entrySet()) {
            String name = cat.getKey();
            String hashes = "#".repeat(20);
            out.append(hashes).append(NEW_LINE)
                    .append("# ").append(name).append(NEW_LINE)
                    .append(hashes).append(NEW_LINE).append(NEW_LINE);
            out.append(quote(name)).append(" {").append(NEW_LINE);
            int i = 0;
            for (Map.Entry<String, Property> e : cat.getValue().entrySet()) {
                Property prop = e.getValue();
                if (prop.comment != null) {
                    if (i++ != 0) {
                        out.append(NEW_LINE);
                    }
                    for (String commentLine : prop.comment.split("\r?\n")) {
                        out.append("    # ").append(commentLine).append(NEW_LINE);
                    }
                }
                out.append("    ").append(prop.type).append(':').append(quote(e.getKey()))
                        .append('=').append(prop.value).append(NEW_LINE);
            }
            out.append("}").append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
        }
        try {
            Files.createDirectories(this.path.getParent());
            Files.writeString(this.path, out.toString(), StandardCharsets.UTF_8);
            this.changed = false;
        } catch (IOException e) {
            LOTRMod.LOGGER.error("LOTR: could not write {}", this.path, e);
        }
    }

    /** Forge quoted a name with anything but letters, digits and {@code ._-} in it. */
    private static String quote(String name) {
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && ALLOWED_NAME_CHARS.indexOf(c) < 0) {
                return "\"" + name + "\"";
            }
        }
        return name;
    }
}
