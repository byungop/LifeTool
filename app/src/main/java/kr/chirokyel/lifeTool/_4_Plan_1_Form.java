package kr.chirokyel.lifeTool;

public class _4_Plan_1_Form {

    private String start;
    private String end;
    private String plan;
    private String color;


    public _4_Plan_1_Form(String start, String end, String plan, String color) {
        this.start = start;
        this.end = end;
        this.plan = plan;
        this.color = color;
    }


    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }
    public String getPlan() {
        return plan;
    }
    public String getColor() {
        return color;
    }

    public void setStart(String start) {
        this.start = start;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public void setPlan(String plan) {
        this.plan = plan;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
