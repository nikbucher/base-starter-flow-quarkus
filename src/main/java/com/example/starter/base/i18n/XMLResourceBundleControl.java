package com.example.starter.base.i18n;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class XMLResourceBundleControl extends ResourceBundle.Control {

    public static final String XML = "xml";

    /**
     * @see ResourceBundle.Control
     */
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
        if (baseName == null || locale == null || format == null || loader == null) {
            throw new IllegalArgumentException("baseName, locale, format and loader cannot be null");
        }
        if (!format.equals(XML)) {
            throw new IllegalArgumentException("format must be xml");
        }

        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, format);

        final InputStream is;
        if (reload) {
            URL url = loader.getResource(resourceName);
            if (url == null) return null;
            URLConnection connection = url.openConnection();
            if (connection == null) return null;

            // Disable caches to get fresh data for reloading.
            connection.setUseCaches(false);
            is = connection.getInputStream();
        } else {
            is = loader.getResourceAsStream(resourceName);
        }

        return getXmlResourceBundle(is);
    }

    private static XMLResourceBundle getXmlResourceBundle(InputStream is) throws IOException {
        try (InputStream inputStream = is;
             BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            return new XMLResourceBundle(bis);
        }
    }

    @Override
    public List<String> getFormats(String baseName) {
        return List.of(XML);
    }

}
