package com.urise.webapp.util;

import com.urise.webapp.model.Resume;
import com.urise.webapp.model.sections.Section;
import com.urise.webapp.model.sections.TextSection;
import org.junit.Assert;
import org.junit.Test;

import static com.urise.webapp.TestData.RESUME_1;

public class JsonParserTest {

    @Test
    public void testResume() {
        String json = JsonParser.write(RESUME_1);
        System.out.printf(json);
        Resume testResume = JsonParser.read(json, Resume.class);
        Assert.assertEquals(RESUME_1, testResume);
    }

    @Test
    public void write() {
        Section expSection = new TextSection("TestSection");
        String json = JsonParser.write(expSection, Section.class);
        System.out.println(json);
        Section parsedSection = JsonParser.read(json, Section.class);
        Assert.assertEquals(expSection, parsedSection);
    }
}