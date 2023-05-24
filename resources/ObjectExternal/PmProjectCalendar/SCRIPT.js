var PmProjectCalendar = (function() {
	function render(params) {
		$('#pmprojectcalendar').append('Hello world!');
		var calendar = new FullCalendar.Calendar($('#pmprojectcalendar'), {
          initialView: 'dayGridMonth'
        });
        calendar.render();
	}

	return { render: render };
})();
