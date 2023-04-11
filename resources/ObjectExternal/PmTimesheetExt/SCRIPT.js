var PmTimesheetExt = (function($) {
    let app;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        var app = $ui.getAjax(); 
		var extobj = "PmTimesheetRecapExt"; 
		var embedded = true; 
		var param = null; 
		var url = app.getExternalObjectURL(extobj, param, embedded);
		console.log("url = " + url);
		ctn = $('#recap'); 
		$ui.loadURL(ctn, url, { showNav:false });
    }        
    return { render: render };
})(jQuery);