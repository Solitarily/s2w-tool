package info.corne.s2wtool;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import android.os.Environment;

public class ApplyChanges extends Thread{
	private Set<ChangesAppliedListener> listeners = new CopyOnWriteArraySet<ChangesAppliedListener>();
	private boolean enabled;
	private boolean getStatus;
	private boolean firstCheck;
	public ApplyChanges(boolean enabled)
	{
		this.enabled = enabled;
		this.getStatus = false;
	}
	public ApplyChanges()
	{
		this.getStatus = true;
	}
	public void setFirstCheck(boolean first)
	{
		this.firstCheck = first;
	}
	@Override
	public void run() {
		String res = "";
		if(this.firstCheck)
		{
			String[] command = {"su", "-c", "ls /sys/android_touch/"};
			String comp = ShellCommand.run(command);
			if(comp.indexOf("sweep2wake") == -1)
			{
				notifyListeners(1);
				return;
			}
		}
		try
		{
			String[] command = {"ls", "-a", Environment.getExternalStorageDirectory().getPath()};
			res = ShellCommand.run(command);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getStackTrace());
		}
		if(getStatus)
		{
			
			if(res.indexOf(".info.corne.TurnS2WOff") == -1)
			{
				notifyListeners(true);
			}
			else
			{
				notifyListeners(false);
			}
		}
		else
		{
			if(this.enabled)
			{
				String[] enableCommand = {"su", "-c", "echo 1 > /sys/android_touch/sweep2wake && " +
						"rm /sdcard/.info.corne.TurnS2WOff"};
				ShellCommand.run(enableCommand);
				notifyListeners(this.enabled);
			}
			else
			{
				String[] disableCommand = {"su", "-c", "echo 0 > /sys/android_touch/sweep2wake && " +
						"touch /sdcard/.info.corne.TurnS2WOff"};
				ShellCommand.run(disableCommand);
				notifyListeners(this.enabled);
			}
		}
	}
	public final void addListener(final ChangesAppliedListener listener)
	{
		listeners.add(listener);
	}
	public final void removeListener(final ChangesAppliedListener listener)
	{
		listeners.remove(listener);
	}
	public final void notifyListeners(boolean enabled)
	{
		for(ChangesAppliedListener listener : listeners)
			listener.changesApplied(this, enabled);
	}
	public final void notifyListeners(int error)
	{
		for(ChangesAppliedListener listener : listeners)
			listener.changesApplied(this, error);
	}
	

}
