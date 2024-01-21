import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;
    public static final int TYPE_GOAL = 2;
    public static final int TYPE_MUSHROOM = 3;
    public static final int TYPE_WARP = 4;

    private static final String mapImageFiles[] = { "png/SPACE.png", "png/WALL.png", "png/GOAL.png", "png/MUSHROOM.png",
            "png/WARP.png" };

    private Image[] mapImages;
    private ImageView[][] mapImageViews;
    private int[][] maps;
    private int width; // width of the map
    private int height; // height of the map

    MapData(int x, int y) {
        mapImages = new Image[mapImageFiles.length];
        mapImageViews = new ImageView[y][x];
        for (int i = 0; i < mapImageFiles.length; i++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }

        width = x;
        height = y;
        maps = new int[y][x];

        fillMap(MapData.TYPE_WALL);
        digMap(1, 3);
        decideGoal();
        setItems(MapData.TYPE_MUSHROOM, MapData.TYPE_WARP);
        setImageViews();
    }

    // fill two-dimentional arrays with a given number (maps[y][x])
    private void fillMap(int type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = type;
            }
        }
    }

    // dig walls for making roads
    private void digMap(int x, int y) {
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
        int[] tmp;

        for (int i = 0; i < dl.length; i++) {
            int r = (int) (Math.random() * dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int i = 0; i < dl.length; i++) {
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x + dx * 2, y + dy * 2) == MapData.TYPE_WALL) {
                setMap(x + dx, y + dy, MapData.TYPE_SPACE);
                digMap(x + dx * 2, y + dy * 2);
            }
        }
    }

    private void setItems(int... items) {
        for (int item : items) {
            int x, y;
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while (!(x == 1 && y == 1) && getMap(x, y) != MapData.TYPE_SPACE && getMap(x, y) != MapData.TYPE_GOAL);
            setMap(x, y, item);
        }
    }

    // decide goal position
    private void decideGoal() {
        int[] deepestSpace = findDeepestSpaceByDFS(1, 1);
        setMap(deepestSpace[0], deepestSpace[1], MapData.TYPE_GOAL);
    }

    private int[] findDeepestSpaceByDFS(int x, int y) {
        int[] deepestSpace = { x, y };
        int maxDepth = -1;
        int[][] visited = new int[height][width];

        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = 0;
            }
        }

        maxDepth = dfs(x, y, 0, visited, deepestSpace, maxDepth);
        return deepestSpace;
    }

    private int dfs(int current_x, int current_y, int depth, int[][] visited, int[] deepestSpace, int maxDepth) {
        if (visited[current_y][current_x] == 1 || getMap(current_x, current_y) == MapData.TYPE_WALL) {
            return maxDepth;
        }
        visited[current_y][current_x] = 1;
        if (maxDepth < depth) {
            maxDepth = depth;
            deepestSpace[0] = current_x;
            deepestSpace[1] = current_y;
        }
        int[][] dl = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
        for (int i = 0; i < dl.length; i++) {
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(current_x + dx, current_y + dy) == MapData.TYPE_SPACE) {
                maxDepth = dfs(current_x + dx, current_y + dy, depth + 1, visited, deepestSpace, maxDepth);
            }
        }
        return maxDepth;
    }

    public int getMap(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public void setMap(int x, int y, int type) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        maps[y][x] = type;
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }

    public void setImageView(int x, int y, int type) {
        mapImageViews[y][x] = new ImageView(mapImages[type]);
    }

    public void setImageViews() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
