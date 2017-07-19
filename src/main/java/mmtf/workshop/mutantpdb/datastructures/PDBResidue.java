package mmtf.workshop.mutantpdb.datastructures;

import java.io.Serializable;

/**
 * Created by Yana Valasatava on 7/11/17.
 */
public class PDBResidue extends UniprotResidue implements Serializable {

    private String pdbId;
    private String chainId;
    private int pdbResNumber;
    private String insCode;
    private String pdbResLetter;

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

    public int getPdbResNumber() {
        return pdbResNumber;
    }

    public void setPdbResNumber(int pdbResNumber) {
        this.pdbResNumber = pdbResNumber;
    }

    public String getInsCode() {
        return insCode;
    }

    public void setInsCode(String insCode) {
        this.insCode = insCode;
    }

    public String getPdbResLetter() {
        return pdbResLetter;
    }

    public void setPdbResLetter(String pdbResLetter) {
        this.pdbResLetter = pdbResLetter;
    }

}
