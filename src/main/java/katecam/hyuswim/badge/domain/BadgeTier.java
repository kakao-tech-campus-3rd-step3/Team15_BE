package katecam.hyuswim.badge.domain;

import lombok.Getter;

@Getter
public enum BadgeTier {
    TIER1(1),
    TIER2(7),
    TIER3(15),
    TIER4(30);

    private final int threshold;
    BadgeTier(int threshold) { this.threshold = threshold; }
}
