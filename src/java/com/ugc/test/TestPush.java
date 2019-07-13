package com.ugc.test;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;

public class TestPush
{
    public static void main(String[] args) {


        try {
            String token = "e8b0f899159f5e269f540282b7099040d49ec492d0e66ed57dac56bbb41a42bf";

            Push.alert("Hello World!", "c:\\tmp\\3FF513E1FC2888988C20F78A528F0F41\\push_cert.p12", "China820930", false, token);
      
          //  Push.combined("Hello World", -1, "Glass.aiff", "/Users/wangshuai/Desktop/projects/push/push_cert.p12", "China820930", false, token);

        } catch (CommunicationException e) {
           e.printStackTrace();
        } catch (KeystoreException e) {
           e.printStackTrace();

        }

    }

}
