# java-kanban
Трекер помогает эффективно организовать совместную работу над задачами. Это учебный проект, при выполнении которого я 
изучил: 
* Java Core 
* Git
* GitHub
* принципы ООП 
* Unit - тесты
* Алгоритмы и структуры данных
* Работу с файлами
* Функциональный стиль и его инструменты
* JSON
* Библиотеку Gson

### Типы задач:
* Task - отдельно стоящая задача
* Epic - большая задача, которая состоит из подзадач
* Subtask - подзадачи, которые являются частями Epic

### Особенность:
* Для хранения истории просмотров задач реализован собственный двусвязный список, в котором удаление задачи из 
произвольного места происходит за O(1)

### API:
| URL                  | HTTP - метод | Описание                                    |
|----------------------|--------------|---------------------------------------------|
| /tasks               | GET          | Получение всех задач                        |
| /tasks/{id}          | GET          | Получение задачи по id                      |
| /tasks               | POST         | Создание / Обновление задачи                |
| /tasks/{id}          | DELETE       | Удаление задачи по id                       |
| /subtasks            | GET          | Получение всех подзадач                     |
| /subtasks/{id}       | GET          | Получение подзадачи по id                   |
| /subtasks            | POST         | Создание / Обновление подзадачи             |
| /subtasks/{id}       | DELETE       | Удаление подзадачи по id                    |
| /epics               | GET          | Получение всех эпиков                       |
| /epics/{id}          | GET          | Получения эпика по id                       |
| /epics/{id}/subtasks | GET          | Получение подзадач конкретного эпика        |
| /epics               | POST         | Создание эпика                              |
| /epics/{id}          | DELETE       | Удаление эпика по id                        |
| /history             | GET          | Получение истории просмотров задач          |
| /prioritized         | GET          | Получение приоритетных задач                |
