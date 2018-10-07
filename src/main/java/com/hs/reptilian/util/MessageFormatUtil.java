package com.hs.reptilian.util;

import java.text.MessageFormat;

public class MessageFormatUtil {

    public static String format(String pattern, Object ... params) {
        MessageFormat messageFormat = new MessageFormat(pattern);
        return messageFormat.format(params);
    }

}
