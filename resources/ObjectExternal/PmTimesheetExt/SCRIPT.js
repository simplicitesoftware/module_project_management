var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        getElementById("ts").displayTimesheet(null, "PmUser", $ui.grant.getUserID(), "PmTimeSheetAssign");
        //$ui.displayTimesheet(null, "PmUser", $ui.grant.getUserID(), "PmTimeSheetAssign");
    }
    return { render: render };
})();