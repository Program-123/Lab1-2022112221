package com.graph.lab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class WordGraph {
    //guoguo
    private Map<String, List<String>> adjacencyList;
    private Map<String, Integer> edgeWeights;

    public WordGraph() {
        this.adjacencyList = new HashMap<>();
        this.edgeWeights = new HashMap<>();
    }
    //bvbvc
    public void buildGraph(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].toLowerCase();
            String word2 = words[i + 1].toLowerCase();
            if (!adjacencyList.containsKey(word1)) {
                adjacencyList.put(word1, new ArrayList<>());
            }
            adjacencyList.get(word1).add(word2);

            String edge = word1 + " -> " + word2;
            edgeWeights.put(edge, edgeWeights.getOrDefault(edge, 0) + 1);
        }
        String lastWord = words[words.length - 1].toLowerCase();
        if (!adjacencyList.containsKey(lastWord)) {
            adjacencyList.put(lastWord, new ArrayList<>());
        }
    }

    public Map<String, List<String>> getAdjacencyList() {
        return adjacencyList;
    }

    public Map<String, Integer> getEdgeWeights() {
        return edgeWeights;
    }

    public String queryBridgeWords(String word1, String word2) {

//        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
//            return "No " + (adjacencyList.containsKey(word1) ? "word2" : "word1") + " in the graph!";
//        }
        boolean word1Exists = adjacencyList.containsKey(word1);
        boolean word2Exists = adjacencyList.containsKey(word2);

        if (!word1Exists || !word2Exists) {
            if (!word1Exists && !word2Exists) {
                return "Neither " + word1 + " nor " + word2 + " is in the graph!";
            } else if (!word1Exists) {
                return "No " + word1 + " in the graph!";
            } else {
                return "No " + word2 + " in the graph!";
            }
        }

        List<String> bridgeWords = new ArrayList<>();
        for (String neighbor : adjacencyList.get(word1)) {
            if (adjacencyList.get(neighbor).contains(word2)) {
                bridgeWords.add(neighbor);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            StringBuilder result = new StringBuilder("The bridge words from " + word1 + " to " + word2 + " are: ");
            for (int i = 0; i < bridgeWords.size(); i++) {
                result.append(bridgeWords.get(i));
                if (i < bridgeWords.size() - 1) {
                    result.append(", ");
                }
            }
            return result.toString();
        }
    }

    public String generateNewText(String inputText) {
        String[] words = inputText.split("\\s+");
        List<String> newTextWords = new ArrayList<>();

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i].toLowerCase(); // 添加toLowerCase()确保一致性
            String word2 = words[i + 1].toLowerCase(); // 添加toLowerCase()确保一致性

            newTextWords.add(word1);
            String bridgeWordsResult = queryBridgeWords(word1, word2);

            // 检查是否找到了桥接词
            if (bridgeWordsResult.startsWith("The bridge words from")) {
                try {
                    // 计算前缀的长度
                    String prefix = "The bridge words from " + word1 + " to " + word2 + " are: ";

                    // 确保bridgeWordsResult的长度大于前缀长度再进行substring操作
                    if (bridgeWordsResult.length() > prefix.length()) {
                        String bridgeWordsPart = bridgeWordsResult.substring(prefix.length());

                        // 分割桥接词列表并随机选择一个
                        List<String> bridgeWordList = Arrays.asList(bridgeWordsPart.split(", "));
                        Random random = new Random();
                        String bridgeWord = bridgeWordList.get(random.nextInt(bridgeWordList.size()));

                        newTextWords.add(bridgeWord);
                    }
                } catch (Exception e) {
                    // 处理任何可能的异常，如索引越界
                    System.err.println("Error processing bridge words: " + e.getMessage());
                }
            }
        }

        // 添加最后一个词
        if (words.length > 0) {
            newTextWords.add(words[words.length - 1].toLowerCase());
        }

        return String.join(" ", newTextWords);
    }

    public String calcShortestPath(String word1, String word2) {
        // Check if word1 and word2 are in the graph
        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
            return "No " + (adjacencyList.containsKey(word1) ? "word2" : "word1") + " in the graph!";
        }

        // Initialize a queue for breadth-first search
        Queue<String> queue = new LinkedList<>();
        // Initialize a map to track visited nodes
        Map<String, Boolean> visited = new HashMap<>();
        // Initialize a map to track the shortest path length to each node
        Map<String, Integer> shortestPathLength = new HashMap<>();
        // Initialize a map to track the predecessors for each node in the shortest path
        Map<String, String> predecessors = new HashMap<>();

        // Initialize the shortest path length for all nodes to be infinity
        for (String node : adjacencyList.keySet()) {
            shortestPathLength.put(node, Integer.MAX_VALUE);
            visited.put(node, false);
        }

        // Start from word1
        queue.offer(word1);
        visited.put(word1, true);
        shortestPathLength.put(word1, 0);

        // Perform breadth-first search
        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            List<String> neighbors = adjacencyList.get(currentWord);
            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.getOrDefault(neighbor, false)) {
                        visited.put(neighbor, true);
                        int weight = edgeWeights.get(currentWord + " -> " + neighbor);
                        shortestPathLength.put(neighbor, shortestPathLength.get(currentWord) + weight);
                        predecessors.put(neighbor, currentWord);
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Retrieve the shortest paths from word1 to word2
        List<List<String>> shortestPaths = new ArrayList<>();
        int shortestLength = shortestPathLength.get(word2);
        if (shortestLength == Integer.MAX_VALUE) {
            return "No shortest path from " + word1 + " to " + word2 + "!";
        } else {
            List<String> path = new ArrayList<>();
            String currentWord = word2;
            while (currentWord != null) {
                path.add(0, currentWord);
                currentWord = predecessors.get(currentWord);
            }
            shortestPaths.add(path);
        }

        // Output the shortest paths
        StringBuilder result = new StringBuilder();
        result.append("The shortest path(s) from " + word1 + " to " + word2 + " with length " + shortestLength + " are:\n");
        for (List<String> path : shortestPaths) {
            result.append(String.join(" -> ", path)).append("\n");
        }
        return result.toString();
    }

    public void printShortestDistancesFromWord(String word) {
        // 遍历每个单词，并计算其与给定单词之间的最短距离
        for (String otherWord : adjacencyList.keySet()) {
            if (!otherWord.equals(word)) { // 跳过给定单词本身
                // 调用 calcShortestPath 计算最短路径，并打印结果
                String shortestPathResult = calcShortestPath(word, otherWord);
                System.out.println(shortestPathResult);

            }
        }
    }


    public String randomWalk() {
        //456
        // Randomly select a starting node
        Random random = new Random();
        List<String> nodes = new ArrayList<>(adjacencyList.keySet());
        String currentNode = nodes.get(random.nextInt(nodes.size()));

        StringBuilder result = new StringBuilder();
        result.append(currentNode).append(" ");

        Set<String> visitedEdges = new HashSet<>();

        while (adjacencyList.containsKey(currentNode) && !adjacencyList.get(currentNode).isEmpty()) {
            // Randomly choose the next node
            List<String> neighbors = adjacencyList.get(currentNode);
            String nextNode = neighbors.get(random.nextInt(neighbors.size()));

            // Check if the edge has been visited before
            String edge = currentNode + " -> " + nextNode;
            if (visitedEdges.contains(edge)) {
                result.append(nextNode).append(" ");
                break;
            }

            // Append the next node to the result
            result.append(nextNode).append(" ");

            // Mark the edge as visited
            visitedEdges.add(edge);

            // Move to the next node
            currentNode = nextNode;

            // Check if the current node has no outgoing edges
            if (!adjacencyList.containsKey(currentNode) || adjacencyList.get(currentNode).isEmpty()) {
                break;
            }
        }

        return result.toString().trim();
    }

    public void writeToFile(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath,true)) {
//            writer.write(content);
            writer.write(content + System.lineSeparator());
            System.out.println("Random walk result has been written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing random walk result to file: " + e.getMessage());
        }
    }

    public double calculatePageRank(String word, int maxIterations, double dampingFactor) {
        Map<String, Double> pageRank = new HashMap<>();
        int numNodes = adjacencyList.size();

        // 初始化PageRank值
        for (String node : adjacencyList.keySet()) {
            pageRank.put(node, 1.0 / numNodes);
        }

        for (int iter = 0; iter < maxIterations; iter++) {
            Map<String, Double> newPageRank = new HashMap<>();
            double deadEndPR = 0.0; // 收集所有dead ends的PR值

            // 首先计算dead ends贡献的PR值
            for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
                String fromNode = entry.getKey();
                List<String> neighbors = entry.getValue();
                if (neighbors.isEmpty()) {
                    deadEndPR += pageRank.get(fromNode);
                }
            }

            // 将dead ends的PR值均分给所有节点
            double deadEndContribution = deadEndPR / numNodes;

            // 计算每个节点的新PR值
            for (String node : adjacencyList.keySet()) {
                double sum = 0.0;
                for (Map.Entry<String, List<String>> entry : adjacencyList.entrySet()) {
                    String fromNode = entry.getKey();
                    List<String> neighbors = entry.getValue();
                    if (neighbors.contains(node)) {
                        int outDegree = neighbors.size();
                        sum += pageRank.get(fromNode) / outDegree;
                    }
                }
                // 加上dead ends的贡献
                newPageRank.put(node, (1 - dampingFactor) / numNodes + dampingFactor * (sum + deadEndContribution));
            }
            pageRank = newPageRank;
        }

        return pageRank.getOrDefault(word, 0.0);
    }
}
