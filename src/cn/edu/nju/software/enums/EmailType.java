package cn.edu.nju.software.enums;

public enum EmailType {
	INBOXMAIL(1),OUTBOXMAIL(2);
	
	private int value = 0;
	private EmailType(int value) { 
        this.value = value;
    }
	public int value(){
		return this.value;
	}
	public static EmailType valueOf(int value) {   
        switch (value) {
        case 1:
            return INBOXMAIL;
        case 2:
            return OUTBOXMAIL;
        default:
            return null;
        }
    }

}
