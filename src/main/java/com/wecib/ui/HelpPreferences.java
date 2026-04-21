package com.wecib.ui;

import java.util.prefs.Preferences;

/**
 * Remembers whether the player has dismissed the first-run help overlay.
 */
public final class HelpPreferences {

    private static final String NODE = "com/wecib";
    private static final String KEY_SEEN = "battle_help_seen_v1";

    private HelpPreferences() {}

    public static boolean hasSeenHelp() {
        return Preferences.userRoot().node(NODE).getBoolean(KEY_SEEN, false);
    }

    public static void markHelpSeen() {
        Preferences.userRoot().node(NODE).putBoolean(KEY_SEEN, true);
    }
}
