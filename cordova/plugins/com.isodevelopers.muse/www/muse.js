window.muse = {
	refreshMuses : function(callback){
		cordova.exec(callback, function(err){
			callback('Error in callback!'); 
		}, "Muse", "refreshMuses");
	}
}