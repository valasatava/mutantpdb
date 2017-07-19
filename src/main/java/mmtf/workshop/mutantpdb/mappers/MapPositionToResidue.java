package mmtf.workshop.mutantpdb.mappers;

import mmtf.workshop.mutantpdb.datastructures.PDBResidue;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Row;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.rcsb.uniprot.auto.Uniprot;
import org.rcsb.uniprot.auto.dao.UniprotDAO;
import org.rcsb.uniprot.auto.dao.UniprotDAOImpl;
import org.rcsb.uniprot.auto.tools.JpaUtilsUniProt;
import org.rcsb.uniprot.isoform.IsoformTools;

import javax.persistence.EntityManager;

/**
 * Created by Yana Valasatava on 7/18/17.
 */
public class MapPositionToResidue implements Function<Row, PDBResidue> {

    @Override
    public PDBResidue call(Row row) {

        PDBResidue residue = new PDBResidue();

        String uniprotId = row.getAs("uniProtId");
        residue.setUniprotId(uniprotId);

        int uniprotResNumber = row.getAs("uniProtPos");
        residue.setUniprotResNumber(uniprotResNumber);

        //TODO: uniprot leter

        EntityManager upEM = JpaUtilsUniProt.getEntityManager();
        UniprotDAO uniprotDAO = new UniprotDAOImpl();

        Uniprot uniProt = uniprotDAO.getUniProt(upEM, uniprotId);
        IsoformTools isoTools = new IsoformTools();

        ProteinSequence[] uniprotIsoforms=null;
        try {
            uniprotIsoforms = isoTools.getIsoforms(uniProt);
        } catch (CompoundNotFoundException e) {
            e.printStackTrace();
        }

        ProteinSequence canonical = uniprotIsoforms[0];
        String base = canonical.getAsList().get(uniprotResNumber - 1).getBase();

        String pdbId = row.getAs("pdbId");
        residue.setPdbId(pdbId);

        String chainId = row.getAs("chainId");
        residue.setChainId(chainId);

        int pdbResNumber = Integer.valueOf(row.getAs("pdbAtomPos"));
        residue.setPdbResNumber(pdbResNumber);

        //TODO: pdb leter

        return residue;
    }
}
