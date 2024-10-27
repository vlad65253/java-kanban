package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.ManagerSaveException;
import manager.TaskManager;
import tasks.SubTask;

import java.io.IOException;

public class SubtaskHandlers extends BaseHttpHandler {
    public SubtaskHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET:
                    sendText(exchange, gson.toJson(taskManager.getSubTaskList()));
                    break;
                case GET_BY_ID:
                    sendText(exchange, gson.toJson(taskManager.getSubtask(Integer.parseInt(split[2]))));
                    break;
                case POST:
                    subtask = gson.fromJson(getTaskFromRequestBody(exchange), SubTask.class);
                    taskManager.createSubTask(subtask);
                    writeResponse(exchange, 201, "Задача добавлена");
                    break;
                case POST_BY_ID:
                    subtask = gson.fromJson(getTaskFromRequestBody(exchange), SubTask.class);
                    taskManager.updateSubTask(subtask);
                    sendText(exchange, "");
                    break;
                case DELETE_BY_ID:
                    subtask = taskManager.getSubtask(Integer.parseInt(split[2]));
                    taskManager.delSubTaskById(subtask.getId());
                    writeResponse(exchange, 204, "");
                    break;
                case UNKNOWN:
                    sendNotFound(exchange);
                    break;
            }
        } catch (NullPointerException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            writeResponse(exchange, 500, "");
        }
    }
}