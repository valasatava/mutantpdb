package mmtf.workshop.mutantpdb;

import mmtf.workshop.mutantpdb.io.DataProvider;
import mmtf.workshop.mutantpdb.mappers.MapToSingleLetterCode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Created by Yana Valasatava on 7/19/17.
 */
public class GetMutationsPresentInPDB {

    public static void run() 
    {
        Dataset<Row> df = DataProvider.getUniprotToPdbMismatches();
        Dataset<Row> pdb = MapToSingleLetterCode.map(df);
        // pdb.show();

        Dataset<Row> input = DataProvider.getOncoKBMutations();
        //input.show();

        Dataset<Row> mutations = pdb.join(input, pdb.col("uniprotId").equalTo(input.col("Uniprot"))
                .and(pdb.col("uniprotResNum").equalTo(input.col("Position")))
                .and(pdb.col("uniprotLetter").equalTo(input.col("Ref")))
                .and(pdb.col("pdbLetter").equalTo(input.col("Varaint"))))
                .drop(input.col("Uniprot")).drop(input.col("Position")).drop(input.col("Ref"))
                .withColumnRenamed("Gene", "gene").withColumnRenamed("Varaint", "mutation")
                .select("gene","uniprotId","uniprotResNum","uniprotLetter","pdbId","chainId","pdbResNum","pdbLetter","details");
        mutations.show();
    }

    public static void main(String[] args) {
        run();
    }
}
