package com.isodevelopers.MusePlugin; 

import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.Accelerometer;
import com.interaxon.libmuse.Eeg;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

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

    private ArrayList<ArrayList<Double>> alpha, beta, delta, theta, gamma;

    public DataListener(){
        alpha = new ArrayList<ArrayList<Double>>();
        beta = new ArrayList<ArrayList<Double>>();
        delta = new ArrayList<ArrayList<Double>>();
        theta = new ArrayList<ArrayList<Double>>();
        gamma = new ArrayList<ArrayList<Double>>();
    }

    @Override
    public void receiveMuseArtifactPacket(MuseArtifactPacket p){}

    @Override
    public void receiveMuseDataPacket(MuseDataPacket p) {
        switch (p.getPacketType()) {
            case ALPHA_RELATIVE:
                saveReading(p.getValues(), alpha);
                break;
            case BETA_RELATIVE:
                saveReading(p.getValues(), beta);
                break;
            case DELTA_RELATIVE:
                saveReading(p.getValues(), delta);
                break;
            case THETA_RELATIVE:
                saveReading(p.getValues(), theta);
                break;
            case GAMMA_RELATIVE:
                saveReading(p.getValues(), gamma);
                break;
            default:
                break;
        }
    }

    private void saveReading(final ArrayList<Double> data, ArrayList<ArrayList<Double>> toSave) {

        Double readings[] = {
            data.get(Eeg.TP9.ordinal()), 
            data.get(Eeg.FP1.ordinal()), 
            data.get(Eeg.FP2.ordinal()),
            data.get(Eeg.TP10.ordinal())
        }; 

        ArrayList<Double> selectedData = new ArrayList<Double>();
        
        for(int i=0;i<readings.length;i++){
            if(!Double.isNaN(readings[i])){
                selectedData.add(readings[i]); 
            }
            
        }

        // don't save partial readings
        if(selectedData.size()==4){
            toSave.add(selectedData); 
        }
        
    }

    public ArrayList<ArrayList<Double>> getReadings(String toGet){
        switch(toGet){
            case "Alpha": return alpha;
            case "Beta": return beta;
            case "Delta": return delta;
            case "Theta": return theta;
            case "Gamma": return gamma;
            default: break; 
        } return null; 
    }

    public void resetReadings(){
        alpha = new ArrayList<ArrayList<Double>>();
        beta = new ArrayList<ArrayList<Double>>();
        delta = new ArrayList<ArrayList<Double>>();
        theta = new ArrayList<ArrayList<Double>>();
        gamma = new ArrayList<ArrayList<Double>>();
    }

}
