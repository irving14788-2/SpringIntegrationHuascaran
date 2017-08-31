
package  com.bbva.integration.exceptions;

public class BaseException extends Exception {

    private static final long serialVersionUID = 3186105543822592034L;

    private Exception objException;
    private String codError;
    private String msjError;
    private String nombreSP;
    private String nombreBD;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public BaseException() {
        super();
    }

    public BaseException(Exception objException) {
        this.objException = objException;
    }

    public BaseException(String msjError) {
        super(msjError);
        this.msjError = msjError;
    }

    public BaseException(String msjError, Exception objException) {
        super(objException);
        this.objException = objException;
        this.msjError = msjError;
    }

    public BaseException(String codError, String msjError,
                         Exception objException) {
        super(msjError);
        this.codError = codError;
        this.msjError = msjError;
        this.objException = objException;
    }

    public BaseException(String codError, String msjError, String nombreSP,
                         String nombreBD, Exception objException) {
        super(msjError);
        this.codError = codError;
        this.msjError = msjError;
        this.nombreSP = nombreSP;
        this.nombreBD = nombreBD;
        this.objException = objException;
    }

    public BaseException(String codError, String msjError, String nombreWS,
                         Exception objException) {
        super(msjError);
        this.codError = codError;
        this.msjError = msjError;
        this.objException = objException;
    }

    public Exception getObjException() {
        return objException;
    }

    public void setObjException(Exception objException) {
        this.objException = objException;
    }

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }

    public String getMsjError() {
        return msjError;
    }

    public void setMsjError(String msjError) {
        this.msjError = msjError;
    }

    public String getNombreSP() {
        return nombreSP;
    }

    public void setNombreSP(String nombreSP) {
        this.nombreSP = nombreSP;
    }

    public String getNombreBD() {
        return nombreBD;
    }

    public void setNombreBD(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}

