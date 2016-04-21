package com.isodevelopers.muse; 

import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.Accelerometer;
import com.interaxon.libmuse.Eeg;

/**
 * Data listener will be registered to listen for: Accelerometer,
 * Eeg and Relative Alpha bandpower packets. In all cases we will
 * update UI with new values.
 * We also will log message if Artifact packets contains "blink" flag.
 * DataListener methods will be called from execution thread. If you are
 * implementing "serious" processing algorithms inside those listeners,
 * consider to create another thread.
 */
class DataListener extends MuseDataListener {

    DataListener(){}

    @Override
    public void receiveMuseDataPacket(MuseDataPacket p) {
        switch (p.getPacketType()) {
            case EEG:
                updateEeg(p.getValues());
                break;
            case ACCELEROMETER:
                updateAccelerometer(p.getValues());
                break;
            case ALPHA_RELATIVE:
                updateAlphaRelative(p.getValues());
                break;
            default:
                break;
        }
    }

    @Override
    public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
        if (p.getHeadbandOn() && p.getBlink()) {
            Log.i("Artifacts", "blink");
        }
    }

    private void updateAccelerometer(final ArrayList<Double> data) {
        Log.i("Accelerometer", 
            String.format("%6.2f", data.get(Accelerometer.FORWARD_BACKWARD.ordinal())));
        Log.i("Accelerometer", 
            String.format("%6.2f", data.get(Accelerometer.UP_DOWN.ordinal())));
        Log.i("Accelerometer", 
            String.format("%6.2f", data.get(Accelerometer.LEFT_RIGHT.ordinal())));
    }

    private void updateEeg(final ArrayList<Double> data) {
        Log.i("EEG", 
            String.format("%6.2f", data.get(Eeg.TP9.ordinal())));
        Log.i("EEG", 
            String.format("%6.2f", data.get(Eeg.FP1.ordinal())));
        Log.i("EEG", 
            String.format("%6.2f", data.get(Eeg.FP2.ordinal())));
        Log.i("EEG", 
            String.format("%6.2f", data.get(Eeg.TP10.ordinal())));
    }

    private void updateAlphaRelative(final ArrayList<Double> data) {
        Log.i("Alpha Relative", 
            String.format("%6.2f", data.get(Eeg.TP9.ordinal())));
        Log.i("Alpha Relative", 
            String.format("%6.2f", data.get(Eeg.FP1.ordinal())));
        Log.i("Alpha Relative", 
            String.format("%6.2f", data.get(Eeg.FP2.ordinal())));
        Log.i("Alpha Relative", 
            String.format("%6.2f", data.get(Eeg.TP10.ordinal())));
    }

}
