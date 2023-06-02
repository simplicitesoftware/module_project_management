var PmProjectGantt = (function() {
	let updateTsk=JSON.parse('{}');
	let updateVrs=JSON.parse('{}');
	
	function showCurrentDay() {
		
		const currentDay = document.querySelector('.today-highlight')
		currentDay.scrollIntoView({ inline: 'center' })
	}
	function render(params,tasks=[]) {
		
		let app = $ui.getApp();
		
		$('#saveGantt').click(save);
		let grant = app.getGrant();
		lang=(app.getGrant().getLang()=="FRA")?"fr":"en";
		var gantt = new Gantt("#pmprojectgantt", tasks, {
			on_click: function (task) {
				let url=task.url;
				
				window.location.href=url;
			},
			on_date_change: function(task, start, end) {
				if(task.id.slice(0,2)!="V#"){
					if (!updateTsk.hasOwnProperty(task.id)) {
						updateTsk[task.id]=JSON.parse('{}');
					}
					
					updateTsk[task.id]["start"] =app.toDateValue(start);
					updateTsk[task.id]["end"] = app.toDateValue(end);
					

				}else{
					if (!updateVrs.hasOwnProperty(task.id.slice(2))) {
					
						updateVrs[task.id.slice(2)]=JSON.parse('{}');
					}
					updateVrs[task.id.slice(2)]["end"] = app.toDateValue(end);
					
				}
				

			},
			on_progress_change: function(task, progress) {
				
				if(task.id.slice(0,2)!="V#"){
					if (!updateTsk.hasOwnProperty(task.id)) {
					
						updateTsk[task.id]=JSON.parse('{}');
					}
					updateTsk[task.id]["progress"] =progress;
				}
			},
			view_modes: ['Day'],
			bar_height: 20,
			bar_corner_radius: 3,
			arrow_curve: 5,
			padding: 18,
			view_mode: 'Day',
			language: lang, // 'fr', 'es', 'it', 'ru', 'ptBr', 'en', 'tr', 'zh', 'de', 'hu'
			popup_trigger: 'click',
			custom_popup_html: null,
			allow_dragging:grant.hasResponsibility("PM_SUPERADMIN"),
			allow_progress_update:grant.hasResponsibility("PM_SUPERADMIN"),
		});
		gantt.render();
		showCurrentDay();
		
		var div=$("#pmprojectgantt").parents(".tab-pane");
		var observer = new MutationObserver(function (event) {
			if(div.hasClass( "active" )) showCurrentDay();
		  })
		  
		  observer.observe(div.get(0), {
			attributes: true, 
			attributeFilter: ['class'],
			childList: false, 
			characterData: false
		  })

	}
	function save(){
		let app = $ui.getApp();
		var oTsk = app.getBusinessObject("PmTask");
		oTsk.action(function(res) {
			if(res!=[])alert(res);
		}, "PM_UPDATE_GANTT", { values: { "tsk": JSON.stringify(updateTsk) } });
		var oVrs= app.getBusinessObject("PmVersion");
		oVrs.action(function(res) {
			if(res!=[])alert(res);
		}, "PM_UPDATE_GANTT", { values: { "tsk": JSON.stringify(updateVrs) } });
					
	}
	
	return { render: render };
})();
