import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

public class FastCollinearPoints {

    private Map<Double, List<Point>> foundSegments = new HashMap<>(); // All found segments
    private List<LineSegment> segments = new ArrayList<>(); // Valid segments to avoid duplicity

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

        for (Point startPoint : points) {
            Arrays.sort(aux, startPoint.slopeOrder());

            List<Point> slopePoints = new ArrayList<>();
            double slope = 0;
            double previousSlope = Double.NEGATIVE_INFINITY;

            for (int i = 1; i < aux.length; i++) {
                Point comparePoint = aux[i];
                slope = startPoint.slopeTo(comparePoint);
                if (slope == previousSlope) {
                    slopePoints.add(comparePoint);
                } else {
                    if (slopePoints.size() >= 3) {
                        slopePoints.add(startPoint);
                        addSegmentIfNew(slopePoints, previousSlope);
                    }
                    slopePoints.clear();
                    slopePoints.add(comparePoint);
                }
                previousSlope = slope;
            }

            if (slopePoints.size() >= 3) {
                slopePoints.add(startPoint);
                addSegmentIfNew(slopePoints, slope);
            }
        }
    }

    private void addSegmentIfNew(List<Point> slopePoints, double slope) {
        List<Point> endPoints = foundSegments.get(slope);
        Collections.sort(slopePoints);

        Point startPoint = slopePoints.get(0);
        Point endPoint = slopePoints.get(slopePoints.size() - 1);

        if (endPoints == null) {
            endPoints = new ArrayList<>();
            endPoints.add(endPoint);
            foundSegments.put(slope, endPoints);
            segments.add(new LineSegment(startPoint, endPoint));
        } else {
            for (Point currentEndPoint : endPoints) {
                if (currentEndPoint.compareTo(endPoint) == 0) {
                    return;
                }
            }
            endPoints.add(endPoint);
            segments.add(new LineSegment(startPoint, endPoint));
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
