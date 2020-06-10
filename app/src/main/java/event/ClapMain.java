package event;

import android.util.Log;

public class ClapMain  {

    public static final int DETECT_NONE = 0;
    public static final int DETECT_CLAP = 1;
    public static int selectedDetection = DETECT_NONE;

    private DetectorThread detectorThread;
    private RecorderThread recorderThread;
    private Thread detectedTextThread;
    public static int clapsValue = 0;



    public ClapMain(DetectorThread detectorThread) {
        this.detectorThread = detectorThread;
        startVoiceDetection();
        //stopVoiceDetection();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }




    public void startVoiceDetection() {
        selectedDetection = DETECT_CLAP;
        recorderThread = new RecorderThread();
        recorderThread.start();
        detectorThread = new DetectorThread(recorderThread);
        detectorThread.start();
        goListeningView();
    }

    private void stopVoiceDetection() {
        if (recorderThread != null) {
            recorderThread.stopRecording();
            recorderThread = null;
        }
        if (detectorThread != null) {
            detectorThread.stopDetection();
            detectorThread = null;
        }
        selectedDetection = DETECT_NONE;
    }

    private void goListeningView() {
        if (detectedTextThread == null) {
            detectedTextThread = new Thread() {
                public void run() {
                    try {
                        while (recorderThread != null && detectorThread != null) {
                            new Runnable() {
                                public void run() {
                                    if (detectorThread != null) {
                                        Log.e("Clap", "Detected");
                                    }
                                }
                            };
                            sleep(100);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        detectedTextThread = null;
                    }
                }
            };
            detectedTextThread.start();
        }
    }
}