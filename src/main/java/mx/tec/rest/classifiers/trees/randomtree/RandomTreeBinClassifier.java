package mx.tec.rest.classifiers.trees.randomtree;

import mx.tec.rest.classifiers.Classifier;
import mx.tec.rest.service.ClassifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mx.tec.rest.model.Flow;
import weka.classifiers.trees.RandomTree;
import weka.core.Instance;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;

public class RandomTreeBinClassifier extends Classifier {
    private static Logger log = LoggerFactory.getLogger(RandomTreeBinClassifier.class);

    @Override
    public RandomTree decode(String modelPath) {
        log.debug("Constructing RandomTree...");
        RandomTree randomTree = new RandomTree();
        try {
            URL resource = ClassifierService.class.getClassLoader().getResource(modelPath);
            log.info("Recuperando url del modelo: " + resource);
            ObjectInputStream ois = new ObjectInputStream(resource.openStream());
            randomTree = (RandomTree) ois.readObject();
            ois.close();
            log.info("Random tree classifier loaded");
        } catch (Exception e) {
            log.error("Error while loading random forest classifier");
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return randomTree;
    }
}