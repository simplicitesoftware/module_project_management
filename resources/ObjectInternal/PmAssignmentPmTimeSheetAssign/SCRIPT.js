/* // PmAssignmentPmTimeSheetAssign front side hook
(function(ui) {
	console.log("DEBUG fonction(ui) PmAssignmentPmTimeSheetAssign ------------------------------------");
	if (!ui) return;
	var app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmAssignmentPmTimeSheetAssign = function(o, cbk) {
		console.log("DEBUG Simplicite.UI.hooks.PmAssignmentPmTimeSheetAssign ------------------------------------");
		try {
			console.log("PmAssignmentPmTimeSheetAssign hooks loading...");
			var p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					var extobj = "PmTimesheetRecapExt"; 
					var embedded = true; 
					var param = null; 
					var url = app.getExternalObjectURL(extobj, param, embedded);
					console.log("url = " + url);
					$("#ts").after("<div id='recap'></div>");
					$ui.loadURL($('#recap'), url, { showNav:false });
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
})(window.$ui); */