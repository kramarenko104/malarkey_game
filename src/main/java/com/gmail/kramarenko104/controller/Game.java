package com.gmail.kramarenko104.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.gmail.kramarenko104.model.*;
import org.apache.log4j.Logger;

public class Game {

    private static final Path WHO_FILE_PATH = Paths.get(".", "/src/main/resources/nouns.txt").toAbsolutePath().normalize();
    private static final Path VERB_FILE_PATH = Paths.get(".", "/src/main/resources/verbs.txt").toAbsolutePath().normalize();
    private static final Path WHERE_FILE_PATH = Paths.get(".", "/src/main/resources/where.txt").toAbsolutePath().normalize();
    private static final Path WHY_FILE_PATH = Paths.get(".", "/src/main/resources/why.txt").toAbsolutePath().normalize();
    private static final String MUSIC_FILE = "/lp-lost_on_you_original.mp3";
    private static String splitLine = "\n----------------------------------------------";
    private static Logger logger = Logger.getLogger(Game.class);
    private RunnableWord who;
    private RunnableWord whatDoes;
    private RunnableWord where;
    private RunnableWord why;
    private Musician musician;
    private List<RunnableWord> wordsList;

    public Game() {
        logger.debug("Start application...");

        musician = new Musician(true, MUSIC_FILE);
        new Thread(musician).start();

        wordsList = new ArrayList<>();

        // 'WHO'
        // first resource for sentence' words: local file 'nouns.txt'
        who = new Who(WHO_FILE_PATH);
        wordsList.add(who);
        logger.debug("'who' source was created from file 'nouns.txt'");

        // 'WHAT DOES'
        // next resource for sentence' words: local MySQL database, table 'whatDoes'
        whatDoes = new WhatDoes(VERB_FILE_PATH);
        wordsList.add(whatDoes);
        logger.debug("'what' source was created from MySQL database, table 'actions'");

        // 'WHERE'
        // next resource for sentence' words: local strings' list
        where = new Where(WHERE_FILE_PATH);
        wordsList.add(where);
        logger.debug("'where' source was created from local strings' list");

        // 'WHY'
        // next resource for sentence' words: local MySQL database, table 'reasons'
        why = new Why(WHY_FILE_PATH);
        wordsList.add(why);
        logger.debug("'why' source was created from MySQL database, table 'reasons'");

        logger.debug("Init all sources with data...");
        wordsList.stream().forEach(e -> e.fillWithValues());
        logger.debug("All sources were filled out with data from /resources text files" + splitLine);
    }

    public String createSentence() {
        StringBuilder sentence = new StringBuilder();
        CountDownLatch startLatch = new CountDownLatch(4);
        ExecutorService pool = Executors.newCachedThreadPool();

        // run threads for word's parallel creation
        wordsList.stream().forEach(
                e -> {
                    e.setCountDownLatch(startLatch);
                    pool.execute(e);
                });

        // wait until all threads finish their work
        try {
            startLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
        logger.debug("all 4 threads finish their work:");

        // collect results from all threads
        wordsList.stream().forEach(e -> sentence.append(e.getWord()).append(" "));
        logger.debug(sentence + splitLine);
        return sentence.toString();
    }

    public void playMusic(boolean toPlay) {
        musician.setPlay(toPlay);
    }

    public RunnableWord getWho() {
        return who;
    }

    public RunnableWord getWhatDoes() {
        return whatDoes;
    }

    public RunnableWord getWhere() {
        return where;
    }

    public RunnableWord getWhy() {
        return why;
    }

    public void exit() {
        wordsList.stream().forEach(e -> e.close());
        logger.debug("Close all resources and exit " + splitLine);
        System.exit(0);
    }
}
