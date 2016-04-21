/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package com.isodevelopers.MusePlugin;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;


/**
 * Plugins must extend this class and override one of the execute methods.
 */
public class MusePlugin extends CordovaPlugin {

    private ConnectionListener conn;
    private DataListener data;  



    /**
     * Called after plugin construction and fields have been initialized.
     * Prefer to use pluginInitialize instead since there is no value in
     * having parameters on the initialize() function.
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        this.conn = new ConnectionListener();
        this.data = new DataListener();
    }

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments, wrapped with some Cordova helpers.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     */
    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if("refreshMuses".equals(action)){
            return refreshMuses(callbackContext); 
        } else if("getReadings".equals(action)){
            return getReadings(callbackContext); 
        } else if("isConnected".equals(action)){
            return isConnected(callbackContext);
        }
        return false;
    }

    private boolean refreshMuses(CallbackContext callbackContext){
        MuseManager.refreshPairedMuses(); 
        List<Muse> pairedMuses = MuseManager.getPairedMuses(); 
        String muses = ""; 
        for(Muse m: pairedMuses){
            String dev_id = m.getName() + "-" + m.getMacAddress();
            Log.i("Muse Headband", dev_id);
            muses += dev_id + "\n";
            m.registerConnectionListener(conn);
            m.registerDataListener(data, MuseDataPacketType.ALPHA_RELATIVE);
            m.registerDataListener(data, MuseDataPacketType.BETA_RELATIVE);
            m.registerDataListener(data, MuseDataPacketType.DELTA_RELATIVE);
            m.registerDataListener(data, MuseDataPacketType.THETA_RELATIVE);
            m.registerDataListener(data, MuseDataPacketType.GAMMA_RELATIVE);
            m.setPreset(MusePreset.PRESET_14);
            m.enableDataTransmission(true);
            try {
                m.runAsynchronously();
            } catch (Exception e) {
                Log.e("Muse Headband", e.toString());
            }
        }
        if(muses.length() > 0){
            callbackContext.success(muses);
        } else {
            callbackContext.success("No muses are paired.");
        }
        return true;
    }

    private boolean getReadings(CallbackContext callbackContext){
        
        Log.d("Muse readings","asking for readings");
        ArrayList<ArrayList<Double>> alpha = data.getReadings("Alpha");
        ArrayList<ArrayList<Double>> beta = data.getReadings("Beta"); 
        ArrayList<ArrayList<Double>> delta = data.getReadings("Delta"); 
        ArrayList<ArrayList<Double>> theta = data.getReadings("Theta"); 
        ArrayList<ArrayList<Double>> gamma = data.getReadings("Gamma"); 
        Object[] coolDataStructure = {alpha, beta, delta, theta, gamma}; 
        JSONArray allOfIt = new JSONArray();
        for(int i=0;i<coolDataStructure.length;i++){
            JSONArray waves = new JSONArray();
            ArrayList<ArrayList<Double>> wavesAL = (ArrayList<ArrayList<Double>>)coolDataStructure[i];
            for(int j=0;j<wavesAL.size();j++){
                JSONArray times = new JSONArray();
                for(int k=0;k<wavesAL.get(j).size();k++){
                    times.put(wavesAL.get(j).get(k));
                }
                waves.put(times); 
            }
            allOfIt.put(waves);
        }
        data.resetReadings();
        callbackContext.success(allOfIt.toString());
        return true;
    }

    private boolean isConnected(CallbackContext callbackContext){
        callbackContext.success(conn.isConnected());
        return true; 
    }

}