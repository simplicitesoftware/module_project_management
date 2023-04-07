var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        $ui.displayTimesheet($('#ts'), "PmUser", $ui.grant.getUserID(), "PmTimeSheetAssign");
    }
    return { render: render };
})();