
package com.bbva.integration.exceptions;

public class DBException extends BaseException {

    private static final long serialVersionUID = -4938556712560828963L;

    //Constructores ...

    public DBException(Exception objException) {
        super(objException);
    }
    
    public DBException(RuntimeException objException) {
        super(objException);
    }

    public DBException(String msjError) {
        super(msjError);
    }

    public DBException(String codError, String msjError,
                       Exception objException) {
        super(codError, msjError, objException);
    }

    public DBException(String codError, String msjError, String nombreSP,
                       String nombreBD, Exception objException) {
        super(codError, msjError, nombreSP, nombreBD, objException);
    }

}

