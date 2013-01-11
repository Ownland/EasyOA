package cn.edu.nju.software.enums;

public enum DocumentType {
	DOC(1),FOLDER(2);
	
	private int value = 0;
	private DocumentType(int value) { 
        this.value = value;
    }
	public int value(){
		return this.value;
	}
	public static DocumentType valueOf(int value) {   
        switch (value) {
        case 1:
            return DOC;
        case 2:
            return FOLDER;
        default:
            return null;
        }
    }
}
