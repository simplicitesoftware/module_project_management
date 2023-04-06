var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        console.log("DEBUG");
        $ui.displayTimesheet(null, "PmUser", $ui.grant.getUserID(), "PmTimeSheetAssign");
    }
    return { render: render };
})();