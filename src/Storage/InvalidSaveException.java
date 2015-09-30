package Storage;

/*
 * Exception class when the file is invalid
 * 
 * @author Priyanka Bhula
 */

@SuppressWarnings("serial")
public class InvalidSaveException extends Exception{	
	public InvalidSaveException(){
		super();
	}
	
	public InvalidSaveException(String msg, Throwable e){
		super(msg,e);
	}
	
}
