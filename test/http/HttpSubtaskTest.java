package http;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.http.HttpTaskServer;
import manager.http.token.SubtaskTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpSubtaskTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private static final String BASE_URL = "http://localhost:8080/subtasks";

    @BeforeEach
    void setUp() {
        manager.delAllTask();
        manager.delAllSubTask();
        manager.delAllEpic();
        taskServer.start();
        Epic epic = new Epic("Test 2", "Testing epic 2");
        manager.createEpic(epic);
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask(2, "subtask1", TaskStatus.NEW, "description1",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15), 1);
        SubTask subtask2 = new SubTask(3, "subtask2", TaskStatus.NEW, "description2",
                LocalDateTime.of(2024, 8, 25, 20, 40), Duration.ofMinutes(15), 1);
        SubTask subtask3 = new SubTask(4, "subtask3", TaskStatus.NEW, "description3",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15), 1);

        manager.createSubTask(subtask1);
        manager.createSubTask(subtask2);
        manager.createSubTask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> fromManager = manager.getSubTaskList();
        List<SubTask> fromHttp = gson.fromJson(response.body(), new SubtaskTypeToken().getType());

        assertEquals(fromManager.size(), fromHttp.size());
        assertEquals(fromManager.get(0), fromHttp.get(0));
        assertEquals(fromManager.get(1), fromHttp.get(1));
        assertEquals(fromManager.get(2), fromHttp.get(2));
    }

    @Test
    void getSubtaskById() throws IOException, InterruptedException {
        SubTask subtask3 = new SubTask(4, "subtask3", TaskStatus.NEW, "description3",
                LocalDateTime.of(2024, 8, 26, 20, 40), Duration.ofMinutes(15), 1);
        manager.createSubTask(subtask3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask3.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subtaskByHttp = gson.fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(subtask3, subtaskByHttp);
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        SubTask subtask = new SubTask("Test 2", "Testing subtask 2", 1);
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<SubTask> tasksFromManager = manager.getSubTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Testing subtask 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void testUpdateSubtask() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask(2, "subtask1", TaskStatus.NEW, "description1",
                LocalDateTime.of(2024, 8, 24, 20, 40), Duration.ofMinutes(15), 1);
        SubTask newSubtask1 = new SubTask(2, "subtask2", TaskStatus.NEW, "description2",
                LocalDateTime.of(2024, 8, 25, 10, 40), Duration.ofMinutes(15), 1);
        manager.createSubTask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        String subtaskJson = gson.toJson(newSubtask1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(newSubtask1, manager.getSubtask(2), "Задача не совпадает.");
    }

    @Test
    void testStatusCode406() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask(2, "subtask1", TaskStatus.NEW, "description1",
                LocalDateTime.of(2024, 8, 24, 15, 30), Duration.ofMinutes(15), 1);
        SubTask newSubtasks2 = new SubTask(3, "subtask2", TaskStatus.IN_PROGRESS, "description2",
                LocalDateTime.of(2024, 8, 24, 15, 30), Duration.ofMinutes(15), 1);
        manager.createSubTask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        String taskJson = gson.toJson(newSubtasks2);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        SubTask subtask1 = new SubTask("subtask1", "description1", 1);
        manager.createSubTask(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/" + subtask1.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getSubTaskList().size());
    }

    @Test
    void deleteTaskStatus404() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(BASE_URL + "/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}