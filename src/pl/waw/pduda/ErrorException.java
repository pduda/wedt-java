package pl.waw.pduda;

public abstract class ErrorException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean IsCritical;
	
	protected String Message;
	
	protected int Type;
	
	public abstract boolean isCritical();
	
	public abstract String getMessage();
	
	public abstract int getType();
}
