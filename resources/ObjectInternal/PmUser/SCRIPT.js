// PmUser front side hook
(function(ui) {
	if (!ui) return;
	const app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmUser = function(o, cbk) {
		try {
			console.log("PmUser hooks loading...");
			const p = o.locals.ui;
			p.timesheet.onload = function(ctn, obj, t) {
				var extobj = "PmTimesheetRecapExt"; 
				var embedded = true; 
				var param = null; 
				var url = app.getExternalObjectURL(extobj, param, embedded);
				console.log("url = " + url);
				let form = $("form",ctn);
				$('<div id="recap"/>')
					.attr("data-empid", t.ts.id1)
					.appendTo(form);
				$ui.loadURL($('#recap'), url, { showNav:false });
			}
			
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmUser: " + e.message);
		} finally {
			console.log("PmUser hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);
