import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class FastCollinearPoints {

    private Map<Double, List<Point>> segmentsEnds = new HashMap<>(); // Map all the found segments by the slope value
    private List<LineSegment> segments = new ArrayList<>(); // Store valid segments to avoid duplicity

    /**
     * Created by teocci on 10/25/16.
     *
     * Brute force
     * FastCollinearPoints algorithm finds all line segments containing 4 points.
     * Throw a java.lang.NullPointerException either the argument to the constructor is null or if any point in the array is null.
     * Throw a java.lang.IllegalArgumentException if the argument to the constructor contains a repeated point.
     *
     * FastCollinearPoints should work properly even if the input has 5 or more collinear points.
     *
     * @param  points the other point
     */
    public FastCollinearPoints(Point[] points) {
        checkNullPointArray(points);
        // copy points to an aux[]
        Point[] aux = new Point[points.length];
        int index = 0;
        for (Point point : points) {
            checkPointNull(point);
            aux[index++] = point;
        }

        checkDuplicatedPoints(aux);

        for (Point p : points) {
            // Moves p as the first element of aux[] and sort aux[] based on the slope value
            Arrays.sort(aux, p.slopeOrder());

            List<Point> segment = new ArrayList<>();
            double slope = 0;
            double previousSlope = Double.NEGATIVE_INFINITY; // if (x0, y0) and (x1, y1) are equal.

            for (Point q : aux) {
                slope = p.slopeTo(q);

                // add points with the same slope to the segment
                if (slope == previousSlope) {
                    segment.add(q);
                } else {
                    // if slope change verify that the segment has at least 3 points
                    if (segment.size() >= 3) {
                        segment.add(p);
                        // line segments added because slope change
                        addNewSegment(segment, previousSlope);
                    }
                    segment.clear();
                    segment.add(q);
                }
                previousSlope = slope;
            }

            // Verifies if the segment has 4 or more points
            if (segment.size() >= 3) {
                segment.add(p);
                addNewSegment(segment, slope);
            }
        }
    }

    /**
     * Returns the number of maximal line segment that contain 4 collinear points
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * Returns an array that includes each maximal line segment containing 4 (or more) points exactly once.
     * For example, if 5 points appear on a line segment in the order p→q→r→s→t,
     * then do not include the subsegments p→s or q→t.
     *
     * @return a LineSegment array of maximal line segments
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
     * Throws a java.lang.IllegalArgumentException if there is a duplicate point
     *
     * @param points an array of points
     */
    private void checkDuplicatedPoints(Point[] points) {
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicated entries in given points");
            }
        }
    }

    /**
     * Adds a segment if its ending point has not been added before
     *
     * @param segment a list of point that represents a line segment
     * @param slope the slope value of the line segment points
     */
    private void addNewSegment(List<Point> segment, double slope) {
        List<Point> segmentEnds = segmentsEnds.get(slope);

        Collections.sort(segment);
        Point p = segment.get(0); // Beginning point of the segment
        Point q = segment.get(segment.size() - 1); // Ending point of the segment

        if (segmentEnds == null) {
            segmentEnds = new ArrayList<>();
            segmentEnds.add(q);
            segmentsEnds.put(slope, segmentEnds);
            segments.add(new LineSegment(p, q));
        } else {
            for (Point r : segmentEnds) {
                if (q.compareTo(r) == 0) {
                    return;
                }
            }
            segmentEnds.add(q);
            segments.add(new LineSegment(p, q));
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
