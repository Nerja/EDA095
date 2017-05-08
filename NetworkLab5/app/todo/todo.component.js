(function(app) {
  app.TodoComponent = TodoComponent;

  TodoComponent.parameters = [ app.DataService ];

  TodoComponent.annotations = [
    new ng.core.Component({
      selector: 'todo',
      templateUrl: 'app/todo/todo.component.html',
    })
  ];

  function TodoComponent(dataService) {
    this.dataService = dataService;

    this.todo = {text: "", done:false};

    this.newTodo = function() {
      this.dataService.addTodo(this.todo);
    }

  }
})(window.app = window.app || {});
