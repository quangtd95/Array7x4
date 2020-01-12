import java.util.*;

public class Array7x4 {
    private final static float deltaX = 30;
    private final static float deltaY = 10;

    public static void main(String[] args) {
        List<Point> listPointInput = prepareInput3();

        TreeMap<Float, TreeSet<Point>> pointsInSameRowMap = new TreeMap<>();
        TreeMap<Float, TreeSet<Point>> pointsInSameColMap = new TreeMap<>();
        for (Point point : listPointInput) {
            putPointIntoSet(pointsInSameRowMap, point, SET_TYPE.ROW);
            putPointIntoSet(pointsInSameColMap, point, SET_TYPE.COLUMN);
        }
        Point[][] result = new Point[7][4];
//        setMissingPoint(result);
        int numRows = pointsInSameRowMap.keySet().size();
        int numCols = pointsInSameColMap.keySet().size();
        //toa do trung binh tren tung` hang`, tung cot.
        TreeMap<Float, Float> averagePositionRowMap = new TreeMap<>();
        TreeMap<Float, Float> averagePositionColumnMap = new TreeMap<>();
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
        if (!isHeadAndTailIncluded(averagePositionRowMap, pointsInSameRowMap, SET_TYPE.ROW)) {
            return;
        }

        if (!isHeadAndTailIncluded(averagePositionColumnMap, pointsInSameColMap, SET_TYPE.COLUMN)) {
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
                result[i][j] = process(pointsInSameRowMap, pointsInSameColMap, averageDistanceOneRow, averageDistanceOneCol, i, j);
            }
        }
        printArray(result);
    }

    private static boolean isHeadAndTailIncluded(TreeMap<Float, Float> averagePositionMap, TreeMap<Float, TreeSet<Point>> samePointMap, SET_TYPE set_type) {
        float firstRowAverage = averagePositionMap.get(samePointMap.firstKey());
        float lastRowAverage = averagePositionMap.get(samePointMap.lastKey());
        float averageDistanceOneRow = (lastRowAverage - firstRowAverage) / (set_type == SET_TYPE.ROW ? 6 : 3);
        for (Float row : averagePositionMap.keySet()) {
            if (!isDuplex(row - firstRowAverage, averageDistanceOneRow, (set_type == SET_TYPE.ROW ? deltaY : deltaX))) {
                System.out.println("missing head of tail row");
                return false;
            }
        }
        return true;
    }

    private static Point process(TreeMap<Float, TreeSet<Point>> sameRowSetMap,
                                 TreeMap<Float, TreeSet<Point>> sameColumnSetMap,
                                 float averageDistanceOneRow, float averageDistanceOneColumn,
                                 int i, int j) {
        Float rowKey = getKeyByIndex(sameRowSetMap, i, SET_TYPE.ROW, averageDistanceOneRow);
        Float colKey = getKeyByIndex(sameColumnSetMap, j, SET_TYPE.COLUMN, averageDistanceOneColumn);
        if (rowKey == null) {
            return null;
        }
        if (colKey == null) {
            return null;
        }
        TreeSet<Point> row = sameRowSetMap.get(rowKey);
        TreeSet<Point> col = sameColumnSetMap.get(colKey);
        for (Point point : row) {
            if (isIncludedInSet(col, point)) {
                return point;
            }
        }
        return null;
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


    private static void putPointIntoSet(TreeMap<Float, TreeSet<Point>> setTreeMap, Point point, SET_TYPE type) {
        Set<Float> keySet = setTreeMap.keySet();
        for (Float key : keySet) {
            Float[] keys = setTreeMap.get(key).stream().map(point1 -> {
                if (type == SET_TYPE.ROW) {
                    return point1.y;
                } else {
                    return point1.x;
                }
            }).toArray(Float[]::new);
            boolean condition = (type == SET_TYPE.ROW) ? point.sameRow(keys) : point.sameColumn(keys);
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
//            if (isDuplex) {
//                System.out.println("---" + (i + 1) + ": " + (unit * i + delta) + " >= " + f + " <= " + (unit * i - delta));
//            }
            if (isDuplex) return true;
        }
        return false;
    }

    private static Float getKeyByIndex(TreeMap<Float, TreeSet<Point>> treeMap, int index, SET_TYPE type, float unit) {
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

    private static void printArray(Point[][] arr) {
        for (Point[] points : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                if (points[j] == null) {
                    System.out.print("(---,---)");
                } else {
                    System.out.print(points[j]);
                }

                if (j != arr[0].length - 1) {
                    System.out.print("          ");
                }
            }
            System.out.println();
            System.out.println();
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

        public boolean sameRow(float row) {
            return y <= row + deltaY && y >= row - deltaY;
        }

        public boolean sameRow(Float[] rows) {
            for (float row : rows) {
                if (y <= row + deltaY && y >= row - deltaY) {
                    return true;
                }
            }
            return false;
        }


        public boolean sameColumn(float col) {
            return x <= col + deltaX && x >= col - deltaX;
        }

        public boolean sameColumn(Float[] cols) {
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

