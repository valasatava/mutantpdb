package mmtf.workshop.mutantpdb.datastructures;

import java.io.Serializable;

/**
 * Created by Yana Valasatava on 7/18/17.
 */
public class UniprotResidue implements Serializable {

    private String uniprotId;
    private int uniprotResNumber;
    private String uniprotResLetter;

    public String getUniprotId() {
        return uniprotId;
    }

    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    public int getUniprotResNumber() {
        return uniprotResNumber;
    }

    public void setUniprotResNumber(int uniprotResNumber) {
        this.uniprotResNumber = uniprotResNumber;
    }

    public String getUniprotResLetter() {
        return uniprotResLetter;
    }

    public void setUniprotResLetter(String uniprotResLetter) {
        this.uniprotResLetter = uniprotResLetter;
    }
}
