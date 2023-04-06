var CraTimesheetObjExt = (function() {
	//CRA
    function render(params) {
    	$ui.displayTimesheet(null, "CraEmploye", $ui.grant.getUserID(), "Imputation");
    }
    return { render: render };
})();