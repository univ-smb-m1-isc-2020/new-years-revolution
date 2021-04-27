package net.oups.new_years_revolution.infrastructure.persistence;

public class ResolutionCount {
    private Resolution resolution;
    private Long count;

    public ResolutionCount(Resolution resolution, Long count) {
        this.resolution = resolution;
        this.count = count;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
