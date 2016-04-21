package com.isodevelopers.MusePlugin; 

import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.ConnectionState;
import android.util.Log;
/**
* Connection listener updates UI with new connection status and logs it.
*/
public class ConnectionListener extends MuseConnectionListener {
    private boolean connected;

    ConnectionListener() {
        connected = false; 
    }

    @Override
    public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
        final ConnectionState current = p.getCurrentConnectionState();
        if(current == ConnectionState.CONNECTED){
            connected = true;
        } 
        else {
            connected = false; 
        }
        final String status = p.getPreviousConnectionState().toString() +
                     " -> " + current;
        final String full = "Muse " + p.getSource().getMacAddress() +
                            " " + status;
        Log.i("Muse Headband", full);
    }

    public String isConnected(){ return (connected)?"True":"False"; }

}