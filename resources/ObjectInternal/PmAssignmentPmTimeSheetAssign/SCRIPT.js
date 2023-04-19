// PmAssignmentPmTimeSheetAssign front side hook
(function(ui) {
	if (!ui) return;
	var app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmAssignmentPmTimeSheetAssign = function(o, cbk) {
		try {
			
			var p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					var extobj = "PmTimesheetRecapExt"; 
					var embedded = true; 
					var param = null; 
					var url = app.getExternalObjectURL(extobj, param, embedded);
					console.log("url = " + url);
					$ui.loadURL($('#recap'), url, { showNav:false });
				};
			}
			//...
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmAssignmentPmTimeSheetAssign: " + e.message);
		} finally {
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);