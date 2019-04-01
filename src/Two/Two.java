package Two;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Two extends PApplet {
    private Minim minim;  //Minim型変数であるminimの宣言
    private AudioPlayer player;  //サウンドデータ格納用の変数

    @Override
    public void settings() {
        size(600, 400);

    }

    @Override
    public void setup() {
        minim = new Minim(this);  //初期化
        player = minim.loadFile("2.wav");  //2.wavをロードする
        player.play();  //再生
    }

    @Override
    public void draw() {
        background(0,0,0);  // 背景色
        stroke(0,220,0);    // 線色(背景)
        int amp = 100;      // 振幅(音声チャンネルを画面上に波形で映し出す際の振幅)

        // 波形描画
        for(int i = 0; i < player.left.size()-1; i++){
            line(i, 100 + player.left.get(i)*amp, i+1, 100 + player.left.get(i+1)*amp);    // Lチャンネルの波形
            line(i, 300 + player.right.get(i)*amp, i+1, 300 + player.right.get(i+1)*amp);  // Rチャンネルの波形
        }
    }

    @Override
    public void stop() {
        player.close();
        minim.stop();
    }

    public static void main(String[] args){
        PApplet.main("Two.Two");
    }
}
