package cn.edu.nju.software.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.edu.nju.software.dao.EmailDAO;
import cn.edu.nju.software.dao.EmailDAOImpl;
import cn.edu.nju.software.enums.EmailType;
import cn.edu.nju.software.model.Email;
import cn.edu.nju.software.model.EmailList;
import cn.edu.nju.software.service.MailService;
import cn.edu.nju.software.ui.R;
import cn.edu.nju.software.utils.AppContext;



public class MailServiceImpl implements MailService{
	private Context context;
	public MailServiceImpl(Context context){
		this.context = context;
	}
	

	//鎺ュ彈閭欢  
    public  EmailList getMail(int pageIndex,int pageSize,boolean networkAvailable,EmailType type,boolean isRefresh){  
    	
        final String host = AppContext.EMAIL_RECIEVE_HOST;  
        final String username = AppContext.EMAIL_ADDRESS;  
        final String password = AppContext.EMAIL_PASSWORD;  
  

        EmailDAO emailDAO = new EmailDAOImpl(context);
        int localMax = emailDAO.findMaxNumber(type);
        int localMin = emailDAO.findMinNumber(type);
        EmailList emailList = new EmailList();
      
        if(networkAvailable&&isRefresh){
	        // 鍒涘缓Properties 瀵硅薄  
	        Properties props = new Properties();  
	        // 鍒涘缓閭欢浼氳瘽  
	        Session session = Session.getDefaultInstance(props, new Authenticator(){  
	            @Override  
	            public PasswordAuthentication getPasswordAuthentication() {  
	                return new PasswordAuthentication(username, password);  
	            }  
	              
	        });  
	        try {  
	            
	            Store store = session.getStore("imap");  
	            store.connect(host, username, password);  
	  
	            String folderType = "INBOX";
	            if(type==EmailType.OUTBOXMAIL){
	            	folderType="SENT ITEMS";
	            }
	            
	            Folder folder = store.getFolder(folderType);  
	            folder.open(Folder.READ_ONLY);  //鎵撳紑锛屾墦寮�悗鎵嶈兘璇诲彇閭欢淇℃伅  
	            // 鑾峰彇閭欢娑堟伅  
	           int total = folder.getMessageCount(); 
	           List<Email> list = new ArrayList<Email>();
	            for (int i=pageSize*pageIndex+1; i<=total&&i<=pageSize*(pageIndex+1); i++) {  
	            	Email email = new Email();
	            	if(total-i+1>localMax||total-i+1<localMin){
		            	Message message = folder.getMessage(total-i+1);
		            	email.setId(message.getMessageNumber());
		            	email.setDate(message.getSentDate());
		            	email.setSender(((InternetAddress)message.getFrom()[0]).getAddress());
		            	email.setReciever(((InternetAddress)message.getAllRecipients()[0]).getAddress());
		            	email.setTitle(message.getSubject());
		            	email.setType(type);
		            	String content = "";
		            	if(message.getContent() instanceof String){
		              	   content = (String)message.getContent();
			            }else{
			            	Multipart multipart = (Multipart) message.getContent();
			            	for (int j = 0,m= multipart.getCount(); j < m; j++) { 
			            		  Part part = multipart.getBodyPart(j);
			            		  if (part.getContent() instanceof Multipart) { 
				                       Multipart p = (Multipart) part.getContent();
				                       for (int k = 0; k < p.getCount(); k++) { 
				                    	   if(p.getBodyPart(k).getContentType().startsWith("text/html")){
				                    		   content = (String)p.getBodyPart(k).getContent();
				                    		   break;
				                    	   }
				                       } 
				                 }else{
				                	   if(part.getContent() instanceof InputStream){
				                		   BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)part.getContent()));
				                		   String oneLine = "";
				                		   while((oneLine = reader.readLine())!=null){
				                			   content+=oneLine;
				                		   }
				                	   }else{
				                		   if(part.getContent() instanceof MimeMessage){
				                			  MimeMessage mimeMessage =  (MimeMessage)part.getContent();
				                			  content = (String)mimeMessage.getContent();
				                		   }else{
				                			   content = (String)part.getContent();
				                		   }
				                	   }
				              
				                   }
				               }
		            		}
		                email.setContent(content);
		                emailDAO.insert(email);
	            	}else{
	            		email = emailDAO.findEmailByNumber(total-i+1,type);
	            	}
	                list.add(email);
	             }  
	  
