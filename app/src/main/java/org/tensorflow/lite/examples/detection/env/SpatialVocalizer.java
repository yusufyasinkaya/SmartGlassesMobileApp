package org.tensorflow.lite.examples.detection.env;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.core.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Queue;

public class SpatialVocalizer {

    private static final int languageSuccess = 0;
    private static final int languageError = -1;
    private static final String ttsFileName = "ttsAudioFile.wav";
    private static final String Class_TAG = "SpacialVocalizer";
    private static final String TTS_TAG = "SpacialVoc... TTS:";

    private static final int PlaybackRate = 44100;

    TextToSpeech tts;
    private Context appContext;
    private float leftChannel = 1.0f;
    private float rightChannel = 0.0f;
    private boolean isPlaying = false;

    AudioFormat audioFormat = new AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(PlaybackRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build();

    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build();

    private ArrayList<Pair<String, Pair<Float, Float>>> textQueue = new ArrayList<Pair<String, Pair<Float, Float>>>();

    public SpatialVocalizer(Context context, Locale locale) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    if (setLanguage(locale) == languageSuccess) {
                        setupTTS(0.6f, 1.0f);
                        appContext= context;
                    } else {
                        Log.e(TTS_TAG,
                                "TTS language is not valid");
                    }
                } else {
                    Log.e(TTS_TAG,
                            "TTS INIT ERROR, STATUS: ".concat(String.valueOf(status))
                            );
                }
            }
        });
    }

    public int setLanguage(Locale locale) {
        int result = tts.setLanguage(locale);
        if (result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e(TTS_TAG, "LANGUAGE NOT SUPPORTED");
            return languageError;
        } else {
            return languageSuccess;
        }
    }

    public void setupTTS(float pitch, float speechRate) {
        tts.setPitch(pitch);
        tts.setSpeechRate(speechRate);

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                Log.d(Class_TAG, "UTR on start");
            }

            @Override
            public void onDone(String s) {
                Log.d(Class_TAG, "UTR on done:");
                byte[] audioData = readSound();
                playSound(audioData);
            }

            @Override
            public void onError(String s) {
                Log.e(Class_TAG, "UTR error on TTS");
            }
        });
    }

    private void playNext() {
        if (textQueue.size() > 0) {
           // textToWav(textQueue.get(0));
            textQueue.remove(0);
        }
    }

    public void playText(
            String text,
            float leftChannel,
            float rightChannel
    ) {
        textQueue.add(new Pair<>(text, new Pair<>(leftChannel, rightChannel)));
        textToWav(text);
    }

    private Pair<Float, Float> calculateChannels(float totalX, float xPos) {
        float ratio = xPos / totalX;
        return new Pair<>(1 - ratio, ratio);
    }

    private void textToWav(
            String text
    ) {
        String utrId = "spatialVocalizerUtrId";
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utrId);

        File path = appContext.getApplicationContext().getFilesDir();
        File ttsFile = new File(path, ttsFileName);

        //this.leftChannel = leftChannel;
        //this.rightChannel = rightChannel;
        //textQueue.add(text);
        tts.synthesizeToFile(text, params, ttsFile, utrId);
    }

    private byte[] readSound() {
        File path = appContext.getApplicationContext().getFilesDir();
        File file = new File(path, ttsFileName);
        byte[] audioData = new byte[(int) file.length()];
        try {
            InputStream inputStream = new FileInputStream(file);
            inputStream.read(audioData);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return audioData;
    }

    private void playSound(byte[] audioData) {

        AudioTrack at = new AudioTrack.Builder()
                .setAudioFormat(audioFormat)
                .setAudioAttributes(audioAttributes)
                .setBufferSizeInBytes(audioData.length)
                .build();

        at.write(audioData, 0, audioData.length);
        at.setPlaybackRate(PlaybackRate / 2);

        float left = textQueue.get(0).second.first;
        float right = textQueue.get(0).second.second;
        at.setStereoVolume(left, right);
        textQueue.remove(0);

        at.setNotificationMarkerPosition(audioData.length / 2);

        at.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onMarkerReached(AudioTrack audioTrack) {
                Log.d(Class_TAG, "ON PLAY END");
            }

            @Override
            public void onPeriodicNotification(AudioTrack audioTrack) {

            }
        });

        at.play();
    }
}
