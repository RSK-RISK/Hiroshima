package me.rsk.hiroshima.gasda.command.syntax;

public interface SyntaxParser {
    String getChunk(SyntaxChunk[] chunks, SyntaxChunk thisChunk, String[] values, String chunkValue);
}