	            // 鍏抽棴璧勬簮  
	            folder.close(false);  
	            store.close();  
	            
	            emailList.setPageSize(list.size());
	            emailList.setEmailsList(list);
	            return emailList;
	              
	        } catch (MessagingException e) {  
	            e.printStackTrace();  
	            return null;
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}  
        }else{
        	  List<Email> list = new ArrayList<Email>();
        	  if(localMax!=0){
        		  Log.e("pageIndex1",pageIndex+"");
        		  Log.e("localMax",localMax+"");
        		  Log.e("localMin",localMin+"");
	        	 for (int i=pageSize*pageIndex+1; i<=localMax-localMin+1&&i<=pageSize*(pageIndex+1); i++) {  
		            	Email	email = emailDAO.findEmailByNumber(localMax-localMin-i+2,type);
		            	list.add(email);
		         }  
        	  }
        	  emailList.setPageSize(list.size());
		      emailList.setEmailsList(list);
	         return emailList;
        }
  
    }  
    public void sendEmail(Email email) {

    	 final String host = AppContext.EMAIL_SEND_HOST;  
         final String username = AppContext.EMAIL_ADDRESS;  
         final String password = AppContext.EMAIL_PASSWORD;  
		 Properties props = new Properties();
		 props.put("mail.smtp.host", host);  
	     Session session = Session.getInstance(props, null);  
		 String msgBody = email.getContent();
		 try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(email.getSender()));
			msg.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getReciever()));
			Multipart mainPart = new MimeMultipart();   
		    BodyPart html = new MimeBodyPart();   
		    html.setContent(msgBody, "text/html; charset=utf-8");   
		    mainPart.addBodyPart(html);   
		    msg.setContent(mainPart); 
			msg.setSubject(email.getTitle());
			Transport transport = session.getTransport("smtp");  
	        transport.connect((String) props.get("mail.smtp.host"), username,password);
	        transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
		} catch (AddressException e) {
				e.printStackTrace();
		} catch (MessagingException e) {
				e.printStackTrace();
		}
		;
	}


	@Override
	public void deleteEmail(Email email,boolean networkAvailable) {
		// TODO Auto-generated method stub
		 final String host = AppContext.EMAIL_RECIEVE_HOST;  
	     final String username = AppContext.EMAIL_ADDRESS;  
	     final String password = AppContext.EMAIL_PASSWORD;  
		 int number = email.getId();
		 if(networkAvailable){
			 	Properties props = new Properties();  
		        // 鍒涘缓閭欢浼氳瘽  
		        Session session = Session.getDefaultInstance(props, new Authenticator(){  
		            @Override  
		            public PasswordAuthentication getPasswordAuthentication() {  
		                return new PasswordAuthentication(username, password);  
		            }  
		        });  
		       
		        Store store;
				try {
					store = session.getStore("imap");
					store.connect(host, username, password);  
			        String folderType = "INBOX";
			        if(email.getType()==EmailType.OUTBOXMAIL){
			         	folderType="SENT ITEMS";
			        }
			        Folder folder = store.getFolder(folderType);  
			        folder.open(Folder.READ_ONLY);  //鎵撳紑锛屾墦寮�悗鎵嶈兘璇诲彇閭欢淇℃伅  
			        Message message = folder.getMessage(number);
			        message.setFlag(Flags.Flag.DELETED, true);  
			        message.saveChanges(); 
			        folder.close(false);  
		            store.close();  
				} catch (NoSuchProviderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		        
		 }
		  
	}
}
