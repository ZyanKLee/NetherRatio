package org.doraji.netherratio.util;

/**
 * Coordinate conversion math for Nether/Overworld portal travel.
 *
 * <p>This class is the single source of truth for the ratio and offset
 * calculations, shared by portal travel handling and the coordinate
 * calculator command so the two can never diverge.</p>
 *
 * @author NetherRatio Team
 * @author ZyanKLee (Maintainer)
 * @version 3.0.2
 */
public final class CoordinateMath {

    private CoordinateMath() {
    }

    /**
     * Converts an Overworld coordinate to its Nether equivalent.
     *
     * @param coordinate The Overworld X or Z coordinate
     * @param ratio The Overworld:Nether ratio (e.g. 8 for vanilla behavior)
     * @param offset The configured offset for this axis
     * @return The corresponding Nether coordinate
     */
    public static double toNether(double coordinate, double ratio, double offset) {
        return coordinate / ratio + offset;
    }

    /**
     * Converts a Nether coordinate to its Overworld equivalent.
     *
     * <p>This is the inverse of {@link #toNether(double, double, double)}.</p>
     *
     * @param coordinate The Nether X or Z coordinate
     * @param ratio The Overworld:Nether ratio (e.g. 8 for vanilla behavior)
     * @param offset The configured offset for this axis
     * @return The corresponding Overworld coordinate
     */
    public static double toOverworld(double coordinate, double ratio, double offset) {
        return (coordinate - offset) * ratio;
    }

    /**
     * Clamps a coordinate to the given bounds.
     *
     * @param value The coordinate to clamp
     * @param min The minimum allowed value
     * @param max The maximum allowed value
     * @return The clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
