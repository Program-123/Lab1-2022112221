package com.graph.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordGraphWhiteBoxBridgeWordsTest {

    private WordGraph wordGraph;

    @BeforeEach
    void setUp() {
        wordGraph = new WordGraph();
    }

    // 测试用例1
    @Test
    // 构建空图，使an和babies都不在图中
    void testQueryBridgeWords_Case1() throws Exception {

        Map<String, List<String>> adjacencyList = new HashMap<>();
        setAdjacencyList(adjacencyList);

        String word1 = "an";
        String word2 = "babies";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Neither an nor babies is in the graph!", result);
        System.out.println("测试用例1通过");
    }

    // 测试用例2
    @Test
    void testQueryBridgeWords_Case2() throws Exception {
        // 构建空图，使a和babies都不在图中
        Map<String, List<String>> adjacencyList = new HashMap<>();
        setAdjacencyList(adjacencyList);

        String word1 = "a";
        String word2 = "babies";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("Neither a nor babies is in the graph!", result);
        System.out.println("测试用例2通过");
    }

    // 测试用例3
    @Test
    void testQueryBridgeWords_Case3() throws Exception {
        // 重新构建图结构，确保baby和a都存在，且没有从baby到a的路径
        Map<String, List<String>> adjacencyList = new HashMap<>();

        // 确保baby节点存在且有邻居
        List<String> neighborsOfBaby = new ArrayList<>();
        neighborsOfBaby.add("c");
        adjacencyList.put("baby", neighborsOfBaby);

        // 确保a节点存在且有邻居，但不与baby连通
        List<String> neighborsOfA = new ArrayList<>();
        neighborsOfA.add("d");
        adjacencyList.put("a", neighborsOfA);

        // 添加中间节点c和d，但不让它们连通
        List<String> neighborsOfC = new ArrayList<>();
        neighborsOfC.add("e"); // c连接到e，不连接到a
        adjacencyList.put("c", neighborsOfC);

        List<String> neighborsOfD = new ArrayList<>();
        neighborsOfD.add("f"); // d连接到f，不连接到baby
        adjacencyList.put("d", neighborsOfD);

        // 添加e和f节点，确保图结构完整但baby到a不连通
        adjacencyList.put("e", new ArrayList<>());
        adjacencyList.put("f", new ArrayList<>());

        setAdjacencyList(adjacencyList);

        String word1 = "baby";
        String word2 = "a";
        String result = wordGraph.queryBridgeWords(word1, word2);
        assertEquals("No bridge words from baby to a!", result);
        System.out.println("测试用例3通过");
    }

    // 测试用例4
    @Test
    void testQueryBridgeWords_Case4() throws Exception {
        // 构建图结构，使a到baby的最短路径长度为2（即a->b->baby）
        Map<String, List<String>> adjacencyList = new HashMap<>();

        // a连接到b
        List<String> neighborsOfA = new ArrayList<>();
        neighborsOfA.add("b");
        adjacencyList.put("a", neighborsOfA);

        // b连接到baby
        List<String> neighborsOfB = new ArrayList<>();
        neighborsOfB.add("baby");
        adjacencyList.put("b", neighborsOfB);

        // baby节点存在但可以没有出边，或者有出边但不影响测试
        List<String> neighborsOfBaby = new ArrayList<>();
        adjacencyList.put("baby", neighborsOfBaby);

        setAdjacencyList(adjacencyList);

        String word1 = "a";
        String word2 = "baby";
        String result = wordGraph.queryBridgeWords(word1, word2);

        // 根据实际的返回格式调整断言
        // 实际返回格式是: "The bridge words from a to baby are: b"
        System.out.println("实际返回结果: " + result);

        assertTrue(result.contains("The bridge words from a to baby are: b"),
                "预期结果应该是'The bridge words from a to baby are: b'，实际结果: " + result);
        System.out.println("测试用例4通过");
    }

    // 通过反射设置邻接表
    private void setAdjacencyList(Map<String, List<String>> adjacencyList) throws Exception {
        Field field = WordGraph.class.getDeclaredField("adjacencyList");
        field.setAccessible(true);
        field.set(wordGraph, adjacencyList);
    }
}