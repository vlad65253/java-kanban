package manager.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.InteractionsException;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;

public class EpicHandlers extends BaseHttpHandler {
    public EpicHandlers(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        String[] split = exchange.getRequestURI().getPath().split("/");

        try {
            switch (endpoint) {
                case GET:
                    sendText(exchange, gson.toJson(taskManager.getEpicList()));
                    break;
                case GET_BY_ID:
                    sendText(exchange, gson.toJson(taskManager.getEpic(Integer.parseInt(split[2]))));
                    break;
                case GET_SUBS_BY_EPIC_ID:
                    sendText(exchange, gson.toJson(taskManager.getAllSubTaskForEpic(Integer.parseInt(split[2]))));
                case POST:
                    epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                    taskManager.createEpic(epic);
                    writeResponse(exchange, 201, "Задача добавлена");
                    break;
                case POST_BY_ID:
                    epic = gson.fromJson(getTaskFromRequestBody(exchange), Epic.class);
                    taskManager.updateEpic(epic);
                    sendText(exchange, "");
                    break;
                case DELETE_BY_ID:
                    epic = taskManager.getEpic(Integer.parseInt(split[2]));
                    taskManager.delEpicById(epic.getId());
                    writeResponse(exchange, 204, "");
                    break;
                case UNKNOWN:
                    sendNotFound(exchange);
                    break;
            }
        } catch (InteractionsException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            writeResponse(exchange, 500, "Ошибка в эндпоинте");
        }
    }
}