package com.beust.perry;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Perry
{

  // Windows
  private static final String TOP_DIR_WINDOWS = "c:/Users/Cedric/Documents/perry-rhodan/";
  // Mac
  private static final String TOP_DIR_MAC = "/Users/cbeust/Documents/perry-rhodan/";

  private static final String TOP_DIR = TOP_DIR_WINDOWS;

  private static final String GERMAN_DIR = TOP_DIR + "ge2/";
  private static final String OUT_GERMAN_DIR = TOP_DIR + "ge-clean/";
  private static final String ENGLISH_DIR = TOP_DIR + "en/";
  private static final String OUT_ENGLISH_DIR = TOP_DIR + "en-clean/";

  private boolean debug = false;
  private boolean m_singleFile = false;
  private boolean verbose = true;
  private String key = "AIzaSyCay1CnJHuVpSBCiCLyfIrxZdH1PbEtWVY";

  /**
   * null as a value means that every group of characters that is not between
   * parentheses will be removed.
   */
  private static final Map<String, String> GERMAN_STRINGS = new LinkedHashMap<String, String>() {{
    put("\r", " ");
    put("\n", " \n");
    put("\\emdash", "-");
    // Remove all hyphens if they are in the middle of a word. I check that by seeing
    // if the character that follows the hyphen is an uppercase letter (then the hyphen
    // is separating two words, we keep it) or a lowercase letter (then the hyphen
    // is splitting one word, we remove it).
    put("-([a-z])", null);
    // RTF clean up: only keep \par directives at the end of a sentence.
    // If we are in the middle of a sentence, remove \par.
    put("([a-z])\\par", null);
    //    "{\\\u8212\\\'97}", " ",
//    " - ", ""
  }};

  private Map<String, String> ENGLISH_STRINGS = new LinkedHashMap<String, String>() {{
    put("\r", " ");
    put("\n", " \n");
    put("([a-z])\\par", null);
    // longer strings first
    put("her/it/them", "h_er");
    put("her/its/their", "t_heir");
    put("she/it/they", "s_he");
    put("them/her/it", "t_hem");
    put("her/them", "t_hem");
    put("he/it", "h_e");
    put("her/it", "h_er");
    put("his/its", "h_is");
    put("him/it", "h_im");
    put("himself/itself", "h_imself");
    put("itself/themselves", "i_tself");
    put("she/it", "s_he");
    put("she/they", "s_he");
    put("them/her", "t_hem");
    put("you/they", "t_hey");
    put("your/their", "t_heir");
    put("them/you", "t_hem");

    put("h_er", "her");
    put("h_e", "he");
    put("h_is", "his");
    put("h_im", "him");
    put("i_tself", "itself");
    put("s_he", "she");
    put("t_heir", "their");
    put("t_hem", "them");
    put("t_hey", "they");
    put("h_himself", "himself");

    put(", that", " that");
    put("-loose", "less");
    put("a moment once", "hold on");
    put("amounted", "counted");
    put("antigravschacht", "antigrav shaft");
    put("area", "space");
    put("artgenossen", "compatriot");
    put("attendants sie", "wait");
    put("attendant", "wait");
    put("attendants still", "wait");
    put("befell", "happened to");
    put("cross-ion", "Querion");

    put("draw lots", "let's go"); // los jetz
    put("draws lots", "less");
    put("cantarische", "Cantaros");
    put("cell - vibration - assets - gate", "cell activator");
    put("cell-assets-fool", "cell activator");
    put("cell-assets-gate", "cell activator");
    put("cell-assets-goal", "cell activator");
    put("asset gate", "activator");
    put("assets gate", "activator");
    put("clear become", "figure out"); // klarwerde
    put("drive around", "turn around");
    put("drives around", "turns around");
    put("drove around", "turned around");
    put("chronopuls", "Chronopulse");
    put("debit that is", "does that mean"); // Soll das heisse
    put("election", "choice");
    put("embankment", "wall");
    put("estuary", "opening");
    put("expounded", "explained");
    put("fauchte", "hissed");
    put("free-dealer", "Free Trader");
    put("free dealer", "Free Trader");
    put("gucky", "Pucky");
    put("heavily", "hardly");
    put("it gives", "there is");
    put("it gave", "there was");
    put("Galaktiker", "Galactics");
    put("Kugelsternhaufens", "globular cluster");
    put("Haluter", "Halutian");
    put("heaven", "sky");
    put("husbands", "men");
    put("innern", "inside");
    put("introduce itself", "imagine");
    put("is free with", "is going on with");
    put("it gives", "there is");
    put("long-distance-orientation", "long distance detection");
    put("marked", "Excellent"); // Ausgezeichne
    put("marsianische", "Martian");
    put("medo-roboter", "medo robot");
    put("mighty-ness-concentration", "sphere of influence");
    put("pubs group", "globular cluster");
    put("most upper", "supreme");
    put("nature", "creature");
    put("needed not", "didn't have");
    put("orientation", "detection");
    put("Pedokr=C3=A4fte", "Pedopower");
    put("reputation", "shout");
    put("respect", "detection");
    put("property", "good");
    put("properly had", "are right");
    put("properly kept", "was right");
    put("race tschubai", "Ras Tschubai");
    put("Raumhafens", "space port");
    put("robotische", "robotized");
    put("sat down in connection with", "opened a connection with");
    put("sentence", "step");
    put("shot for him through the head", "occurred to him");
    put("shot for her through the head", "occurred to her");
    put("something is", "what is");
    put("space-harbor", "space port");
    put("state security service", "stasis");
    put("stylish", "send");
    put("shelf-storage", "onboard computer");
    put("sturgeon-impulse", "surge pulse");
    put("suitor terraner the league", "league of the free Terrans");
    put("tax-computer", "control computer");
    put("Terraner", "Terran");
    put("Terranerin", "Terran");
    put("toot sorry for me", "I'm sorry");
    put("toot this", "sorry");
    put("to the thunder-eather", "the hell");
    put("umbrella", "screen");
    put("under-light-fast", "sublight speed");
    put("what is free", "what is going on");
    put("what was free", "what happened");
    put("whether", "maybe");
    put("you you", "you");
    put("still another", "neither a");
    put("until on", "except");
    put("ready-ron", "Paratron");
    put(" ad ", " display ");
    put(" ads ", " displays ");
    put("in agreement", "alright");
    put("terranische", "Terran");
    put("Linguiden", "Linguide");
    put("shall?", "mean?");
//    put("ischen", "ic");
//    put("ische", "ic");
//    put("isch", "ic");

  }};

  private void cleanStrings(Map<String, String> strings, String dir, String outDir)
      throws IOException {
    FileVisitor visitor = new FileVisitor(strings, outDir);
    if (m_singleFile) {
      visitor.visit(new File(dir + "a.rtf"));
    }
    else {
      eachFileRecurse(dir, visitor);
    }
  }

  void cleanEnglish() throws IOException {
    cleanStrings(ENGLISH_STRINGS, ENGLISH_DIR, OUT_ENGLISH_DIR);
  }

  void cleanGerman() throws IOException {
    cleanStrings(GERMAN_STRINGS, GERMAN_DIR, OUT_GERMAN_DIR);
  }

  private void eachFileRecurse(String directory, FileVisitor visitor) throws IOException {
    File d = new File(directory);
    if (! d.exists()) {
    	throw new RuntimeException(directory + " doesn't exist");
    }
    for (File f : d.listFiles()) {
      visitor.visit(f);
    }
  }

  /**
   * "ab" "cd" -> "cd"
   * "Ab" "cd" -> "Cd"
   * "AB" "cd" -> "CD"
   */
  static String matchCase(String string, String out)
  {
    if (! (string instanceof String)) return out;

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);
      if (i < out.length()) {
        char c2 = out.charAt(i);
        if (Character.isLowerCase(c2)) c = Character.toLowerCase(c);
        else if (Character.isUpperCase(c2)) c = Character.toUpperCase(c);
      }
      result.append(c);
    }

    return result.toString();
  }

  static String replaceAllWithCase(String string, String pattern, String rep) {
    Pattern p = rep != null
        ? Pattern.compile(Matcher.quoteReplacement(pattern), Pattern.CASE_INSENSITIVE)
        : Pattern.compile(Matcher.quoteReplacement(pattern));
    Matcher m = p.matcher(string);

    StringBuffer sb = new StringBuffer();

    while (m.find()) {
      if (rep != null){
        m.appendReplacement(sb, Matcher.quoteReplacement(matchCase(rep, m.group())));
      } else {
        m.appendReplacement(sb, Matcher.quoteReplacement(m.group(1)));
      }
    }
    m.appendTail(sb);

    return sb.toString();
  }

  public static void main(String[] args) throws IOException {
    Perry p = new Perry();
//    p.test();
//    p.cleanGerman();
    p.cleanEnglish();
    System.out.println("This is the main class, need to handle command line switches");
  }
}
