var PmTimesheetExt = PmTimesheetExt || (function($) {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
    }        
    return { render: render };
})(jQuery);