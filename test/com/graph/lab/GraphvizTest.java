package com.graph.lab;

import org.junit.jupiter.api.Test;

class GraphvizTest {

    private final String txt = "i do not love you";
    private final WordGraph wordGraph = new WordGraph();


    @Test
    void showDirectedGraph() {
        wordGraph.buildGraph(txt);
        Graphviz.showDirectedGraph(wordGraph);
    }
    //测试
    @Test
    void generateGraphWithShortestPath() {
        wordGraph.buildGraph(txt);

        String word1 = "explore";
        String word2 = "lzl";

        String result = wordGraph.calcShortestPath(word1, word2);
        String outputFileName = word1 + "_to_" + word2 + ".png";
        Graphviz.generateGraphWithShortestPath(wordGraph, result, outputFileName, "png");
    }
}