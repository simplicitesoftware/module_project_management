var PmProjectGantt = (function() {
	function showCurrentDay() {
		console.log("DEBUG IN showCurrentDay")
		const currentDay = document.querySelector('.today-highlight')
		currentDay.scrollIntoView({ inline: 'center' })
	}
	function render(params,tasks=[]) {
		
		app = $ui.getAjax();
		console.log("DEBUG: lang "+app.getGrant().getLang())
		lang=(app.getGrant().getLang()=="FRA")?"fr":"en";
		var gantt = new Gantt("#pmprojectgantt", tasks, {
			view_modes: ['Day'],
			bar_height: 20,
			bar_corner_radius: 3,
			arrow_curve: 5,
			padding: 18,
			view_mode: 'Day',
			language: lang, // 'fr', 'es', 'it', 'ru', 'ptBr', 'en', 'tr', 'zh', 'de', 'hu'
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
	
	return { render: render };
})();
