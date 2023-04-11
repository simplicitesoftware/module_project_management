var PmTimesheetExt = (function($) {
    let app;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
    }        
    return { render: render };
})(jQuery);