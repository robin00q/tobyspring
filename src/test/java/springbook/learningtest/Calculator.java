package springbook.learningtest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public int calcSum(String filepath) throws IOException {
        LineCallback<Integer> lineCallback = (line, value) -> Integer.valueOf(line) + value;

        return lineReadTemplate(filepath, lineCallback, 0);
    }

    public int calcMultiply(String filePath) throws IOException {
        LineCallback<Integer> lineCallback = (line, value) -> Integer.valueOf(line) * value;

        return lineReadTemplate(filePath, lineCallback, 1);
    }

    public String concatenate(String filePath) throws IOException {
        LineCallback<String> lineCallback = (line, value) -> value + line;

        return lineReadTemplate(filePath, lineCallback, "");
    }

    public <T> T lineReadTemplate(String filePath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReaderCallback<T> brCallback = br -> {
            T res = initVal;
            String line = null;

            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        };
        return fileReadTemplate(filePath, brCallback);
    }

    public <T> T fileReadTemplate(String filepath, BufferedReaderCallback<T> callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));

            T ret = callback.doSomethingWithReader(br);

            return ret;
        } catch (IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("e.getMessage() = " + e.getMessage());
                }
            }
        }
    }
}
