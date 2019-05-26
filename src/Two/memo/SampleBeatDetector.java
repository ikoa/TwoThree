package Two.memo;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import processing.core.PApplet;

public class SampleBeatDetector extends PApplet {
    // private AudioInput audioInput;
    private AudioPlayer audioPlayer;
    private BeatDetect beatDetect;
    private TextPrinter textPrinter = new TextPrinter();
    private int sensitivity;

    @Override
    public void settings() {
        size(500, 500);
    }

    @Override
    public void setup() {
        Minim minim = new Minim(this);
        frameRate(50);
        // audioInput = minim.getLineIn(Minim.STEREO, 1024);
        audioPlayer = minim.loadFile("Mixdown.mp3");
        audioPlayer.play();
        beatDetect = new BeatDetect();
        beatDetect.detectMode(BeatDetect.FREQ_ENERGY);
        sensitivity = 300;
        beatDetect.setSensitivity(sensitivity);
    }

    @Override
    public void draw() {
        // background(255);
        background(0);
        beatDetect.detect(audioPlayer.mix);

        fill(128);
        //text("sensitivity(+/-): " + sensitivity + "mills", 20, 30);

        translate(width/2, height/2);
        noStroke();
//        if(beatDetect.isKick() || beatDetect.isHat() || beatDetect.isSnare()) {
//            background(0);
//        }

        drawWave(audioPlayer, beatDetect);
        if (beatDetect.isHat()) {
            filter(POSTERIZE, 4);
        }

         drawText(textPrinter);
    }

    private void drawText(TextPrinter textPrinter) {
        textSize(400);
        text(textPrinter.selectText(), -50, -50);
    }

    private class TextPrinter {
        private int counter = 0;
        private int textIndex = 0;
        private String[] texts = {"-", "\\", "|", "/"};

        private String selectText() {
            counter++;
            if (counter % 7 == 0) {
                textIndex++;
            }
            return texts[textIndex % 4];
        }
    }

    private void drawWave(AudioPlayer player, BeatDetect beatDetect) {
        int amp = 300;
        final int arraySize = 1024;
        final double MaxSize = 1024.0;
        final double delAng = 360.0 / MaxSize;
        final float r = 150;

        stroke(180, 220, 220, 50);
//        text("player.left.size() " + player.left.size(), -200, 200);
//        text("player.right.size() " + player.right.size(), -200, 180);
//        text("deAng " + delAng, -200, 160);

        final int del = 2;
        for (int i = 0; i < arraySize - del; i += del) {
            final float ang = (float) (i * delAng);
            final float nextAng = (float) (ang + delAng);
            final float rad = radians(ang);
            final float nextRad = radians(nextAng);
            line(
                    ((amp * player.left.get(i)) + r)  * cos(rad),
                    ((amp * player.left.get(i)) + r) * sin(rad),
                    ((amp * player.left.get(i + del)) + r) * cos(nextRad),
                    ((amp * player.left.get(i + del)) + r) * sin(nextRad)
            );
        }
        line(
                ((amp * player.left.get(0)) + r)  * cos(0),
                ((amp * player.left.get(0)) + r) * sin(0),
                ((amp * player.left.get(arraySize - del)) + r) * cos(radians((float) ((arraySize-del)*delAng))),
                ((amp * player.left.get(arraySize - del)) + r) * sin(radians((float) ((arraySize-del)*delAng)))
        );

    }

    @Override
    public void keyPressed() {
        switch(key){
            case '+':
                sensitivity += 10;
                break;
            case '-':
                sensitivity -= 10;
                break;
            default:
                return;
        }
        if(sensitivity < 0){
            sensitivity = 0;
        } else if(sensitivity > 1000){
            sensitivity = 1000;
        }
        beatDetect.setSensitivity(sensitivity);
    }

    @Override
    public void stop() {
        audioPlayer.close();
    }

    public static void main(String[] args) {
        PApplet.main(SampleBeatDetector.class.getName());
    }
}
