package kr.chirokyel.lifeTool;

public class _5_Todo_1_Form {

    // 객체 변수 선언
    private String todo;
    String settingTime;
    boolean stateVisible;
    private int todoKey;


    // 메서드 : 겟터 셋터
    public String getTodo() {
        return todo;
    }
    public String getSettingTime() {
        return settingTime;
    }
    public boolean isStateVisible() {
        return stateVisible;
    }
    public int getTodoKey() {
        return todoKey;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }
    public void setSettingTime(String settingTime) {
        this.settingTime = settingTime;
    }
    public void setStateVisible(boolean stateVisible) {
        this.stateVisible = stateVisible;
    }
    public void setTodoKey(int todoKey) {
        this.todoKey = todoKey;
    }


}
