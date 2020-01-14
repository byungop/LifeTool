package kr.chirokyel.lifeTool;

public class _1_Note_2_Page_2_Leftslide_1_Form {

    // 객체 변수 선언
    String pageSubject;
    int noteKey;
    int pageKey;
    boolean stateChecked;
    boolean stateVisible;

    // 객체 생성자
    public _1_Note_2_Page_2_Leftslide_1_Form(String pageSubject, int noteKey, int pageKey, boolean stateChecked, boolean statevisible) {
        this.pageSubject = pageSubject;
        this.noteKey = noteKey;
        this.pageKey = pageKey;
        this.stateChecked = stateChecked;
        this.stateVisible = statevisible;
    }

    // 메서드 : 겟터 셋터
    public String getPageSubject() {
        return pageSubject;
    }
    public int getNoteKey() {
        return noteKey;
    }
    public int getPageKey() {
        return pageKey;
    }
    public boolean isStateChecked() {
        return stateChecked;
    }
    public boolean isStateVisible() {
        return stateVisible;
    }

    public void setPageSubject(String pageSubject) {
        this.pageSubject = pageSubject;
    }
    public void setNoteKey(int noteKey) {
        this.noteKey = noteKey;
    }
    public void setPageKey(int pageKey) {
        this.pageKey = pageKey;
    }
    public void setStateChecked(boolean stateChecked) {
        this.stateChecked = stateChecked;
    }
    public void setStateVisible(boolean stateVisible) {
        this.stateVisible = stateVisible;
    }
}