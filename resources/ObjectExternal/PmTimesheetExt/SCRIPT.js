var PmTimesheetExt = (function() {
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        try {
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
			var div = $('#recap');
			app = $ui.getAjax();
	
			var affect = app.getBusinessObject('PmAssignment'),
                pmAssPmUserid = userid;
				affect.search(function(rows) {
					div.html(affect);
				}, {
					pmAssPmUserid: pmAssPmUserid
				}, {error: function(err) {console.error(err);}}
					
			);
			
		} catch(e) {
			console.error('Render error: ' + e.message);
		}
    }
    return { render: render };
})();