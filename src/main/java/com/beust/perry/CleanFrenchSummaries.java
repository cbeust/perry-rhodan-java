package com.beust.perry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Files;

public class CleanFrenchSummaries extends Perry {
//    private String m_file = "/Users/cedric/Documents/perry/vannereux-decrypted/1569.txt";
    private String m_file = "/Users/cedric/Documents/perry/vannereux-decrypted/22.txt";
//    private String m_file = "/tmp/a.txt";
    private static String[] SUB = new String[] {
        "à", new String(new byte[] { (byte) 0xc3, (byte) 0xa0 }), "&agrave;",
        "â", new String(new byte[] { (byte) 0xc3, (byte) 0xa2 }), "&acirc;", // c3 a2
        "é", new String(new byte[] { (byte) 0xc3, (byte) 0xa9 }), "&eacute;", // c3 a9
        "è", new String(new byte[] { (byte) 0xc3, (byte) 0xa8 }), "&egrave;", // c3 a8
        "ê", new String(new byte[] { (byte) 0xc3, (byte) 0xaa }), "&ecirc;",
        "ê", "ê", "&ecirc;",
        "ô", new String(new byte[] { (byte) 0xc3, (byte) 0xb3 }), "&ocirc;", // c3 b4
        "ô", new String(new byte[] { (byte) 0xc3, (byte) 0xb4 }), "&ocirc;", // c3 b4
        "î", new String(new byte[] { (byte) 0xc3, (byte) 0xae }), "&icirc;", // c3 ae
        "ù", new String(new byte[] { (byte) 0xc3, (byte) 0xb9 }), "&ugrave;", // c3 b9
        "ç", new String(new byte[] { (byte) 0xc3, (byte) 0xa7 }), "&ccedil;", // c3 a7
        "’", new String(new byte[] { (byte) 0xe2, (byte) 0x80, (byte) 0x99 }), "&apos;",
        "'", new String(new byte[] { (byte) 0x27 }), "&apos;",
        "'", "'", "&apos;",
        "ï", new String(new byte[] { (byte) 0xc3, (byte) 0xaf }), "&iuml;",
        "«", new String(new byte[] { (byte) 0xc2, (byte) 0xab }), "\"",
        "»", new String(new byte[] { (byte) 0xc2, (byte) 0xbb }), "\"",
    };

    private static Map<String, String> SUB_MAP = new HashMap<String, String>();

    static {
        for (int i = 0; i < SUB.length; i += 3) {
            SUB_MAP.put(SUB[i + 1], SUB[i + 2]);
        }
    }

    private static String s(String line) {
        if (line == null) return line;
        String result = line;
        for (Map.Entry<String, String> e : SUB_MAP.entrySet()) {
            result = result.replace(e.getKey(), e.getValue());
        }

        return result;
    }

    private String read(BufferedReader br) throws IOException {
        return s(br.readLine());
    }

    public CleanFrenchSummaries() throws IOException {
        StringBuilder file = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(m_file));
        String line = read(br);
        while (line != null && !"".equals(line)) {
            String number = line;
            String title = read(br);
            line = read(br);
            StringBuilder summary = new StringBuilder();
            while (line != null && ! "".equals(line)) {
                summary.append(line).append("<p>");
                line = read(br);
            }
            file.append("delete from summaries_fr where number = " + number + ";\n");
            file.append("insert into summaries_fr (number, english_title, summary) "
                    + "values (" + number + ", '" + title + "', '" + summary + "');")
                    .append("\n");
            System.out.println("Number:" + number + " Title:" + title + "\n" + summary);
            line = br.readLine();
        }
        Files.write(file.toString().getBytes(), new File("/tmp/a.sql"));
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(s("L'appel de l'immortalité"));
        new CleanFrenchSummaries();
    }
}
