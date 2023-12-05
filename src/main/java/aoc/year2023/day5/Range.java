package aoc.year2023.day5;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Range {

    private long sourceBegin;
    private long targetBegin;
    private long rangeLength;

    public boolean sourceInRange(long testSource) {
        return sourceBegin <= testSource && testSource <= (sourceBegin + rangeLength);
    }

    public boolean targetInRange(long testTarget) {
        return targetBegin <= testTarget && testTarget <= (targetBegin + rangeLength);
    }

    public long getTarget(long source) {
        long offset = source - sourceBegin;
        return targetBegin + offset;
    }

    public long getSource(long target) {
        long offset = target - targetBegin;
        return sourceBegin + offset;
    }
}
