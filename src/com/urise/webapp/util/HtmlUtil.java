package com.urise.webapp.util;

import com.urise.webapp.model.Organization;

public class HtmlUtil {

    public static String notNull(String s) {
        return s == null ? "" : s;
    }

    public static String linkOrName(Organization organization) {
        String url = organization.getHomePage().getUrl();
        String name = organization.getHomePage().getName();
        if (url == null || url.isEmpty()) return name;
        return "<a href='" + url + "'>" + name + "</a>";
    }

    public static boolean notExistItem(String value) {
        return (value == null || value.trim().length() == 0);
    }

}
