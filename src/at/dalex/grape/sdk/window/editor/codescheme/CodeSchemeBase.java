package at.dalex.grape.sdk.window.editor.codescheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class CodeSchemeBase {

    private Pattern compiledPattern;
    private HashMap<String, String> patterns = new HashMap<>();

    /**
     * Sets the given words as keywords in the pattern.
     * @param keywords Array of keywords as String
     */
    protected void setKeywordPattern(String... keywords) {
        String keywordPattern = "\\b(" + String.join("|", keywords) + ")\\b";
        addPattern("keyword", keywordPattern);
    }

    /**
     * Adds a new pattern to the scheme.
     *
     * @param patternName The unique pattern name
     * @param pattern The pattern's value
     */
    protected void addPattern(String patternName, String pattern) {
        patterns.put(patternName, pattern);
    }

    /**
     * Compiles the final pattern using all added patterns.
     */
    protected void compilePattern() {
        StringBuilder patternBuilder = new StringBuilder();
        for (String patternName : patterns.keySet())
            patternBuilder.append("(?<" + patternName + ">" + patterns.get(patternName) + ")|");

        System.out.println("pattern; " + patternBuilder.toString());
        this.compiledPattern = Pattern.compile(patternBuilder.toString());
    }

    /**
     * @return The compiled pattern.
     */
    public Pattern getCompiledPattern() {
        return this.compiledPattern;
    }

    /**
     * @return The registered keyword sets
     */
    public Set<String> getPatternKeys() {
        return patterns.keySet();
    }
}
