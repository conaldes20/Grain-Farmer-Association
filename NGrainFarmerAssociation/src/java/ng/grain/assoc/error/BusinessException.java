/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.error;

/**
 *
 * @author CONALDES
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class BusinessException extends RuntimeException
{

	public BusinessException()
	{
	}

	public BusinessException(String msg)
	{
		super(msg);
	}

	public BusinessException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public BusinessException(Throwable cause)
	{
		super(cause);
	}

}