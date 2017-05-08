(function(app) {
  app.TodoComponent = TodoComponent;

  TodoComponent.parameters = [ ng.router.ActivatedRoute, app.DataService ];

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
      tmp = dataService.getTODOs()[params['id']];
      if(tmp){ this.todo = tmp; }
    });

    this.newTodo = function() {
      this.dataService.addTodo(this.todo);
    }

    this.updateTodo = function() {
      this.dataService.updateTodo(this.todo);
    }
  }

})(window.app = window.app || {});
