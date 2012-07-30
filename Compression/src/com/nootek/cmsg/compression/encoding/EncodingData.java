package com.nootek.cmsg.compression.encoding;

import org.jetbrains.annotations.NotNull;

public class EncodingData {
    private final String string;
    private final EncodingsEnum stringEncoding;

    public EncodingData(@NotNull final String string, @NotNull final EncodingsEnum stringEncoding) {
        this.string = string;
        this.stringEncoding = stringEncoding;
    }

    public String getString() {
        return this.string;
    }

    public EncodingsEnum getStringEncoding() {
        return this.stringEncoding;
    }
}
