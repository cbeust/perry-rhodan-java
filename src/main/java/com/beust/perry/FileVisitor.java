package com.beust.perry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class FileVisitor {

  private String m_outDir;
  private Map<String, String> m_strings;

  public FileVisitor(Map<String, String> strings, String outDir) {
    m_strings = strings;
    m_outDir = outDir;
  }

  public void visit(File file) throws IOException {
    p("Replacing in file:" + file + " outDir:" + m_outDir);

    String fileText = readFile(file);
    fileText = performReplacement(fileText);
    File outFile = new File(m_outDir, file.getName());
    outFile.delete();

    p("Writing " + outFile.getAbsolutePath());
    writeFile(outFile, fileText);
  }

  /* for testing */ String performReplacement(String fileText) {
    String result = fileText;
    for (Map.Entry<String, String> s : m_strings.entrySet()) {
      result = Perry.replaceAllWithCase(result, s.getKey(), s.getValue());
    }

    return result;
  }

  private void writeFile(File outFile, String text) throws IOException {
    Writer output = new BufferedWriter(new FileWriter(outFile));
    output.write(text);
    output.close();
  }

  private void p(String string) {
    System.out.println("[FileVisitor] " + string);
  }

  private static String readFile(File file) throws IOException {
    String lineSep = System.getProperty("line.separator");
    BufferedReader br = new BufferedReader(new FileReader(file));
    String nextLine = "";
    StringBuffer sb = new StringBuffer();
    while ((nextLine = br.readLine()) != null) {
      sb.append(nextLine);
      //
      // note:
      //   BufferedReader strips the EOL character
      //   so we add a new one!
      //
      sb.append(lineSep);
    }
    return sb.toString();
 }}
