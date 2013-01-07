package info.corne.s2wtool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements ChangesAppliedListener {

	public boolean firstRun = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ApplyChanges applyChanges = new ApplyChanges();
		applyChanges.addListener(this);
		applyChanges.setFirstCheck(firstRun);
		applyChanges.start(); 
		firstRun = false;
	}
	public void changesApplied(Thread thread, final boolean enabled)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton1);
				ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
				pb.setVisibility(View.INVISIBLE);
				tb.setEnabled((boolean) true);
				tb.setChecked(enabled);
				
			}
		});
		
		System.out.println("Changes Applied");
	}
	public void changesApplied(Thread thread, final int error)
	{
		final Context context = this;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, context.getResources().getString(R.string.no_sweep2wake), Toast.LENGTH_LONG).show();
				ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton1);
				tb.setVisibility(View.INVISIBLE);
			}
		});
		
	}

	public void applySweep2Wake(View view)
	{
		final ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton1);
		ApplyChanges applyChanges = new ApplyChanges((boolean) tb.isChecked());
		applyChanges.addListener(this);
		applyChanges.start();
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				
				ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
				pb.setVisibility(View.VISIBLE);
				tb.setEnabled(false);
			}
		});
	}

}
