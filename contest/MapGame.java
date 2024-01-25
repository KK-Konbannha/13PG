import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

//追加
import javafx.scene.control.Label;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;

public class MapGame extends Application {
  Stage stage;

  @Override
  public void start(Stage primaryStage) throws Exception {
    stage = primaryStage;
    stage.hide();
    StageDB.setMainClass(getClass());
    StageDB.getMainStage().show();
    StageDB.getMainSound().play();
  }

  public static void main(String[] args) {
    launch(args);
  }

  //追加
  //createTaskTestを呼び出すメソッド
  public static void callTask(Label lb, MapData mapData, MoveChara chara){
    int timeUpSec = 45;
    lb.setText("time up count : " + timeUpSec + " sec");
    createTaskTest(lb, timeUpSec, mapData, chara);
  }

  //時間制限を管理するためのメソッド
  //試しでfunc3を押すと実行されるようになっている
  public static void createTaskTest(Label time_count_label, int timeUpSec, MapData mapData, MoveChara chara){
    Service<Boolean> service = new Service<Boolean>(){
      //経過時間を保持
      final int endTime = timeUpSec;

      @Override
      protected Task<Boolean> createTask(){
        //タスクを定義
        Task<Boolean> task = new Task<Boolean>(){
          @Override
          protected Boolean call() throws Exception{
            for (int i=0; i<endTime; i++){
              try{
                //1秒待つ
                Thread.sleep(1000);
                //時間をコマンドラインに表示
                System.out.println("time up count : " + i + " sec");
              } finally {
                //この時点でキャラがゴールに到達していたらラベルの表示を変えてサービスを終了
                if (mapData.getMap(chara.getPosX(), chara.getPosY())==MapData.TYPE_GOAL){
                Platform.runLater( () -> time_count_label.setText( "!! GOAL !!" ) );
                //GameOverに飛ぶ
                StageDB.getMainStage().hide();
                StageDB.getMainSound().stop();
                StageDB.getGameOverStage().show();
                StageDB.getGameOverSound().play();
                break;
              }
                if(isCancelled()){
                  break;
                }
              }
              //ラベルを更新
              this.updateProgress( i+1 ,endTime );
              // this.updateMessage(String.valueOf(30-i));
              // this.updateProgress(i+1, endTime);
            }
            //タスクを終了
            return true;
          };
        };
        //作成したタスクを返す
        return task;
      };
    };
    //もしserviceが終了していたら実行する
    service.addEventFilter(WorkerStateEvent.WORKER_STATE_SUCCEEDED, e -> {
      //キャラがゴール地点にいるか取得
      if (mapData.getMap(chara.getPosX(), chara.getPosY())==MapData.TYPE_GOAL){
        // service.progressProperty().addListener(eve -> time_count_label.setText("!! GOAL !!"));
        Platform.runLater( () -> time_count_label.setText( "!! GOAL !!" ) );
        return;
      } else {
        //GameOverに飛ぶ
        Platform.runLater( () -> time_count_label.setText( "" ) );
        StageDB.getMainStage().hide();
        StageDB.getMainSound().stop();
        StageDB.getGameOverStage().show();
        StageDB.getGameOverSound().play();
      }
    });
    //サービスを開始
    service.start();
    //ラベルを更新
    service.progressProperty().addListener(e -> time_count_label.setText("time up count : " + (int)(timeUpSec - service.getProgress() * timeUpSec) + " sec"));
    
    return;
  };
}
