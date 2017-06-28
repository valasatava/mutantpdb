package mmtf.workshop.mutantpdb;

import edu.sdsc.mmtf.spark.mappers.StructureToBioJava;
import mmtf.workshop.mutantpdb.io.DataProvider;
import mmtf.workshop.mutantpdb.mappers.AddResidueToKey;
import mmtf.workshop.mutantpdb.mappers.FilterResidue;
import mmtf.workshop.mutantpdb.utils.SaprkUtils;
import edu.sdsc.mmtf.spark.mappers.StructureToPolymerChains;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import edu.sdsc.mmtf.spark.io.MmtfReader;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.api.StructureDataInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Dataset<Row> mutations = DataProvider.getMutationsToStructures();

        List<Row> broadcasted = mutations.select("pdbId", "chainId", "pdbAtomPos").collectAsList();
        SaprkUtils.stopSparkSession();

        JavaSparkContext sc = SaprkUtils.getSparkContext();
        Broadcast<List<Row>> bcmut = sc.broadcast(broadcasted);

        MmtfReader
                .downloadMmtfFiles(Arrays.asList("1A1U"), sc)
                .flatMapToPair(new StructureToPolymerChains())
                .flatMapToPair(new AddResidueToKey(bcmut))
                .mapValues(new StructureToBioJava())
                .mapToPair(new FilterResidue()).keys()
                .map(t -> t.replace(".", ","))
                .saveAsTextFile("/Users/yana/git/mutantpdb/src/main/resources/pdbResidues.csv");
        sc.close();
    }
}
