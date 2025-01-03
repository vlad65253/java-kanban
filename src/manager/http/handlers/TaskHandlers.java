package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;

public class TaskHandlers extends BaseHttpHandler {
    public TaskHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET:
                    sendText(exchange, gson.toJson(taskManager.getTaskList()));
                    break;
                case GET_BY_ID:
                    sendText(exchange, gson.toJson(taskManager.getTask(Integer.parseInt(split[2]))));
                    break;
                case POST:
                    task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                    taskManager.createTask(task);
                    writeResponse(exchange, 201, "Задача добавлена");
                    break;
                case POST_BY_ID:
                    task = gson.fromJson(getTaskFromRequestBody(exchange), Task.class);
                    taskManager.updateTask(task);
                    sendText(exchange, "");
                    break;
                case DELETE_BY_ID:
                    task = taskManager.getTask(Integer.parseInt(split[2]));
                    taskManager.delTaskById(task.getId());
                    writeResponse(exchange, 204, "");
                    break;
                case UNKNOWN:
                    sendNotFound(exchange);
                    break;
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            writeResponse(exchange, 500, "");
        }
    }
}