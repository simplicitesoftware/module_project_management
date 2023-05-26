// PmVersion front side hook
(function(ui) {
	if (!ui) return;
	const app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmVersion = function(o, cbk) {
		try {
			console.log("PmVersion hooks loading...");
			const p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					//...
				};
			}
			//...
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmVersion: " + e.message);
		} finally {
			console.log("PmVersion hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);
