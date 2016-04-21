var argscheck = require('cordova/argscheck'),
  utils = require('cordova/utils'),
  exec = require('cordova/exec');

var Muse = function(){};
Muse.refreshMuses = function(callback){
	exec(callback, function(err){
		callback(err); 
	}, "MusePlugin", "refreshMuses", []);
};
Muse.isConnected = function(callback){
	exec(callback, function(err){
		callback(err);
	}, "MusePlugin", "isConnected", []); 
};
Muse.getReadings = function(callback){
	exec(callback, function(err){
		callback(err); 
	}, "MusePlugin", "getReadings", []);
};

module.exports = Muse;	