var PmTimesheetExt = (function($) {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
		var div=$('#recap')
        var template =$('#pm-ass-template').html();
		if (userid) {
            // Search the client
			app = $ui.getAjax();
			var affect = app.getBusinessObject('PmAssignment');
            affect.search(function(list) {
                div.html(Mustache.render(template,list))
                if (list && list.length) {
                    list.forEach(ass => div.html(div.html()+'<br>'+formatLineAss(ass)));
                }
            }, {
                pmAssPmUserid: userid
            });
        }
        else $ui.alert("No client id");
    }
    return { render: render };
})(jQuery);
function formatLineAss(ass) {
    return ass.pmAssRole;
  }
  

