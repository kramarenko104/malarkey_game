package com.gmail.kramarenko104.model;

import com.gmail.kramarenko104.dao.FileWorker;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

public abstract class RunnableWord implements Runnable {

    protected CountDownLatch cdl;
    protected String resultWord;
    protected Path sourceFilePath;
    protected FileWorker fileWorker;

    public RunnableWord(Path sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
        fileWorker = new FileWorker(sourceFilePath);
    }

    public void addWord(String word) {
        fileWorker.addWord(word);
    }

    public void run() {
        resultWord = fileWorker.getRandomWord();
    }

    public String getWord() {
        return resultWord;
    }

    public void setCountDownLatch(CountDownLatch cdl) {
        this.cdl = cdl;
    }

    public void close() {
    }

    public void fillWithValues() {
    }

}
