// PmAssignment front side hook
(function(ui) {
	if (!ui) return;
	const app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmAssignment = function(o, cbk) {
		try {
			console.log("DEBUG Simplicite.UI.hooks.PmAssignment hook");
			var extobj = "PmTimesheetRecapExt"; 
			var embedded = true; 
			var param = null; 
			var url = app.getExternalObjectURL(extobj, param, embedded);
			console.log("url = " + url);
			$ui.loadURL($('#recap'),url,null);
			console.log("PmTimesheetExt");
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmAssignment: " + e.message);
			console.log("Error in Simplicite.UI.hooks.PmAssignment: " + e.message);
		} finally {
			console.log("PmAssignment hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);
 