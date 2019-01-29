package com.gmail.kramarenko104.model;

import com.gmail.kramarenko104.dao.DBWorker;
import org.apache.log4j.Logger;
import java.nio.file.Path;

public class Why extends RunnableWord {

    private static Logger logger = Logger.getLogger(Why.class);
    private DBWorker DBWorker;

    public Why(Path sourceFilePath) {
        super(sourceFilePath);
        DBWorker = new DBWorker("reasons", "reason");
        this.sourceFilePath = sourceFilePath;
    }

    @Override
    public void run() {
        resultWord = DBWorker.getRandomWord();
        logger.debug("[" + Thread.currentThread().getName() + "] " + resultWord);
        cdl.countDown();
    }

    @Override
    public void fillWithValues() {
        DBWorker.init();
        DBWorker.fillWithValues(sourceFilePath);
    }

    @Override
    // add the new word from GUI to source file and DB table
    public void addWord(String word) {
        super.addWord(word);
        DBWorker.addWord(word);
        logger.debug("Phrase '" + word + "' was added\n" );
    }

    @Override
    public void close() {
        DBWorker.close();
    }
}
