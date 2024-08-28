import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest<F extends InMemoryTaskManager> {

    @Test
    void getDefault() {
        assertNotNull(Managers.getDefault(), "TaskManager не найден");
    }

    @Test
    void getDefaultHistory() {
        assertNotNull(Managers.getDefaultHistory(), "HistoryManager не найден");
    }
}