// PmAssignmentPmTimeSheetAssign front side hook
(function(ui) {
	if (!ui) return;
	var app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmAssignmentPmTimeSheetAssign = function(o, cbk) {
		window.alert("test alert");
		try {
			console.log("PmAssignmentPmTimeSheetAssign hooks loading...");
			var p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					var app = $ui.getAjax(); 
					var extobj = "PmTimesheetRecapExt"; 
					var embedded = true; 
					var param = null; 
					var url = app.getExternalObjectURL(extobj, param, embedded);
					console.log("url = " + url);
					ctn = "$('#recap')"; 
					$ui.loadURL(ctn, url, { showNav:false });
				};
			}
			//...
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmAssignmentPmTimeSheetAssign: " + e.message);
		} finally {
			console.log("PmAssignmentPmTimeSheetAssign hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);
