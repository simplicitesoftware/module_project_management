var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        $ui.log("DEBUG");
        $ui.displayTimesheet(null, "PmUser", $ui.grant.getUserID(), "PmTimeSheetAssign");
    }
    return { render: render };
})();