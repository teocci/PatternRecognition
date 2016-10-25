import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;


    /**
     * Created by teocci on 10/25/16.
     *
     * Brute force Algorithm finds all line segments containing 4 points.
     *
     * Examines 4 points at a time and checks whether they all lie on the same line segment,
     * returning all such line segments.
     * To verify whether the 4 points p, q, r, and s are collinear,
     * check whether the three slopes between p and q,
     * between p and r, and between p and s are all equal.
     *
     * @param points an array of points
     */
    public BruteCollinearPoints(Point[] points) {
        ArrayList<LineSegment> foundSegments = new ArrayList<>();

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        checkDuplicatedEntries(pointsCopy);

        for (int p = 0; p < pointsCopy.length - 3; p++) {
            for (int q = p + 1; q < pointsCopy.length - 2; q++) {
                for (int r = q + 1; r < pointsCopy.length - 1; r++) {
                    for (int s = r + 1; s < pointsCopy.length; s++) {
                        if (pointsCopy[p].slopeTo(pointsCopy[q]) == pointsCopy[p].slopeTo(pointsCopy[r]) &&
                                pointsCopy[p].slopeTo(pointsCopy[q]) == pointsCopy[p].slopeTo(pointsCopy[s])) {
                            foundSegments.add(new LineSegment(pointsCopy[p], pointsCopy[s]));
                        }
                    }
                }
            }
        }

        segments = foundSegments.toArray(new LineSegment[foundSegments.size()]);
    }


    /**
     * Returns the number of line segments that contain 4 collinear points
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * Returns the line segments that contain 4 collinear points
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments());
    }

    /**
     * Throws a java.lang.IllegalArgumentException if there is a duplicate point
     *
     * @param points an array of points
     */
    private void checkDuplicatedEntries(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicated entries in given points");
            }
        }
    }
}
