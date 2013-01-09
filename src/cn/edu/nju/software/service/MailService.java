package cn.edu.nju.software.service;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.model.EmailList;



public interface MailService {
	//接受邮件  
    public  EmailList getMail(int pageIndex,int pageSize,boolean networkAvailable,EmailType type,boolean isRefresh);
    public void sendEmail(Email email);
    public void deleteEmail(Email email,boolean networkAvailable);
}
