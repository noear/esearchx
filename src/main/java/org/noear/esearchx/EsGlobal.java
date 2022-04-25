package org.noear.esearchx;

import java.util.function.Consumer;

/**
 * @author noear
 * @since 1.0
 */
public class EsGlobal {
    private static Consumer<EsCommand> onCommandBefore;
    private static Consumer<EsCommand> onCommandAfter;

    public static void onCommandBefore(Consumer<EsCommand> event) {
        onCommandBefore = event;
    }

    public static void onCommandAfter(Consumer<EsCommand> event) {
        onCommandAfter = event;
    }


    public static void applyCommandBefore(EsCommand cmd) {
        if (onCommandBefore != null) {
            onCommandBefore.accept(cmd);
        }
    }

    public static void applyCommandAfter(EsCommand cmd) {
        if (onCommandAfter != null) {
            onCommandAfter.accept(cmd);
        }
    }
}
