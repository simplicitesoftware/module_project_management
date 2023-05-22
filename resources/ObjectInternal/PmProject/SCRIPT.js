// PmProject front side hook
(function(ui) {
	if (!ui) return;
	const app = ui.getAjax();
	// Hook called by each object instance
	Simplicite.UI.hooks.PmProject = function(o, cbk) {
		try {
			console.log("PmProject hooks loading...");
			const p = o.locals.ui;
			if (p && o.isMainInstance()) {
				p.form.onload = function(ctn, obj, params) {
					if(!o.isNew()){
						$('#PmProject-chart').html('<div id="chart-container"></div>');
						 $ui.displayCrosstab($("#chart-container"), "PmTask", "PmPrjTask",{ filters: {
							'pmTskVrsId.pmVrsPrjId.pmPrjName' :o.getFieldValue("pmPrjName"),
							'pmTskStatus':["DRAFT","TODO", "DOING", "DONE" ]
						}, options: {ztable: false, zcontrol: false , zgraph: 'pie'} });
						/* $('#PmProject-Search').html('<div id="search-container"></div>');
						$ui.displayList($('#PmProject-Search'), "PmTask", {filters: {
							'pmTskVrsId.pmVrsPrjId.pmPrjName' :o.getFieldValue("pmPrjName"),
							'pmTskStatus':["DRAFT","TODO", "DOING", "DONE" ]
						} }); */
					}
				};
			}
			//...
		} catch (e) {
			app.error("Error in Simplicite.UI.hooks.PmProject: " + e.message);
		} finally {
			console.log("PmProject hooks loaded.");
			cbk && cbk(); // final callback
		}
	};
})(window.$ui);
