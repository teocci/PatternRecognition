import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteCollinearPoints {

    private Map<Double, List<Point>> segmentsEnds = new HashMap<>(); // Map all the found segments by the slope value
    private List<LineSegment> segments = new ArrayList<>();
    private List<Point> segment = new ArrayList<>();
    private int count;
    private double slope;
    private boolean repeatedPointVerified;

    /**
     * Created by teocci on 10/25/16.
     *
     * Brute force Algorithm finds all line segments containing 4 points. Examines 4 points at a time
     * and checks whether they all lie on the same line segment, getting all such line segments.
     *
     * Throw a java.lang.NullPointerException either the argument to the constructor is null
     * or if any point in the array is null.
     * Throw a java.lang.IllegalArgumentException if the argument to the constructor contains a repeated point.
     *
     * @param points an array of points
     */
    public BruteCollinearPoints(Point[] points) {
        checkNullPointArray(points);
        count = points.length;

        // copy points to an aux[]
        Point[] aux = new Point[count];
        int index = 0;
        for (Point point : points) {
            checkPointNull(point);
            aux[index++] = point;
        }

        Arrays.sort(aux);

        repeatedPointVerified = false;
        for (int p = 0; p < count - 3; p++) {
            for (int q = p + 1; q < count - 2; q++) {
                for (int r = q + 1; r < count - 1; r++) {
                    for (int s = r + 1; s < count; s++) {
                        segment.clear();
                        if (checkEqualSlopes(aux, p, q, r, s)) {
                            segment.add(aux[p]);
                            segment.add(aux[q]);
                            segment.add(aux[r]);
                            segment.add(aux[s]);

                            addNewSegment();
                        }
                    }
                    if (!repeatedPointVerified) repeatedPointVerified = true;
                }
            }
        }
    }

    /**
     * Returns the number of line segments that contain 4 collinear points
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * Returns the line segments that contain 4 collinear points
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    /**
     * Throws a java.lang.NullPointerException if an array is null
     *
     * @param points an array of points
     */
    private void checkNullPointArray(Point[] points) {
        if (points == null)
            throw new NullPointerException("The argument is null");
    }

    /**
     * Throws a java.lang.NullPointerException if a point is null
     *
     * @param point an array of points
     */
    private void checkPointNull(Point point) {
        if (point == null)
            throw new NullPointerException("A point in the array is null");
    }

    /**
     * Throws a java.lang.IllegalArgumentException if q is a repeated point.
     *
     * @param p a point
     * @param q a point
     */
    private void checkDuplicatedPoints(Point p, Point q) {
        if (p.compareTo(q) == 0) {
            throw new IllegalArgumentException("Duplicated entries in given points");
        }
        segment.add(q);
    }

    /**
     * To verify whether the 4 points p, q, r, and s are collinear,
     * check whether the three slopes between p and q,
     * between p and r, and between p and s are all equal.
     *
     * @param points an array of points
     * @param p a point that belongs to the points array
     * @param q a point that belongs to the points array
     * @param r a point that belongs to the points array
     * @param s a point that belongs to the points array
     * @return true if the 4 points p, q, r, and s are collinear
     */
    private boolean checkEqualSlopes(Point[] points, int p, int q, int r, int s) {
        slope = points[p].slopeTo(points[q]);
        if (!repeatedPointVerified) {
            if (s == 3) {
                checkDuplicatedPoints(points[p], points[q]);
                checkDuplicatedPoints(points[q], points[r]);
                checkDuplicatedPoints(points[r], points[s]);
            } else {
                checkDuplicatedPoints(points[r], points[s]);
            }
        }
        return (slope == points[p].slopeTo(points[r]) && slope == points[p].slopeTo(points[s]));
    }

    /**
     * Adds a segment if its ending point has not been added before
     */
    private void addNewSegment() {
        List<Point> ends = segmentsEnds.get(slope);

        Collections.sort(segment);
        Point p = segment.get(0); // Beginning point of the segment
        Point q = segment.get(segment.size() - 1); // Ending point of the segment

        if (ends != null) {
            for (Point r : ends) {
                if (q.compareTo(r) == 0) {
                    return;
                }
            }
        } else {
            ends  = new ArrayList<>();
        }

        ends.add(q);
        segmentsEnds.put(slope, ends);
        segments.add(new LineSegment(p, q));
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
