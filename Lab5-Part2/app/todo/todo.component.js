(function(app) {
  app.TodoComponent = TodoComponent;
  TodoComponent.parameters = [ng.router.ActivatedRoute, app.DataService];
  TodoComponent.annotations = [
    new ng.core.Component({
      selector: 'todo',
      templateUrl: 'app/todo/todo.component.html',
    })
  ];

  function TodoComponent(route, dataService) {
    this.todo = {text: "", done: false};
    this.dataService = dataService;
    route.params.subscribe((params) => {
      fetchedTodo = dataService.getTODOs()[params['id']];
      if(fetchedTodo) {
        this.todo = fetchedTodo;
      }
    });
    this.newTodo = function () {
      dataService.addTodo(this.todo);
    }
  }
})(window.app = window.app || {})
