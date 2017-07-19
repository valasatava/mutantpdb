package mmtf.workshop.mutantpdb;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.not;

/**
 * Created by Yana Valasatava on 6/28/17.
 */
public class MapMutationsToStructures {

    public static void main( String[] args )
    {
        Dataset<Row> mutations = MapMutationsToPDBPositions.run();
        Dataset<Row> pdbresidues = MapPDBResidueToAminoAcid.run();

        Dataset<Row> result = pdbresidues.join(mutations, pdbresidues.col("pdbId").equalTo(mutations.col("pdbId"))
                .and(pdbresidues.col("chainId").equalTo(mutations.col("chainId")))
                .and(pdbresidues.col("resNumber").equalTo(mutations.col("pdbAtomPos"))))
                .drop(pdbresidues.col("pdbId")).drop(pdbresidues.col("chainId")).drop(pdbresidues.col("resNumber"))
                .withColumnRenamed("aminoacidCode", "pdbRef");

        result.filter(not(col("pdbRef").equalTo(col("Ref"))))
                .show(100);
    }
}