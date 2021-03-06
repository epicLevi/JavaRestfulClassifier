package mx.tec.rest.service;

import mx.tec.rest.classifiers.Classifier;
import mx.tec.rest.classifiers.trees.j48.J48BinClassifier;
import mx.tec.rest.classifiers.trees.randomtree.RandomTreeBinClassifier;
import mx.tec.rest.classifiers.trees.reptree.REPTreeBinClassifier;
import mx.tec.rest.model.ClassifierRequest;
import mx.tec.rest.model.ClassifierResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

public class ClassifierService implements IClassifierService {
    private static Logger log = LoggerFactory.getLogger(RandomTreeBinClassifier.class);

    private ClassifierResponse noModelResponse = new ClassifierResponse(
        "error",
        -1,
        "Model file does not exist."
    );

    @Override
    public ClassifierResponse classifyFlow(ClassifierRequest request) {
        ClassifierResponse response = new ClassifierResponse();
        String requestedClassifier = request.getClassifier();
        log.info("Requested classifier: " + requestedClassifier);

        Classifier classifier;
        String modelPath;
        int classEnumeration;
        switch (requestedClassifier) {
            case "randomtree":
                classifier = new RandomTreeBinClassifier();
                modelPath = "models/randomTree.appddos.model";

                if (!verifyModelExists(modelPath)) {
                    response = noModelResponse;
                    break;
                }

                classifier.load(modelPath);
                classEnumeration = classifier.classify(request.getFlow());
                response = getClassString(classEnumeration);
                break;
            case "reptree":
                classifier = new REPTreeBinClassifier();
                modelPath = "models/reptree.appddos.model";

                if (!verifyModelExists(modelPath)) {
                    response = noModelResponse;
                    break;
                }

                classifier.load(modelPath);
                classEnumeration = classifier.classify(request.getFlow());
                response = getClassString(classEnumeration);
                break;
            case "j48":
                classifier = new J48BinClassifier();
                modelPath = "models/j48.appddos.model";

                if (!verifyModelExists(modelPath)) {
                    response = noModelResponse;
                    break;
                }

                classifier.load(modelPath);
                classEnumeration = classifier.classify(request.getFlow());
                response = getClassString(classEnumeration);
                break;
            default:
                response = new ClassifierResponse(
                    "error",
                    -1,
                    "No classifier selected."
                );
        }

        return response;
    }

    private boolean verifyModelExists(String modelPath) {
        URL resource = ClassifierService
            .class
            .getClassLoader()
            .getResource(modelPath);

        boolean exists = new File(modelPath).exists();

        if (!exists && resource == null) {
            return false;
        }

        return true;
    }

    private ClassifierResponse getClassString(int classEnumeration) {
        ClassifierResponse response = new ClassifierResponse();
        response.setClassEnumeration(classEnumeration);
        response.setClassifiedAs("error");

        switch (classEnumeration) {
            case -1:
                response.setMessage("Internal classifier error.");
                break;
            case 0:
                response.setClassifiedAs("normal");
                response.setMessage("Classified as normal flow.");
                break;
            case 1:
                response.setClassifiedAs("attack");
                response.setMessage("Classified as attack flow.");
                break;
            case 2:
                response.setClassifiedAs("slowbody2");
                response.setMessage("Classified as slowbody2 attack flow.");
                break;
            case 3:
                response.setClassifiedAs("slowread");
                response.setMessage("Classified as slowread attack flow.");
                break;
            case 4:
                response.setClassifiedAs("ddosim");
                response.setMessage("Classified as ddosim attack flow.");
                break;
            case 5:
                response.setClassifiedAs("slowheaders");
                response.setMessage("Classified as slowheaders attack flow.");
                break;
            case 6:
                response.setClassifiedAs("goldeneye");
                response.setMessage("Classified as goldeneye attack flow.");
                break;
            case 7:
                response.setClassifiedAs("rudy");
                response.setMessage("Classified as rudy attack flow.");
                break;
            case 8:
                response.setClassifiedAs("hulk");
                response.setMessage("Classified as hulk attack flow.");
                break;
            case 9:
                response.setClassifiedAs("slowloris");
                response.setMessage("Classified as slowloris attack flow.");
                break;
            default:
                response.setMessage("Classified as an non-existant class of flow.");
        }

        return response;
    }
}
