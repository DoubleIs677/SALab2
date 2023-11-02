package com.mycode.newTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class KWICSoftware extends JFrame {
    private JTextArea outputTextArea;

    public KWICSoftware() {
        // 设置窗口标题
        setTitle("KWIC Software");
        // 设置窗口大小
        setSize(600, 400);
        // 设置窗口关闭按钮行为
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        // 创建输出文本区域
        outputTextArea = new JTextArea();
        // 设置文本区域不可编辑
        outputTextArea.setEditable(false);
        // 创建滚动面板，并将文本区域添加到其中
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        // 添加滚动面板到主面板的中心区域
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        // 创建处理按钮
        JButton processButton = new JButton("Process");
        // 注册按钮点击事件监听器
        processButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理输入文件并显示结果
                processInputFile();
            }
        });
        // 将处理按钮添加到按钮面板
        buttonPanel.add(processButton);

        // 将按钮面板添加到主面板的底部区域
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 将主面板设置为内容面板
        setContentPane(mainPanel);
    }

    private void processInputFile() {
        try {
            // 读取输入文件
            BufferedReader reader = new BufferedReader(new FileReader("D:\\small project\\IdeaProjects\\input.txt"));
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            // 根据选择的方法进行处理
            int selectedMethod = JOptionPane.showOptionDialog(this, "Select method", "Method Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Main Program - Subroutine", "Object-Oriented", "Event System", "Pipe-Filter"}, null);
            String result;
            switch (selectedMethod) {
                case 0:
                    result = processMainProgramSubroutine(lines);
                    break;
                case 1:
                    result = processObjectOriented(lines);
                    break;
                case 2:
                    result = processEventSystem(lines);
                    break;
                case 3:
                    result = processPipeFilter(lines);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid method selection");
            }

            // 清空输出文本区域并显示结果
            outputTextArea.setText("");
            outputTextArea.append(result);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading input file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //主程序-子程序
    private String processMainProgramSubroutine(List<String> lines) {
        List<String> allShiftedStrings = shiftStrings(lines);
        List<String> sortedAndGroupedStrings = sortAndGroupStrings(allShiftedStrings, lines.size());
        return String.join("\n", sortedAndGroupedStrings);
    }

    private List<String> shiftStrings(List<String> lines) {
        List<String> allShiftedStrings = new ArrayList<>();
        for (String line : lines) {
            String[] words = line.split(" ");
            for (int i = 0; i < words.length; i++) {
                String[] shiftedWords = new String[words.length];
                for (int j = 0; j < words.length; j++) {
                    int start = (i + j) % words.length;
                    shiftedWords[j] = words[start];
                }
                String shiftedString = String.join(" ", shiftedWords);
                allShiftedStrings.add(shiftedString);
            }
        }
        return allShiftedStrings;
    }

    private List<String> sortAndGroupStrings(List<String> strings, int numOfLines) {
        Collections.sort(strings, String.CASE_INSENSITIVE_ORDER);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < strings.size(); i += numOfLines) {
            int endIndex = Math.min(i + numOfLines, strings.size());
            List<String> lineGroup = strings.subList(i, endIndex);
            StringBuilder sb = new StringBuilder();
            for (String line : lineGroup) {
                sb.append(line).append("\n");
            }
            result.add(sb.toString().trim());
        }
        return result;
    }

    //    面向对象
    private String processObjectOriented(List<String> lines) {
        List<Line> lineList = new ArrayList<>();
        for (String line : lines) {
            lineList.add(new Line(line));
        }

        KWICIndexSystem indexSystem = new KWICIndexSystem(lineList);
        List<String> sortedLines = indexSystem.getSortedLines();

        return String.join("\n", sortedLines);
    }

    public class Line {
        private String content;

        public Line(String content) {
            this.content = content;
        }

        public List<String> getAllShiftedLines() {
            List<String> shiftedLines = new ArrayList<>();
            String[] words = content.split(" ");
            for (int i = 0; i < words.length; i++) {
                String shiftedLine = String.join(" ", words);
                shiftedLines.add(shiftedLine);
                String firstWord = words[0];
                System.arraycopy(words, 1, words, 0, words.length - 1);
                words[words.length - 1] = firstWord;
            }
            return shiftedLines;
        }
    }

    public class KWICIndexSystem {
        private List<Line> lines;

        public KWICIndexSystem(List<Line> lines) {
            this.lines = lines;
        }

        public List<String> getSortedLines() {
            List<String> shiftedLines = new ArrayList<>();
            for (Line line : lines) {
                shiftedLines.addAll(line.getAllShiftedLines());
            }

            Collections.sort(shiftedLines, String.CASE_INSENSITIVE_ORDER);

            return shiftedLines;
        }
    }

    //管道-过滤器
    private String processPipeFilter(List<String> lines) {
        CircularShift circularShift = new CircularShift(lines);
        List<String> shiftedLines = circularShift.process();

        Alphabetizer alphabetizer = new Alphabetizer(shiftedLines);
        List<String> result = alphabetizer.process();

        StringBuilder finalResult = new StringBuilder();
        for (String str : result) {
            finalResult.append(str.trim()).append("\n");
        }

        return finalResult.toString().trim();
    }

    class CircularShift {
        private final List<String> lines;

        public CircularShift(List<String> lines) {
            this.lines = lines;
        }

        public List<String> process() {
            List<String> shiftedLines = new ArrayList<>();

            for (String line : lines) {
                String[] words = line.split("\\s+");
                int wordCount = words.length;

                for (int i = 0; i < wordCount; i++) {
                    StringBuilder shiftedLine = new StringBuilder();
                    for (int j = 0; j < wordCount; j++) {
                        shiftedLine.append(words[(i + j) % wordCount]).append(" ");
                    }
                    shiftedLines.add(shiftedLine.toString().trim());
                }
            }

            return shiftedLines;
        }
    }

    class Alphabetizer {
        private final List<String> lines;

        public Alphabetizer(List<String> lines) {
            this.lines = lines;
        }

        public List<String> process() {
            List<String> sortedLines = new ArrayList<>(lines);
            Collections.sort(sortedLines, String.CASE_INSENSITIVE_ORDER);
            return sortedLines;
        }
    }


    //事件系统
    //输入模块
    public List<String> readInput(String input) {
        List<String> lines = new ArrayList<String>();
        String[] parts = input.split("\n");
        for (String part : parts) {
            lines.add(part);
        }
        return lines;
    }

    // 移位处理模块
    public List<String> generateShiftedLines(String line) {
        String[] words = line.split(" ");
        List<String> shiftedLines = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int j = i; j < words.length + i; j++) {
                stringBuilder.append(words[j % words.length]);
                stringBuilder.append(" ");
            }

            shiftedLines.add(stringBuilder.toString().trim());
        }

        return shiftedLines;
    }

    // 输出模块
    public String generateOutput(List<String> lines) {
        Collections.sort(lines);
        StringBuilder output = new StringBuilder();
        for (String line : lines) {
            output.append(line);
            output.append("\n");
        }
        return output.toString().trim();
    }

    // 事件处理模块
    public String processEventSystem(List<String> lines) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            result.addAll(generateShiftedLines(line.toLowerCase()));
        }
        return generateOutput(result);
    }


        public static void main(String[] args) {
            // 创建并显示软件界面
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    KWICSoftware software = new KWICSoftware();
                    software.setVisible(true);
                }
            });
        }
    }
