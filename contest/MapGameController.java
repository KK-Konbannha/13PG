import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        drawMap();
    }

    // Draw the map
    public void drawMap() {
        int cx = chara.getPosX();
        int cy = chara.getPosY();
        mapGrid.getChildren().clear();
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                if (x == cx && y == cy) {
                    mapGrid.add(chara.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapData.getImageView(x, y), x, y);
                }
            }
        }
    }

    // Get users' key actions
    public void keyAction(KeyEvent event) {
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
        if (key == KeyCode.A) {
            leftButtonAction();
        } else if (key == KeyCode.S) {
            downButtonAction();
        } else if (key == KeyCode.W) {
            upButtonAction();
        } else if (key == KeyCode.D) {
            rightButtonAction();
        }
    }

    // Operations for going the cat up
    public void upButtonAction() {
        printAction(MoveChara.TYPE_UP);
        chara.setCharaDirection(MoveChara.TYPE_UP);
        chara.move(MoveChara.TYPE_UP);
        drawMap();
    }

    // Operations for going the cat down
    public void downButtonAction() {
        printAction(MoveChara.TYPE_DOWN);
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        chara.move(MoveChara.TYPE_DOWN);
        drawMap();
    }

    // Operations for going the cat right
    public void leftButtonAction() {
        printAction(MoveChara.TYPE_LEFT);
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        chara.move(MoveChara.TYPE_LEFT);
        drawMap();
    }

    // Operations for going the cat right
    public void rightButtonAction() {
        printAction(MoveChara.TYPE_RIGHT);
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        chara.move(MoveChara.TYPE_RIGHT);
        drawMap();
    }

    @FXML
    public void func1ButtonAction(ActionEvent event) {
        try {
            // 追加
            lb_0.setText("");
            System.out.println("func1");
            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameOverStage().show();
            StageDB.getGameOverSound().play();// ここ
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void func2ButtonAction(ActionEvent event) {
        // 追加
        mapData.setBlackFlag(0); // 暗転オフ
        lb_0.setText("");
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        chara.TYPE_DOWN = 0;
        chara.TYPE_LEFT = 1;
        chara.TYPE_RIGHT = 2;
        chara.TYPE_UP = 3;
        drawMap();
        StageDB.getMainSound().play();
        System.out.println("func2: new map");

    }

    // 追加
    // 時間制限表示用のラベルを取得
    @FXML
    private Label lb_0;

    @FXML
    public void func3ButtonAction(ActionEvent event) {
        // 追加
        // 試しで制限時間の機能を実行
        MapGame.callTask(lb_0, mapData, chara);
    }

    @FXML
    public void func4ButtonAction(ActionEvent event) {
        System.out.println("func4: 暗転");
        for (int bbx = 0; bbx < 21; bbx++) {
            for (int bby = 0; bby < 15; bby++) {
                if (mapData.getMap(bbx, bby) == MapData.TYPE_SPACE || mapData.getMap(bbx, bby) == MapData.TYPE_WALL) {
                    mapData.setImageView(bbx, bby, MapData.TYPE_BLACK); // 壁と床をすべて黒にする
                }
            }
        }
        mapData.setBlackFlag(1); // 暗転オン

    }

    // Print actions of user inputs
    public void printAction(int type) {
        String actionString = "";
        if (type == MoveChara.TYPE_UP) {
            actionString = "UP";
        } else if (type == MoveChara.TYPE_DOWN) {
            actionString = "DOWN";
        } else if (type == MoveChara.TYPE_LEFT) {
            actionString = "LEFT";
        } else if (type == MoveChara.TYPE_RIGHT) {
            actionString = "RIGHT";
        }
        System.out.println("Action: " + actionString);
    }

}
