package com.beust.perry;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

@Test
public class PerryTest {
  public void test() {
    System.out.println("Running tests");
    Assert.assertEquals(Perry.matchCase("abc", "Abc"), "Abc");
    Assert.assertEquals(Perry.matchCase("abc", "ABC"), "ABC");
    Assert.assertEquals(Perry.replaceAllWithCase("ABC Abc abc", "ABC", "foo"),
        "FOO Foo foo");

    Assert.assertEquals(Perry.replaceAllWithCase("her/it/them did something", "her/it/them",
        "h_er"),
        "h_er did something");
    Assert.assertEquals(Perry.replaceAllWithCase("Her/it/them did something", "her/it/them",
        "h_er"),
        "H_er did something");
    Assert.assertEquals(Perry.replaceAllWithCase("His/its family", "his/its", "h_is"),
        "H_is family");
    Assert.assertEquals(Perry.replaceAllWithCase("le-Ben", "-([a-z])", null), "le-Ben");
    Assert.assertEquals(Perry.replaceAllWithCase("le-ben", "-([a-z])", null), "leben");
    Assert.assertEquals(Perry.replaceAllWithCase("a\\par", "([a-z])\\par", null), "a");
    Assert.assertEquals(Perry.replaceAllWithCase("a.\\par", "([a-z])\\par", null), "a.\\par");
    Assert.assertEquals(Perry.replaceAllWithCase("abc\\par", "([a-z])\\par", null), "abc");
    Assert.assertEquals(Perry.replaceAllWithCase("abc.\\par", "([a-z])\\par", null), "abc.\\par");
    Assert.assertEquals(Perry.replaceAllWithCase("strangely\\par\n", "([a-z])\\par", null),
        "strangely\n");
  }
  
  public void testWithMap() {
    Map<String, String> strings = new LinkedHashMap<String, String>() {{
      put("he/it", "h_e");
      put("h_e", "he");
    }};

    String newText = "Once, he/it had tried it";
    newText = new FileVisitor(strings, null /* ignored */).performReplacement(newText);
    Assert.assertEquals(newText, "Once, he had tried it");

  }


}
