package com.teampotato.opotato.util;

import com.teampotato.opotato.Opotato;

public class EmptyThread extends Thread {
    public EmptyThread() {
        this.setName("Empty Thread");
        this.setDaemon(true);
        this.start();
    }
    public void run() {
        Opotato.LOGGER.info("Opotato removes Elenai Dodge 2's Internet connection behavior.");
    }
}
