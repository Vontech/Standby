package org.vontech.standy;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * A class for outputting synthesized voice
 * @author Aaron Vontell
 */
public class Speaker {

    private static Speaker singleton;
    private TextToSpeech tts;
    private boolean inited = false;

    private Speaker() {}

    /**
     * Returns a singleton instance of the Speaker, and initiates the service
     * @param context The context of the Android service which wants this Speaker
     * @return the created or existing speaker
     */
    public static Speaker getInstance(Context context) {

        if (singleton == null) {
            singleton = new Speaker();
            singleton.tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        singleton.tts.setLanguage(Locale.US);
                        singleton.inited = true;
                    }
                }
            });
        }

        return singleton;

    }

    /**
     * Attempts to speak the given string. If the service is unable to satisfy this queueing, then
     * fail silently. Todo: not this lol
     * @param text The String to speak
     */
    public void bestEffortSpeak(String text) {
        if (inited) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, text);
            } else {
                //noinspection deprecation
                tts.speak(text, TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            // The service wasn't ready :( we will figure out what to do here later
        }
    }

}
