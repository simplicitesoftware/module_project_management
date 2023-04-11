var PmTimesheetRecapExt = (function($) {
    let app;
	//CRA
    function render(params) {
        var userid =$ui.grant.getUserID();
        try {
			//if (typeof Mustache === 'undefined') throw 'Mustache not available';
            var div=$('#recap')
            var template =$('#pm-ass-template').html();
            if (userid) {
                // Search the client
                app = $ui.getAjax();
                var affect = app.getBusinessObject('PmAssignment');
                
                affect.search(function(list) {
                    div.html(Mustache.render(template,toDict(list)));
                }, {
                    pmAssPmUserid: userid
                });
            }
            else throw 'No client id';
        } catch(e) {
			console.error('Render error: ' + e.message);
		}
    }        
    
    function toDict(list) {
        var lang = app.getGrant().getLang();
        var data ={
            labelRole:'Role',
            labelQuantity:'Quantity',
            labelConsumed:'Consumed',
            listNotEmpty: false,
            ass: []
        };
        if (lang == "FRA"){
            data.labelRole = "Rôle";
            data.labelQuantity = "Droit";
            data.labelConsumed = "Consommée";
        };
        if (list && list.length){
            data.listNotEmpty = true;
            list.forEach(ass => data.ass.push(function(ass){
                var objAss = {
                    pmAssRole: ass.pmAssRole,
                    pmAssConsumed: ass.pmAssConsumed,
                    pmAssQuantity:' ',
                    setProgress:false
                };
                
                if(ass.pmAssQuantity){
                    objAss.setProgress=true;
                    objAss.pmAssQuantity =ass.pmAssQuantity;
                }
                return objAss;
            }(ass)));
            
        };
        return data;
    }
    return { render: render };
})(jQuery);