import manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    InMemoryTaskManagerTest() {
        taskManager = new InMemoryTaskManager();
    }
}