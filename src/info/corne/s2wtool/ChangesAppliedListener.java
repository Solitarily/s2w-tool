package info.corne.s2wtool;

public interface ChangesAppliedListener {
	void changesApplied(final Thread thread, final boolean enabled);
	void changesApplied(final Thread thread, final int error);
}
