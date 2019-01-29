package com.gmail.kramarenko104.model;

import org.apache.log4j.Logger;
import java.nio.file.Path;

public class Who extends RunnableWord {

    private static Logger logger = Logger.getLogger(Who.class);

    public Who(Path sourceFilePath) {
        super(sourceFilePath);
    }

    @Override
    public void run() {
        super.run();
        logger.debug("[" + Thread.currentThread().getName() + "] " + resultWord);
        cdl.countDown();
    }

    @Override
    // add the new word from GUI to source file
    public void addWord(String word) {
        super.addWord(word);
        logger.debug("Phrase '" + word + "' was added\n" );
    }

}


