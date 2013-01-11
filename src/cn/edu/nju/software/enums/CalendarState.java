package cn.edu.nju.software.enums;

public enum CalendarState {
	TODO(1),DONE(2);
	private int value = 0;
	private CalendarState(int value) { 
        this.value = value;
    }
	public int value(){
		return this.value;
	}
	public static CalendarState valueOf(int value) {   
        switch (value) {
        case 1:
            return TODO;
        case 2:
            return DONE;
        default:
            return null;
        }
    }
	
}
