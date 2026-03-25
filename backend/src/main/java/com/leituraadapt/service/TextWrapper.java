package com.leituraadapt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextWrapper {

    public List<String> wrap(String content, int columnWidth){
        Objects.requireNonNull(content,"content n~ao pode ser nulo");
        if (columnWidth <= 0){
            throw new IllegalArgumentException("ColumnWidth deve ser maior que zero");
        }

        List<String> lines = new ArrayList<>();

        String normalized = content
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .trim();

        if (normalized.isEmpty()){
            return lines;
        }

        String[] paragraphs = normalized.split("\n");

        for(String paragraph : paragraphs){
            if(paragraph.isBlank()){
                lines.add("");
                continue;
            }

            String[] words = paragraph.trim().split("\\s+");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words){
                if(currentLine.length() == 0){
                    currentLine.append(word);
                } else if (currentLine.length() + 1 + word.length()<=columnWidth){
                    currentLine.append(" ").append(word);
                } else {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
            }

            if (currentLine.length() > 0) {
                lines.add(currentLine.toString());
            }

        }
        return lines;
    }
}
