var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        $ui.displayTimesheet(null, "PmUser", $ui.grant.getUserID(), "PmImputation");
    }
    return { render: render };
})();