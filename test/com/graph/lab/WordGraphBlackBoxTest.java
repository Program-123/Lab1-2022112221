package com.graph.lab;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordGraphBlackBoxTest {

    private WordGraph wordGraph;
    private String currentTestName;

    @BeforeEach
    void setup(TestInfo testInfo) {
        wordGraph = new WordGraph();
        currentTestName = testInfo.getDisplayName();
        System.out.println("\n=== 开始测试: " + currentTestName + " ===");
    }

    @AfterEach
    void tearDown() {
        System.out.println("=== " + currentTestName + " 测试成功! ===");
    }

    @Test
    void testCase1_aLittleBaby() {
        wordGraph.buildGraph("a little baby");
        System.out.println("测试场景: 简单三个单词连接");

        // 测试calcShortestPath
        String pathResult = wordGraph.calcShortestPath("a", "baby");
        assertTrue(pathResult.contains("a -> little -> baby"),
                "最短路径应包含: a -> little -> baby");
        System.out.println("✓ calcShortestPath 通过");

        // 测试queryBridgeWords
        String bridgeResult = wordGraph.queryBridgeWords("a", "baby");
        assertEquals("The bridge words from a to baby are: little", bridgeResult);
        System.out.println("✓ queryBridgeWords 通过");

        // 测试generateNewText
        String generateResult = wordGraph.generateNewText("a baby");
        assertEquals("a little baby", generateResult);
        System.out.println("✓ generateNewText 通过");
    }

    @Test
    void testCase2_AllSeparators() {
        String input = "@ @ @ @";
        if (!input.matches("^[\\s@,;:.!?]+$")) {
            wordGraph.buildGraph(input);
        }
        System.out.println("测试场景: 全部分隔符，无有效单词");

        // 测试calcShortestPath
        String pathResult = wordGraph.calcShortestPath("a", "b");
        assertEquals("No word1 in the graph!", pathResult);
        System.out.println("✓ calcShortestPath 通过");

        // 测试queryBridgeWords
        String bridgeResult = wordGraph.queryBridgeWords("a", "b");
        assertEquals("Neither a nor b is in the graph!", bridgeResult);
        System.out.println("✓ queryBridgeWords 通过");

        // 测试generateNewText
        String generateResult = wordGraph.generateNewText("a b");
        assertEquals("a b", generateResult);
        System.out.println("✓ generateNewText 通过");
    }

    @Test
    void testCase3_MixedWithSeparators() {
        wordGraph.buildGraph("a@little@baby");
        System.out.println("测试场景: 单词与分隔符混合（有效单词）");

        // 测试calcShortestPath
        String pathResult = wordGraph.calcShortestPath("a", "baby");
        assertTrue(pathResult.contains("a -> little -> baby"),
                "最短路径应包含: a -> little -> baby");
        System.out.println("✓ calcShortestPath 通过");

        // 测试queryBridgeWords
        String bridgeResult = wordGraph.queryBridgeWords("a", "baby");
        assertEquals("The bridge words from a to baby are: little", bridgeResult);
        System.out.println("✓ queryBridgeWords 通过");

        // 测试generateNewText
        String generateResult = wordGraph.generateNewText("a baby");
        assertEquals("a little baby", generateResult);
        System.out.println("✓ generateNewText 通过");
    }

    @Test
    void testCase4_WithSpecialCharacters() {
        wordGraph.buildGraph("a little baby の");
        System.out.println("测试场景: 包含特殊字符的单词");

        // 测试calcShortestPath
        String pathResult = wordGraph.calcShortestPath("a", "baby");
        assertTrue(pathResult.contains("a -> little -> baby"),
                "最短路径应包含: a -> little -> baby");
        System.out.println("✓ calcShortestPath 通过");

        // 测试queryBridgeWords
        String bridgeResult = wordGraph.queryBridgeWords("a", "baby");
        assertEquals("The bridge words from a to baby are: little", bridgeResult);
        System.out.println("✓ queryBridgeWords 通过");

        // 测试generateNewText
        String generateResult = wordGraph.generateNewText("a baby の");
        assertEquals("a little baby の", generateResult);
        System.out.println("✓ generateNewText 通过");
    }
}