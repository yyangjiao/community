package pers.yangjiao.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }

        } catch (IOException e) {

        }
    }

    private void addKeyword(String keyword) {
        TrieNode curNode = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = curNode.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                curNode.addSubNode(c, subNode);
            }
            curNode = subNode;
            if (i == keyword.length() - 1) {
                curNode.setIsEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 原文本
     * @return 过滤后文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        TrieNode curNode = root; // 指针1
        int begin = 0; // 指针2
        int position = 0; // 指针3

        StringBuilder sb = new StringBuilder();
        while (position < text.length()) {
            char c = text.charAt(position);
            // 跳过特殊字符
            if (isSymbol(c)) {
                // 若指针1处于根节点
                if (curNode == root) {
                    // 计入该字符 指针2前移
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            // 检查下级节点
            curNode = curNode.getSubNode(c);
            if (curNode == null) {
                // 以begin开始的子串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向新节点
                curNode = root;
            } else if (curNode.isEnd) {
                // 发现敏感词, 将[begin,position]字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                curNode = root;
            } else {
                // 检查下一个位置
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(char c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 定义前缀树
    private class TrieNode {
        // 结束标识
        private boolean isEnd = false;
        // key-下级字符 value-下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(boolean end) {
            isEnd = end;
        }
    }
}
