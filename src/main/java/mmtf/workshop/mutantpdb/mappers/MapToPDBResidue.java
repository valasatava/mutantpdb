package mmtf.workshop.mutantpdb.mappers;

import mmtf.workshop.mutantpdb.datastructures.PDBResidue;
import org.apache.spark.api.java.function.Function;

import java.util.regex.Pattern;

/**
 * Created by Yana Valasatava on 7/11/17.
 */
public class MapToPDBResidue implements Function<String, PDBResidue> {

    @Override
    public PDBResidue call(String key) {
        PDBResidue residue = new PDBResidue();
        residue.setPdbId(key.split(Pattern.quote("."))[0]);
        residue.setChainId(key.split(Pattern.quote("."))[1]);
        residue.setResNumber(key.split(Pattern.quote("."))[2]);
        residue.setAminoacidCode(key.split(Pattern.quote("."))[3]);
        return residue;
    }
}