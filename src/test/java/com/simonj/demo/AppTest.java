package com.simonj.demo;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for simple App.
 */
@Test
public class AppTest {
    public void simpleTest() {
        List<String> lines = ImmutableList.<String>of(
                "Lorem Ipsum is simply dummy text of the printing and typesetting",
                "industry. Lorem Ipsum has been the industry's standard dummy",
                "text ever since the 1500s, when an unknown printer took a galley",
                "of type and scrambled it to make a type specimen book. It has",
                "survived not only five centuries, but also the leap into",
                "electronic typesetting, remaining essentially unchanged. It was",
                "popularised in the 1960s with the release of Letraset sheets",
                "containing Lorem Ipsum passages, and more recently with desktop",
                "publishing software like Aldus PageMaker including versions of",
                "Lorem Ipsum.");

        Map<String, Integer> counts = App.countWords(lines);

        Assert.assertEquals(counts.get("lorem").intValue(), 4);
        Assert.assertEquals(counts.get("and").intValue(), 3);
        Assert.assertEquals(counts.get("text").intValue(), 2);
        Assert.assertEquals(counts.get("pagemaker").intValue(), 1);
    }
}
