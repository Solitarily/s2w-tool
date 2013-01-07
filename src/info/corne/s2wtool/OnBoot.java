package info.corne.s2wtool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class OnBoot extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			String[] command = {"ls", "-a", Environment.getExternalStorageDirectory().getPath()};
			String res = ShellCommand.run(command);
			if(res.indexOf(".info.corne.TurnS2WOff") != -1)
			{
				String[] disableCommand = {"su", "-c", "echo 0 > /sys/android_touch/sweep2wake"};
				ShellCommand.run(disableCommand);
			}			
		}	
	}
}
