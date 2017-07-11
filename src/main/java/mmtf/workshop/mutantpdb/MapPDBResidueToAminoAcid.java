package mmtf.workshop.mutantpdb;

import edu.sdsc.mmtf.spark.io.MmtfReader;
import edu.sdsc.mmtf.spark.mappers.StructureToBioJava;
import edu.sdsc.mmtf.spark.mappers.StructureToPolymerChains;
import mmtf.workshop.mutantpdb.datastructures.PDBResidue;
import mmtf.workshop.mutantpdb.io.DataProvider;
import mmtf.workshop.mutantpdb.mappers.AddResidueToKey;
import mmtf.workshop.mutantpdb.mappers.FilterResidue;
import mmtf.workshop.mutantpdb.mappers.MapToPDBResidue;
import mmtf.workshop.mutantpdb.utils.SparkUtils;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.spark.sql.functions.col;

/**
 *
 *
 */
public class MapPDBResidueToAminoAcid
{
    private static final Logger logger = LoggerFactory.getLogger(MapPDBResidueToAminoAcid.class);

    public static Dataset<Row> run()
    {
        Dataset<Row> mutations = DataProvider.getMutationsToStructures();

        List<String> pdbIds = mutations.select(col("pdbId")).distinct()
                .toJavaRDD().map(t -> t.getString(0)).collect();

        List<Row> broadcasted = mutations.select("pdbId", "chainId", "pdbAtomPos").collectAsList();

        SparkSession session = SparkUtils.getSparkSession();
        SparkContext sc = session.sparkContext();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);
        Broadcast<List<Row>> bcmut = jsc.broadcast(broadcasted);

        JavaRDD<PDBResidue> residues = MmtfReader.readSequenceFile("/pdb/2017/full", pdbIds, jsc)
                .flatMapToPair(new StructureToPolymerChains())
                .flatMapToPair(new AddResidueToKey(bcmut))
                .mapValues(new StructureToBioJava())
                .mapToPair(new FilterResidue())
                .filter(t -> t._2 != null)
                .keys().map(new MapToPDBResidue());

        Dataset<Row> df = SparkUtils.getSparkSession().createDataFrame(residues, PDBResidue.class);
        logger.info("PDB residue numbers were mapped to aminoacid codes");

        return df;
    }

    public static void main( String[] args )
    {
        Dataset<Row> df = run();
        df.show();
    }
}
