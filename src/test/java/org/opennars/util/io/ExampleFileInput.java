package org.opennars.util.io;

import org.opennars.main.Nar;
import org.opennars.util.test.OutputCondition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Access to library of examples/unit tests
 */
public class ExampleFileInput {

    public static String load(final String path) throws IOException {
        final StringBuilder sb = new StringBuilder();
        String line;
        final File fp = new File(path);
        final BufferedReader br = new BufferedReader(new FileReader(fp));
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }

    /** narsese source code, one instruction per line */
    private final String source;

    protected ExampleFileInput(final String input) {
        this.source = input;
    }

    public static ExampleFileInput get(final String id) throws Exception {
        return new ExampleFileInput(load("./nal/" + id + ".nal"));
    }

    public List<OutputCondition> enableConditions(final Nar n, final int similarResultsToSave) {
        return OutputCondition.getConditions(n, source, similarResultsToSave);
    }

    public static Map<String, Object> getUnitTests(final String[] directories) {
        final Map<String, Object> l = new TreeMap<>();

        for (final String dir : directories) {

            File folder = null;
            try {
                folder = new File(Nar.class.getResource(dir).toURI());
            } catch (final URISyntaxException e) {
                throw new IllegalStateException("Could not resolve path to nal tests in reosources.", e);
            }

            if (folder.listFiles() != null) {
                for (final File file : folder.listFiles()) {
                    if (file.getName().equals("README.txt") || file.getName().contains(".png"))
                        continue;
                    if (!("extra".equals(file.getName()))) {
                        l.put(file.getName(), new Object[] { file.getAbsolutePath() });
                    }
                }
            }

        }
        return l;
    }

    public String getSource() {
        return source;
    }

}
