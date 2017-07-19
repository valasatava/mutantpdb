package mmtf.workshop.mutantpdb.dataframes;

import mmtf.workshop.mutantpdb.datastructures.PDBResidue;
import mmtf.workshop.mutantpdb.io.DataProvider;
import mmtf.workshop.mutantpdb.mappers.MapPositionToResidue;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.rcsb.uniprot.auto.Uniprot;
import org.rcsb.uniprot.auto.dao.UniprotDAO;
import org.rcsb.uniprot.auto.dao.UniprotDAOImpl;
import org.rcsb.uniprot.auto.tools.JpaUtilsUniProt;
import org.rcsb.uniprot.isoform.IsoformTools;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yana Valasatava on 7/18/17.
 */
public class CreateUniprotPDBResidueMapping {

    public static Map<String, String> getCanonicalSequences(List<String> uniProtIds) {

        EntityManager upEM = JpaUtilsUniProt.getEntityManager();
        UniprotDAO uniprotDAO = new UniprotDAOImpl();
        IsoformTools isoTools = new IsoformTools();

        Map<String, String> sequences = new HashMap<>();
        for ( String uniprotId : uniProtIds) {
            try {
                Uniprot uniProt = uniprotDAO.getUniProt(upEM, uniprotId);
                ProteinSequence[] uniprotIsoforms = isoTools.getIsoforms(uniProt);
                ProteinSequence canonical = uniprotIsoforms[0];
                sequences.put(uniprotId, canonical.getSequenceAsString());
            } catch (CompoundNotFoundException e) {
                continue;
            }
        }
        return sequences;
    }

    public static void foo() {

        Dataset<Row> uniprotpdb = DataProvider.getUniprotToPdbMapping();
        uniprotpdb.show();

        List<String> uniProtIds = uniprotpdb.select("uniProtId").distinct().toJavaRDD().map(t->t.getString(0)).collect();
        Map<String, String> sequences = getCanonicalSequences(uniProtIds);

        JavaRDD<PDBResidue> residues = uniprotpdb.toJavaRDD().map(new MapPositionToResidue());
        residues.collect();

    }

    public static void main(String[] args) {
        foo();
    }
}
