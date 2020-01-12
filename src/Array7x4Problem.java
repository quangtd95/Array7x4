import java.util.*;

public class Array7x4Problem {
    final static float d = 10;
    final static int maxNbOfTimesCanTry = 100;
    float deltaX = 0;
    float deltaY = 0;

    List<Point> input;
    Point[][] result;

    TreeMap<Float, TreeSet<Point>> pointsInSameRowMap;
    TreeMap<Float, TreeSet<Point>> pointsInSameColMap;

    TreeMap<Float, Float> averagePositionRowMap;
    TreeMap<Float, Float> averagePositionColumnMap;

    Array7x4Problem(List<Point> input) {
        this.input = input;
        pointsInSameRowMap = new TreeMap<>();
        pointsInSameColMap = new TreeMap<>();
        averagePositionRowMap = new TreeMap<>();
        averagePositionColumnMap = new TreeMap<>();
        result = new Point[7][4];
    }

    public static void main(String[] args) {
        List<Point> listPointInput = prepareInput3();
        Array7x4Problem problem = new Array7x4Problem(listPointInput);
        boolean isSuccess = false;
        int numOfTimeProcess = 0;
        while (!isSuccess) {
            numOfTimeProcess++;
            System.out.println("resolving... " + numOfTimeProcess + " time(s)!");
            problem.doPosition();
            if (problem.result != null && problem.checkResultAcceptable()) {
                problem.printArray();
                isSuccess = true;
            } else {
                if (problem.isMissingRowCol(problem.pointsInSameRowMap, SET_TYPE.ROW)) {
                    problem.deltaY += d;
                }
                if (problem.isMissingRowCol(problem.pointsInSameColMap, SET_TYPE.COLUMN)) {
                    problem.deltaX += d;
                }

                if (numOfTimeProcess > maxNbOfTimesCanTry) {
                    System.out.println("can't resolve this problem");
                    break;
                }
            }
        }
    }

    private static List<Point> prepareInput() {
        List<Point> listPoint = new ArrayList<>();
        listPoint.add(new Point((215.174f), (1049.901f)));
        listPoint.add(new Point((87.717f), (1048.888f)));
        listPoint.add(new Point((506.637f), (928.163f)));
        listPoint.add(new Point((90.285f), (923.049f)));
        listPoint.add(new Point((382.198f), (800.891f)));
        listPoint.add(new Point((92.075f), (797.79f)));
        listPoint.add(new Point((510.023f), (677.672f)));
        listPoint.add(new Point((220.893f), (674.18f)));
        listPoint.add(new Point((94.183f), (672.253f)));
        listPoint.add(new Point((222.589f), (548.942f)));
        listPoint.add(new Point((95.619f), (546.827f)));
        listPoint.add(new Point((515.478f), (425.848f)));
        listPoint.add(new Point((517.33f), (299.111f)));
        listPoint.add(new Point((100.92f), (293.974f)));
        return listPoint;
    }

    private static List<Point> prepareInput2() {
        List<Point> listPoint = new ArrayList<>();
        listPoint.add(new Point((194.08156575395296f), (964.4136457128164f)));
        listPoint.add(new Point((78.77138303523995f), (966.0709521279734f)));
        listPoint.add(new Point((77.50167031098152f), (850.767583819242f)));
        listPoint.add(new Point((457.81744235625956f), (845.717767063531f)));
        listPoint.add(new Point((75.94603775853776f), (735.0707972582973f)));
        listPoint.add(new Point((192.03572567783095f), (618.3061492114124f)));
        listPoint.add(new Point((73.7317786335758f), (618.5159392383398f)));
        listPoint.add(new Point((455.86884005327516f), (615.3735016345804f)));
        listPoint.add(new Point((73.38489055167207f), (503.6159299005612f)));
        listPoint.add(new Point((189.36872338318312f), (502.3841074906738f)));
        listPoint.add(new Point((338.84810352521199f), (382.0987059348505f)));
        listPoint.add(new Point((454.8481035252119f), (382.9987059348505f)));
        listPoint.add(new Point((454.74391546501863f), (267.09056229495684f)));
        return listPoint;
    }

