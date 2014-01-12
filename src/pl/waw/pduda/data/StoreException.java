package pl.waw.pduda.data;

import pl.waw.pduda.ErrorException;

public class StoreException extends ErrorException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int IMAGE_FAILURE =1;
	
	public static final int FILE_NOT_FOUND_FAILURE =2;
	
	
	public StoreException(int type,String mess,boolean isC)
	{
		this.Message=mess;
		
		this.Type=type;
		
		this.IsCritical=isC;
		
	}
	@Override
	public String getMessage()
	{
		return this.Message;
	}
	@Override
	public int getType()
	{
		return this.Type;
	}
	@Override
	public boolean isCritical() {
		// TODO Auto-generated method stub
		return this.IsCritical;
	}

}
