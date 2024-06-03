import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idSubTask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);


    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }
}
