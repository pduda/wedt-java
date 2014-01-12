package pl.waw.pduda.data;

import pl.waw.pduda.ErrorException;

public class DatabaseException extends ErrorException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DATABASE_FAILURE =1;
	
	
	public DatabaseException(int type,String mess,boolean isC)
	{
		this.Message=mess;
		
		this.Type=type;
		
		this.IsCritical=isC;
		
	}

	@Override
	public boolean isCritical() {
		// TODO Auto-generated method stub
		return this.IsCritical;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.Message;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return this.Type;
	}

}
