package org.sgine.ui;

import com.sun.jna.Memory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener;

/**
 * @author Matt Hicks <mhicks@sgine.org>
 *         Date: 10/31/11
 */
public class Test {
    public static boolean informed = false;

    public static void main(String[] args) {
        args = new String[]{"test.avi"};

        MediaPlayerFactory factory = new MediaPlayerFactory();
//        List<String> vlcArgs = new ArrayList<String>();
//        vlcArgs.add("dummy");

        MediaPlayer mediaPlayer = factory.newDirectMediaPlayer(1, 1, new RenderCallback() {
            public void display(Memory nativeBuffer) {
            }
        });
//        mediaPlayer.setStandardMediaOptions("dummy");

        mediaPlayer.addVideoOutputEventListener(new VideoOutputEventListener() {
            public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput) {
                System.out.println("     Track Information: " + mediaPlayer.getTrackInfo());
                System.out.println("    Title Descriptions: " + mediaPlayer.getTitleDescriptions());
                System.out.println("    Video Descriptions: " + mediaPlayer.getVideoDescriptions());
                System.out.println("    Audio Descriptions: " + mediaPlayer.getAudioDescriptions());
                for (int i = 0; i < mediaPlayer.getTitleDescriptions().size(); i++) {
                    System.out.println("Chapter Descriptions " + i + ": " + mediaPlayer.getChapterDescriptions(i));
                }
                System.out.println("    Other Information : " + (mediaPlayer.getLength() / 60000.0));
                informed = true;
            }
        });

        long time = System.currentTimeMillis();
        mediaPlayer.prepareMedia("test.avi");
        mediaPlayer.parseMedia();
        mediaPlayer.start();
        try {
            while (!informed) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
        }
        System.out.println("Time: " + (System.currentTimeMillis() - time) + "ms");
        mediaPlayer.stop();

        informed = false;
        time = System.currentTimeMillis();
        mediaPlayer.prepareMedia("test2.avi");
        mediaPlayer.parseMedia();
        mediaPlayer.start();
        try {
            while (!informed) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
        }
        System.out.println("Time: " + (System.currentTimeMillis() - time) + "ms");
        mediaPlayer.stop();

        mediaPlayer.release();
        factory.release();
    }

}
