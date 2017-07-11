package mmtf.workshop.mutantpdb.datastructures;

import java.io.Serializable;

/**
 * Created by Yana Valasatava on 7/11/17.
 */
public class PDBResidue implements Serializable {

    private String pdbId;
    private String chainId;
    private String resNumber;
    private String aminoacidCode;

    public String getPdbId() {
        return pdbId;
    }

    public void setPdbId(String pdbId) {
        this.pdbId = pdbId;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getResNumber() {
        return resNumber;
    }

    public void setResNumber(String resNumber) {
        this.resNumber = resNumber;
    }

    public String getAminoacidCode() {
        return aminoacidCode;
    }

    public void setAminoacidCode(String aminoacidCode) {
        this.aminoacidCode = aminoacidCode;
    }
}
