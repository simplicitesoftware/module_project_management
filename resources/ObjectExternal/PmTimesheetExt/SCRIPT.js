var PmTimesheetExt = PmTimesheetExt || (function($) {
    let app, ass;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        $ui.displayTimesheet($('#ts'), "PmUser",userid , "PmTimeSheetAssign");
        try {
            if (typeof Mustache === 'undefined') throw 'Mustache not available';
            const div=$('#recap')
            const template =$('#pm-ass-template').html();
            if (userid) {
                // Search the client
                app = app || (params.pub
                    ? new Simplicite.Ajax(params.root, 'api', 'website', 'simplicite') // External
                    : new Simplicite.Ajax(params.root, 'ui'));//Simplicite.Application); // Internal
        
                ass = ass || app.getBusinessObject('PmAssignment');
                ass.search(function(list) {
                    div.html(Mustache.render(template,list))
                }, {
                    pmAssPmUserid: userid
                });
            }
            else throw 'No user id';
		} catch(e) {
			console.error('Render error: ' + e.message);
		}
		
    }
    return { render: render };
})(jQuery);
/* var PmTimesheetExt = PmTimesheetExt || (function($) {
	let app, ass;

	
	function render(params) {
		try {
			if (typeof Mustache === 'undefined') throw 'Mustache not available';
			const div = $('#demomustachefrontend');
			if (!params.pub) div.css('min-height', '1000px');
			
			app = app || (params.pub
				? new Simplicite.Ajax(params.root, 'api', 'website', 'simplicite') // External
				: new Simplicite.Ajax(params.root, 'ui'));//Simplicite.Application); // Internal
	
			prd = prd || app.getBusinessObject('DemoProduct');
			prd.bannerURL = prd.bannerURL || params.bannerURL; // Image banner URL
			prd.toFixed = prd.toFixed || function() { return function(n, r) { return parseFloat(r(n)).toFixed(2); } }; // Rendering function for decimal

			prd.search(function() {
				div.html(Mustache.render($('#demo-template').html(), prd));
			}, null, { inlineDocs: true });
		} catch(e) {
			console.error('Render error: ' + e.message);
		}
	}

	return { render: render };	
})(jQuery); */