    private static List<Point> prepareInput3() {
        List<Point> listPoint = new ArrayList<>();
        listPoint.add(new Point((194.08156575395296f), (964.4136457128164f)));
        listPoint.add(new Point((78.77138303523995f), (966.0709521279734f)));
        listPoint.add(new Point((75.94603775853776f), (735.0707972582973f)));
        listPoint.add(new Point((192.03572567783095f), (618.3061492114124f)));
        listPoint.add(new Point((73.7317786335758f), (618.5159392383398f)));
        listPoint.add(new Point((455.86884005327516f), (615.3735016345804f)));
        listPoint.add(new Point((73.38489055167207f), (503.6159299005612f)));
        listPoint.add(new Point((189.36872338318312f), (502.3841074906738f)));
        listPoint.add(new Point((454.8481035252119f), (382.9987059348505f)));
        listPoint.add(new Point((454.74391546501863f), (267.09056229495684f)));
        return listPoint;
    }

    private static void putPointIntoSet(TreeMap<Float, TreeSet<Point>> setTreeMap, Point point, SET_TYPE type, float deltaX, float deltaY) {
        Set<Float> keySet = setTreeMap.keySet();
        for (Float key : keySet) {
            Float[] keys = setTreeMap.get(key).stream().map(point1 -> {
                if (type == SET_TYPE.ROW) {
                    return point1.y;
                } else {
                    return point1.x;
                }
            }).toArray(Float[]::new);
            boolean condition = (type == SET_TYPE.ROW) ? point.sameRow(keys, deltaY) : point.sameColumn(keys, deltaX);
            if (condition) {
                setTreeMap.get(key).add(point);
                return;
            }
        }
        TreeSet<Point> points;
        if (type == SET_TYPE.ROW) {
            points = new TreeSet<>((o1, o2) -> (int) (o1.x - o2.x));
        } else {
            points = new TreeSet<>((o1, o2) -> (int) (o1.y - o2.y));
        }
        points.add(point);
        float key = type == SET_TYPE.ROW ? point.y : point.x;
        setTreeMap.put(key, points);
    }

    private static boolean isIncludedInSet(TreeSet<Point> set, Point point) {
        for (Point p : set) {
            if (p.x == point.x && p.y == point.y) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDuplex(float f, float unit, float delta) {
        for (int i = 0; i < 7; i++) {
            boolean isDuplex = (unit * i + delta) >= f && (unit * i - delta) <= f;
            if (isDuplex) return true;
        }
        return false;
    }

    private void printArray() {
        for (Point[] points : result) {
            for (int j = 0; j < result[0].length; j++) {
                if (points[j] == null) {
                    System.out.print("(---,---)");
                } else {
                    System.out.print(points[j]);
                }

                if (j != result[0].length - 1) {
                    System.out.print("          ");
                }
            }
            System.out.println();
            System.out.println();
        }
        System.out.println("deltaX = " + deltaX);
        System.out.println("deltaY = " + deltaY);
    }

    private boolean isMissingRowCol(TreeMap<Float, TreeSet<Point>> treeSetTreeMap, SET_TYPE type) {
        if (type == SET_TYPE.ROW) {
            return treeSetTreeMap.keySet().size() != 7;
        } else {
            return treeSetTreeMap.keySet().size() != 4;
        }
    }

    private boolean checkResultAcceptable() {
        int numInput = input.size();
        int numFilledCell = 0;
        for (Point[] points : result) {
            for (int i = 0; i < result[0].length; i++) {
                if (points[i] != null) numFilledCell++;
            }
        }
        return numInput == numFilledCell;
    }

    private Point process(float averageDistanceOneRow, float averageDistanceOneColumn,
                          int i, int j) {
        Float rowKey = getKeyByIndex(pointsInSameRowMap, i, SET_TYPE.ROW, averageDistanceOneRow);
        Float colKey = getKeyByIndex(pointsInSameColMap, j, SET_TYPE.COLUMN, averageDistanceOneColumn);
        if (rowKey == null) {
            return null;
        }
        if (colKey == null) {
            return null;
        }
        TreeSet<Point> row = pointsInSameRowMap.get(rowKey);
        TreeSet<Point> col = pointsInSameColMap.get(colKey);
        for (Point point : row) {
            if (isIncludedInSet(col, point)) {
                return point;
            }
        }
        return null;
    }

    private Float getKeyByIndex(TreeMap<Float, TreeSet<Point>> treeMap, int index, SET_TYPE type, float unit) {
        float firstKey = treeMap.firstKey();
        float maxPoint = unit * index + firstKey + (type == SET_TYPE.ROW ? deltaY : deltaX);
        float minPoint = unit * index + firstKey - (type == SET_TYPE.ROW ? deltaY : deltaX);
        for (float key : treeMap.keySet()) {
            if (key <= maxPoint && key >= minPoint) {
                return key;
            }
        }
        return null;
    }

    private boolean isHeadAndTailIncluded(SET_TYPE set_type) {
        TreeMap<Float, Float> averagePositionMap;
        TreeMap<Float, TreeSet<Point>> samePointMap;
        int numRowCol;
        float delta;
        if (set_type == SET_TYPE.ROW) {
            averagePositionMap = averagePositionRowMap;
            samePointMap = pointsInSameRowMap;
            numRowCol = 6;
            delta = deltaY;
        } else {
            averagePositionMap = averagePositionColumnMap;
            samePointMap = pointsInSameColMap;
            numRowCol = 3;
            delta = deltaX;
        }
        float firstRowAverage = averagePositionMap.get(samePointMap.firstKey());
        float lastRowAverage = averagePositionMap.get(samePointMap.lastKey());
        float averageDistanceOneRow = (lastRowAverage - firstRowAverage) / numRowCol;
        for (Float row : averagePositionMap.keySet()) {
            if (!isDuplex(row - firstRowAverage, averageDistanceOneRow, delta)) {
                System.out.println("missing head of tail row");
                return false;
            }
        }
        return true;
    }

    private void resetResult() {
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = null;
            }
        }
    }

