var PmTimesheetExt = PmTimesheetExt || (function($) {
    let app;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        console.log("DEBUG in PmTimesheetExt");
    }        
    return { render: render };
})(jQuery);