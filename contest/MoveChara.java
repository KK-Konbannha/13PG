import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

public class MoveChara {
    public static int TYPE_DOWN = 0;
    public static int TYPE_LEFT = 1;
    public static int TYPE_RIGHT = 2;
    public static int TYPE_UP = 3;

    private final String[] directions = { "Down", "Left", "Right", "Up" };
    private final String[] animationNumbers = { "1", "2", "3" };
    private final String pngPathPre = "png/hero";
    private final String pngPathSuf = ".png";

    private int posX;
    private int posY;

    private MapData mapData;

    private Image[][] charaImages;
    private ImageView[] charaImageViews;
    private ImageAnimation[] charaImageAnimations;

    private int charaDirection;

    MoveChara(int startX, int startY, MapData mapData) {
        this.mapData = mapData;

        charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i = 0; i < 4; i++) {
            charaImages[i] = new Image[3];
            for (int j = 0; j < 3; j++) {
                charaImages[i][j] = new Image(
                        pngPathPre + directions[i] + animationNumbers[j] + pngPathSuf);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation(
                    charaImageViews[i], charaImages[i]);
        }

        posX = startX;
        posY = startY;

        setCharaDirection(TYPE_RIGHT); // start with right-direction
    }

    // set the cat's direction
    public void setCharaDirection(int cd) {
        charaDirection = cd;
        for (int i = 0; i < 4; i++) {
            if (i == charaDirection) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    // check whether the cat can move on
    private boolean isMovable(int dx, int dy) {
        if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_WALL) {
            return false;
        }
        return true;
    }

    // move the cat
    public boolean move(int type) {
        int dx = 0, dy = 0;
        switch (type) {
            case 0:
                dx = 0;
                dy = 1;
                break;
            case 1:
                dx = -1;
                dy = 0;
                break;
            case 2:
                dx = 1;
                dy = 0;
                break;
            case 3:
                dx = 0;
                dy = -1;
                break;
        }
        if (isMovable(dx, dy)) {
            posX += dx;
            posY += dy;
            System.out.println("chara[X,Y]:" + posX + "," + posY);
            goalCheck(posX, posY);
            warpCheck(posX, posY);
            mushCheck(posX, posY);
            return true;
        } else {
            return false;
        }
    }

    private void goalCheck(int x, int y) {
        if (mapData.getMap(x, y) == MapData.TYPE_GOAL) {
            System.out.println("game clear");
        }
    }

    private void warpCheck(int x, int y) {
        if (mapData.getMap(x, y) == MapData.TYPE_WARP) {
            this.posX = 1;
            this.posY = 1;
            mapData.setMap(x, y, MapData.TYPE_SPACE);
            mapData.setImageView(x, y, MapData.TYPE_SPACE);
        }
    }

    private void mushCheck(int x, int y) {
        if (mapData.getMap(x, y) == MapData.TYPE_MUSHROOM) {
            TYPE_DOWN = (int) (Math.random() * 4);
            do {
                TYPE_LEFT = (int) (Math.random() * 4);
            } while (TYPE_DOWN == TYPE_LEFT);
            do {
                TYPE_RIGHT = (int) (Math.random() * 4);
            } while (TYPE_DOWN == TYPE_RIGHT || TYPE_LEFT == TYPE_RIGHT);
            do {
                TYPE_UP = (int) (Math.random() * 4);
            } while (TYPE_DOWN == TYPE_UP || TYPE_LEFT == TYPE_UP || TYPE_RIGHT == TYPE_UP);
            mapData.setMap(x, y, MapData.TYPE_SPACE);
            mapData.setImageView(x, y, MapData.TYPE_SPACE);
        }
    }

    // getter: direction of the cat
    public ImageView getCharaImageView() {
        return charaImageViews[charaDirection];
    }

    // getter: x-positon of the cat
    public int getPosX() {
        return posX;
    }

    // getter: y-positon of the cat
    public int getPosY() {
        return posY;
    }

    // Show the cat animation
    private class ImageAnimation extends AnimationTimer {

        private ImageView charaView = null;
        private Image[] charaImages = null;
        private int index = 0;

        private long duration = 500 * 1000000L; // 500[ms]
        private long startTime = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        public ImageAnimation(ImageView charaView, Image[] images) {
            this.charaView = charaView;
            this.charaImages = images;
            this.index = 0;
        }

        @Override
        public void handle(long now) {
            if (startTime == 0) {
                startTime = now;
            }

            preCount = count;
            count = (now - startTime) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if (index < 0 || 2 < index) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }
}