    private void doPosition() {

        pointsInSameRowMap.clear();
        pointsInSameColMap.clear();
        averagePositionRowMap.clear();
        averagePositionColumnMap.clear();
        resetResult();
        for (Point point : input) {
            putPointIntoSet(pointsInSameRowMap, point, SET_TYPE.ROW, deltaX, deltaY);
            putPointIntoSet(pointsInSameColMap, point, SET_TYPE.COLUMN, deltaX, deltaY);
        }

        pointsInSameRowMap.keySet().forEach(key -> {
            TreeSet<Point> row = pointsInSameRowMap.get(key);
            float a = (float) row.stream().mapToDouble(p -> p.y).average().orElse(0);
            averagePositionRowMap.put(key, a);
        });
        pointsInSameColMap.keySet().forEach(key -> {
            TreeSet<Point> row = pointsInSameColMap.get(key);
            float a = (float) row.stream().mapToDouble(p -> p.x).average().orElse(0);
            averagePositionColumnMap.put(key, a);
        });
        if (!isHeadAndTailIncluded(SET_TYPE.ROW) || !isHeadAndTailIncluded(SET_TYPE.COLUMN)) {
            return;
        }

        float firstRowAverage = averagePositionRowMap.get(pointsInSameRowMap.firstKey());
        float lastRowAverage = averagePositionRowMap.get(pointsInSameRowMap.lastKey());
        float averageDistanceOneRow = (lastRowAverage - firstRowAverage) / (6);

        float firstColAverage = averagePositionColumnMap.get(pointsInSameColMap.firstKey());
        float lastColAverage = averagePositionColumnMap.get(pointsInSameColMap.lastKey());
        float averageDistanceOneCol = (lastColAverage - firstColAverage) / (3);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = process(averageDistanceOneRow, averageDistanceOneCol, i, j);
            }
        }
    }

    enum SET_TYPE {
        ROW,
        COLUMN
    }

    private static class Point {
        public float x;
        public float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public boolean sameRow(Float[] rows, float deltaY) {
            for (float row : rows) {
                if (y <= row + deltaY && y >= row - deltaY) {
                    return true;
                }
            }
            return false;
        }

        public boolean sameColumn(Float[] cols, float deltaX) {
            for (float col : cols) {
                if (x <= col + deltaX && x >= col - deltaX) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "(" + String.format("%.0f", x) + "," + String.format("%.0f", y) + ")";
        }
    }
}

