var PmTimesheetRecapExt = PmTimesheetRecapExt || (function($) {
    let app;
	//CRA
    function render(params) {
        console.log("DEBUG render PmTimesheetRecapExt ---------------------------------------------------------");
        var userid =$ui.grant.getUserID();
        try {
			//if (typeof Mustache === 'undefined') throw 'Mustache not available';
            var div=$('#recap');
            var template =$('#pm-ass-template').html();
            var $divtmp = $('<div>');
            $divtmp.load(getResourceURL("HTML", "JS", "PmTimesheetRecapExt", null)+" #pm-ass-template");
            console.log(getResourceURL("HTML", "JS", "PmTimesheetRecapExt", null)+" #pm-ass-template");
            template=$divtmp.html();
            console.log(template);
            template='{{#listNotEmpty}}<div class="container"><div class="table"><div class="table-content">	<div class="table-header"><div class="header__item">{{labelRole}}</div><div class="header__item ">{{labelQuantity}}</div><div class="header__item">{{labelConsumed}}</div><div class="header__item"></div></div>{{#ass}}<div class="table-row">		<div class="table-data ">{{pmAssRole}}</div><div class="table-data ">{{pmAssQuantity}}</div><div class="table-data ">{{pmAssConsumed}}</div><div class="table-data ">{{#setProgress}}<progress value="{{pmAssConsumed}}" max="{{pmAssQuantity}}"></progress>{{/setProgress}}</div></div>{{/ass}}</div>	</div></div>{{/listNotEmpty}}';
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
