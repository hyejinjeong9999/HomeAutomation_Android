package event;

import android.media.AudioFormat;
import android.media.AudioRecord;

import com.musicg.api.ClapApi;
import com.musicg.wave.WaveHeader;

import java.util.LinkedList;

public class DetectorThread extends Thread {

    private RecorderThread recorder;
    private ClapApi clapApi;
    private volatile Thread _thread;

    private LinkedList<Boolean> clapResultList = new LinkedList<Boolean>();
    private int numClaps;
    private int totalClapsDetected = 0;
    private int clapCheckLength = 3;
    private int clapPassScore = 3;

    public DetectorThread(RecorderThread recorder) {
        this.recorder = recorder;
        AudioRecord audioRecord = recorder.getAudioRecord();

        int bitsPerSample = 0;
        if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) {
            bitsPerSample = 16;
        } else if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT) {
            bitsPerSample = 8;
        }

        int channel = 0;
        // whistle detection only supports mono channel
        //if (audioRecord.getChannelConfiguration() == AudioFormat.CHANNEL_CONFIGURATION_MONO) {
        channel = 1;
        //}

        WaveHeader waveHeader = new WaveHeader();
        waveHeader.setChannels(channel);
        waveHeader.setBitsPerSample(bitsPerSample);
        waveHeader.setSampleRate(audioRecord.getSampleRate());
        clapApi = new ClapApi(waveHeader);
    }

    private void initBuffer() {
        numClaps = 0;
        clapResultList.clear();

        // init the first frames
        for (int i = 0; i < clapCheckLength; i++) {
            clapResultList.add(false);
        }
        // end init the first frames
    }

    public void start() {
        _thread = new Thread(this);
        _thread.start();
    }

    public void stopDetection() {
        _thread = null;
    }

    public void run() {
        try {
            byte[] buffer;
            initBuffer();

            Thread thisThread = Thread.currentThread();
            while (_thread == thisThread) {
                // detect sound
                buffer = recorder.getFrameBytes();

                // audio analyst
                if (buffer != null) {
                    // sound detected
                    ClapMain.clapsValue = numClaps;

                    // whistle detection
                    //System.out.println("*Whistle:");
                    boolean isClap = clapApi.isClap(buffer);
                    if (clapResultList.getFirst()) {
                        numClaps--;
                    }

                    clapResultList.removeFirst();
                    clapResultList.add(isClap);

                    if (isClap) {
                        numClaps++;
                    }
                    //System.out.println("num:" + numWhistles);

                    if (numClaps >= clapPassScore) {
                        // clear buffer
                        initBuffer();
                        totalClapsDetected++;
                    }
                    // end whistle detection
                } else {
                    // no sound detected
                    if (clapResultList.getFirst()) {
                        numClaps--;
                    }
                    clapResultList.removeFirst();
                    clapResultList.add(false);

                    ClapMain.clapsValue = numClaps;
                }
                // end audio analyst
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalClapsDetected() {
        return totalClapsDetected;
    }
}