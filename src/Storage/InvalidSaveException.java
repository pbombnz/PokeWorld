package Storage;

/*
 * Exception class when the file is invalid
 * 
 * @author Priyanka Bhula
 */

public class InvalidSaveException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public InvalidSaveException(){
		super();
	}
	
	public InvalidSaveException(String msg, Throwable e){
		super(msg,e);
	}
	
}
