package com.samuel.spectrite.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.samuel.spectrite.Spectrite;

public class UpdateCheckThread extends Thread {
	
	public UpdateCheckThread() {
        this.setName(Spectrite.MOD_NAME + " Update Check Thread");
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        try {
        	String currentMcVersion = Spectrite.MC_VERSION;
            URL buildFileUrl = new URL(String.format("https://raw.githubusercontent.com/Samuel-Harbord/SpectriteMod/%s/build.gradle", currentMcVersion));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(buildFileUrl.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	if (inputLine.startsWith("version = ")) {
            		UpdateNotifier.updateVersion = inputLine.substring(11, inputLine.lastIndexOf("\""));
            		break;
            	}
            }
            in.close();

            if (!StringUtils.isEmpty(UpdateNotifier.updateVersion)) {
	            String clientVersionString = Spectrite.VERSION;
	            
	            String[] updateVersionComponents = UpdateNotifier.updateVersion.split("\\.");
	            String[] currentVersionComponents = clientVersionString.split("\\.");
	            int minComponents = Math.min(updateVersionComponents.length, currentVersionComponents.length);
	            
	            for (int i = 0; i < minComponents; i++) {
	            	int updateVersionComponent = Integer.parseInt(updateVersionComponents[i]);
	            	int currentVersionComponent = Integer.parseInt(currentVersionComponents[i]);
	            	if (updateVersionComponent != currentVersionComponent) {
	            		if (updateVersionComponent > currentVersionComponent) {
	            			UpdateNotifier.newVersion = true;
	            		}
	            		break;
	            	} else if (i == minComponents - 1) {
	            		if (currentVersionComponents.length != updateVersionComponents.length && currentVersionComponents.length == minComponents) {
	            			UpdateNotifier.newVersion = true;
	            		}
	            	}
	            }
	            UpdateNotifier.success = true;
            } else {
            	UpdateNotifier.success = false;
            }
        } catch(Exception e) {
            UpdateNotifier.success = false;
            System.err.println(Spectrite.MOD_NAME + " failed to check for updates.");
        }
    }
}
