import org.deeplearning4j.graph.api.Edge;
import org.deeplearning4j.graph.api.Vertex;
import org.deeplearning4j.graph.graph.Graph;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.text.sentenceiterator.labelaware.LabelAwareFileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.labelaware.LabelAwareSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RobertTest {
    private static final Logger log = LoggerFactory.getLogger(RobertTest.class);

    public static void main(String[] args) {
        try {
            BufferedReader input = new BufferedReader(new FileReader("/home/kilt/BigData-Prak/Deep-Walk-4J/src/test/resources/12831.feat"));
            String line = "";
            HashMap<Integer, Integer> indexMapping = new HashMap();
            ArrayList<Vertex> vertices = new ArrayList();
            int index = 0;
            while((line = input.readLine()) != null) {
                int label = Integer.parseInt(line.split(" ")[0]);
                Vertex v = new Vertex(index, label);
                vertices.add(v);
                indexMapping.put(label, index);
                index++;
            }
            System.out.println("Vertices loaded.");
            Graph g = new Graph(vertices);
            input = new BufferedReader(new FileReader("/home/kilt/BigData-Prak/Deep-Walk-4J/src/test/resources/12831.edges"));
            int j = 0;
            while((line = input.readLine()) != null) {
                String[] split = line.split(" ");
                int label1 = Integer.parseInt(split[0]);
                int label2 = Integer.parseInt(split[1]);
                Edge e = new Edge(indexMapping.get(label1), indexMapping.get(label2), "e" + j, true);
                j++;
                System.out.println(e);
                g.addEdge(e);
            }
            System.out.println("Edges loaded.");
            input = new BufferedReader(new FileReader("/home/kilt/BigData-Prak/Deep-Walk-4J/src/test/resources/12831.featnames"));
            ArrayList<String> labels = new ArrayList();
            while((line = input.readLine()) != null) {
                labels.add(line.split(" ")[1]);
            }
            ClassPathResource res = new ClassPathResource("/12831.feat");
            File file = res.getFile();
            LabelAwareSentenceIterator iter = new LabelAwareFileSentenceIterator(file);
            TokenizerFactory tok = new DefaultTokenizerFactory();

            ParagraphVectors vec = new ParagraphVectors.Builder()
                    .minWordFrequency(1).labels(labels)
                    .layerSize(100)
                    .stopWords(new ArrayList<String>())
                    .windowSize(5).iterate(iter).tokenizerFactory(tok).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
