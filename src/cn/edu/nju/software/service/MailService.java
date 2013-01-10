package cn.edu.nju.software.service;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.model.EmailList;



public interface MailService {
    public  EmailList getMail(int pageIndex,int pageSize,EmailType type,boolean isRefresh);
    public void sendEmail(Email email);
    public void deleteEmail(Email email);
    public String getEmailContent(Email email);
}
