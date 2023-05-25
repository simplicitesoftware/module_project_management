var PmProjectGantt = (function() {
	function render(params) {
		$('#pmprojectgantt').append('Hello world!');
		var tasks = [
			  {
			    id: 'Task 1',
			    name: 'Redesign website',
			    start: '2016-12-28',
			    end: '2016-12-31',
			    progress: 20,
			    dependencies: 'Task 2, Task 3',
			  }
			]
		var gantt = new Gantt("#pmprojectgantt", tasks);
	}

	return { render: render };
})();
