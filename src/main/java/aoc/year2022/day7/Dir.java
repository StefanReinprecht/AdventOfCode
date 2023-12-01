package aoc.year2022.day7;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Dir implements Comparable<Dir>{

    private String name;
    private Dir parentDir;
    private List<File> files = new ArrayList<>();
    private List<Dir> childDirs = new ArrayList<>();

    public Dir(String name) {
        this.name = name;
    }

    public String getPath() {
        StringBuilder builder = new StringBuilder();
        if (parentDir != null) {
            builder.append(parentDir.getPath()).append(":");
        }
        return builder.append(this.name).toString();
    }

    public void addChildDir(Dir dir) {
        childDirs.add(dir);
    }

    public void addFile(File file) {
        files.add(file);
    }

    public int getDiskSpace() {
        int totalSize = 0;
        for (File file : files) {
            totalSize += file.getFileSize();
        }
        for (Dir childDir : childDirs) {
            totalSize += childDir.getDiskSpace();
        }
        return totalSize;
    }

    @Override
    public int compareTo(Dir o) {
        return Integer.compare(this.getDiskSpace(), o.getDiskSpace());
    }
}
