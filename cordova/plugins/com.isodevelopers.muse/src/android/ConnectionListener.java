package com.isodevelopers.muse; 

import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
/**
* Connection listener updates UI with new connection status and logs it.
*/
public class ConnectionListener extends MuseConnectionListener {
    private String lastMessage;

    ConnectionListener() {}

    @Override
    public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
        final ConnectionState current = p.getCurrentConnectionState();
        final String status = p.getPreviousConnectionState().toString() +
                     " -> " + current;
        final String full = "Muse " + p.getSource().getMacAddress() +
                            " " + status;
        Log.i("Muse Headband", full);
        lastMessage = full; 
    }

    public String getLastMessage(){
        return lastMessage;
    }
}