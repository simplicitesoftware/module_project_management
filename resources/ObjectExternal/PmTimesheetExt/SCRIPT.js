var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        alert("in render");
        $ui.displayTimesheet(null, "PmUser", $ui.grant.getUserID(), "PmTimeSheetAssign");
    }
    return { render: render };
})();