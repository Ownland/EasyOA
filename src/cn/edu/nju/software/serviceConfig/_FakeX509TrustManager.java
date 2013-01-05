package cn.edu.nju.software.serviceConfig;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * ����֤�������
 * 
 * @author sC
 * 
 */
public class _FakeX509TrustManager implements X509TrustManager {

 private static TrustManager[] trustManagers;
 private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

 public boolean isClientTrusted(X509Certificate[] chain) {
  return true;
 }

 public boolean isServerTrusted(X509Certificate[] chain) {
  return true;
 }

 /**
  * ��������SSL���� ���ô˷��������Խ��Not trusted server certificate�쳣
  */
 public static void allowAllSSL() {
  HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

   public boolean verify(String s, SSLSession sslsession) {

    return true;
   }

  });

  SSLContext context = null;
  if (trustManagers == null) {
   trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
  }

  try {
   context = SSLContext.getInstance("TLS");
   context.init(null, trustManagers, new SecureRandom());
  } catch (NoSuchAlgorithmException e) {
   e.printStackTrace();
  } catch (KeyManagementException e) {
   e.printStackTrace();
  }

  HttpsURLConnection.setDefaultSSLSocketFactory(context
    .getSocketFactory());
 }

 public void checkClientTrusted(X509Certificate[] ax509certificate, String s)
   throws java.security.cert.CertificateException {

 }

 public void checkServerTrusted(X509Certificate[] ax509certificate, String s)
   throws java.security.cert.CertificateException {

 }

 public X509Certificate[] getAcceptedIssuers() {
  return _AcceptedIssuers;
 }

 // ~

}


