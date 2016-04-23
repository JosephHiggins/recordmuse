/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    data: [],
    //clusterfck: require('clusterfck'),

    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    isConnectionReady: function(){
        window.Muse.isConnected(function(status){
            $('#logging').append($('<p>Connection status: '+status+'</p>')); 
            if(status==="False"){
               window.setTimeout(app.isConnectionReady, 1000); 
            } else {
                $('#logging').append($('<p>Getting readings...</p>'));
                app.getReadings(); 
            }
        });
    },

    checkForConnections: function(){
        window.Muse.refreshMuses(function(message){
            $('#logging').append($('<p>'+message+'</p>')); 
            if(message==="No Muses are paired."){
                window.setTimeout(app.checkForConnections, 1000); 
            } else {
                app.isConnectionReady(); 
            }
        });
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.checkForConnections();
    },

    getReadings: function() {
    	console.log("getting readings");
        window.Muse.getReadings(function(readings){
            app.processReadings(readings);
        }); 
        window.setTimeout(app.getReadings, 1000); 
    },

    processReadings: function(readings){
        readings = JSON.parse(readings);
        app.data.push(readings); 
        if(readings[0].length==0){
            $('#logging').html('<p>The device is not reading from all four sensors. Please adjust the headset and wait. This can take up to a minute.</p>'); 
        } else {
            $('#logging').html('');
            app.displayReading(readings[0], 'alpha');
            app.displayReading(readings[1], 'beta');
            app.displayReading(readings[2], 'delta');
            app.displayReading(readings[3], 'theta');
            app.displayReading(readings[4], 'gamma'); 
        }
    },

    displayReading: function(reading, type){
        $('#logging').append($('<div id="'+type+'"></div>')); 
        var avgs = [0,0,0,0];
        for(var i=0;i<reading.length;i++){
            for(var j=0;j<4;j++){
                avgs[j] += reading[i][j];
            }
        }
        for(var i=0;i<4;i++){
            avgs[j] /= reading.length;
        }
        $('#'+type).html('<strong>'+type+'</strong><br>TP9 '+avgs[0]+'<br>FP1: '+avgs[1]+'<br>FP2: '+avgs[2]+'<br>TP10: '+avgs[3]);
    },

    kmeans: function(){
        var formatted = []; 
        for(var i=0;i<app.data.length;i++){ // every refresh
            var flattened = [[],[],[],[],[]];
            for(var j=0;j<app.data[i].length; j++){ // five waves
                for(var k=0;k<app.data[i][j].length; k++){ // each reading
                    for(var l=0;l<app.data[i][j][k].length; l++){ // 4 sensors
                        flattened[j].push(app.data[i][j][k][l]); 
                    }
                }
            }
            for(var j=0;j<flattened[0].length;j++){
                formatted.push([flattened[0][i]]);
                formatted.push([flattened[1][i]]);
                formatted.push([flattened[2][i]]);
                formatted.push([flattened[3][i]]);
                formatted.push([flattened[4][i]]);
            }
        }

        // [[wave1s1, wave1s2 ... wave5s4] ]
        console.log(formatted);
        //console.log(window.clusterfck.kmeans(formatted, 2));
        app.data = [];  

    }

    
};
