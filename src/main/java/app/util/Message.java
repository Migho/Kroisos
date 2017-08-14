package app.util;

import java.text.*;
import java.util.*;

public class Message {

    private ResourceBundle messages;

    public Message(String languageTag) {
        Locale locale = languageTag != null ? new Locale(languageTag) : Locale.ENGLISH;
        this.messages = ResourceBundle.getBundle("localization/messages", locale);
    }

    public String get(String message) {
        return messages.getString(message);
    }

    public String get(final String key, final Object... args) {
        return MessageFormat.format(get(key), args);
    }

}