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
    this.todo = {id: -1, text: "", done: false};
    this.dataService = dataService;
    route.params.subscribe((params) => {
      fetchedTodo = dataService.getTODOs()[params['id']];
      if(fetchedTodo) {
        this.todo.text = fetchedTodo.text;
        this.todo.done = fetchedTodo.done;
        this.todo.id = params['id'];
      }
    });

    this.newTodo = function () {
      dataService.addTodo(this.todo);
    }

    this.editTodo = function() {
      dataService.editTodo(this.todo);
    }

    this.deleteTodo = function() {
      dataService.deleteTodo(this.todo);
    }
  }
})(window.app = window.app || {})
