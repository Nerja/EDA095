(function(app) {
  app.TodoListComponent = TodoListComponent;
  TodoListComponent.parameters = [app.DataService];
  TodoListComponent.annotations = [
    new ng.core.Component({
        selector: 'todo-list',
        templateUrl: 'app/todo.list/todo.list.component.html',
    })
  ];

  function TodoListComponent(dataService) {
    this.myList = dataService.getTODOs();
  }
})(window.app = window.app || {});

<!--

-->
