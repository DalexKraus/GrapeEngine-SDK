package at.dalex.grape.sdk.window.editor;

import at.dalex.grape.sdk.window.editor.codescheme.CodeSchemeBase;
import at.dalex.grape.sdk.window.editor.codescheme.JavaScheme;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;

public class CodeEditorManager {

    private static final String sampleCode = String.join("\n", new String[] {
            "package com.example;",
            "",
            "import java.util.*;",
            "",
            "public class Foo extends Bar implements Baz {",
            "",
            "    /*",
            "     * multi-line comment",
            "     */",
            "    public static void main(String[] args) {",
            "        // single-line comment",
            "        for(String arg: args) {",
            "            if(arg.length() != 0)",
            "                System.out.println(arg);",
            "            else",
            "                System.err.println(\"Warning: empty string as argument\");",
            "        }",
            "    }",
            "",
            "}"
    });

    private static CodeArea codeArea;
    private static CodeSchemeBase currentScheme;

    /**
     * @return The {@link CodeArea} as singleton
     */
    public static CodeArea getCodeArea() {
        if (codeArea == null) createCodeArea();
        return codeArea;
    }

    /**
     * Creates the {@link CodeArea}
     */
    private static void createCodeArea() {
        codeArea = new CodeArea();
        
        //Set the default code scheme
        currentScheme = new JavaScheme();

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        Subscription cleanupWhenNoLongerNeedIt = codeArea
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));

        codeArea.replaceText(0, 0, sampleCode);
    }

    /**
     * Computes the highlighting for the given text using the current scheme.
     * @param text
     * @return
     */
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        int lastKeywordEnd = 0;
        Matcher matcher = currentScheme.getCompiledPattern().matcher(text);
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while(matcher.find()) {
            String styleClass = null;
            for (String patternName : currentScheme.getPatternKeys()) {
                styleClass = matcher.group(patternName) != null ? patternName : null;
                if (styleClass != null) break;
            }

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKeywordEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKeywordEnd);
        return spansBuilder.create();
    }
}
