package mmtf.workshop.mutantpdb;

import mmtf.workshop.mutantpdb.io.DataLocationProvider;
import mmtf.workshop.mutantpdb.io.DataProvider;
import mmtf.workshop.mutantpdb.mappers.MapToSingleLetterCode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.lit;

/**
 * Created by Yana Valasatava on 7/19/17.
 */
public class GetMutationsPresentInPDB {

    public static void run() 
    {
        // list of input mutations
        Dataset<Row> oncoKBMutations = DataProvider.getOncoKBMutations();

        // all pdb structures with mismatches with respect to the UniProt sequence
        Dataset<Row> pdbMismatches = MapToSingleLetterCode.map(DataProvider.getUniprotToPdbMismatches());

        //
        Dataset<Row> mutations = pdbMismatches.join(oncoKBMutations, pdbMismatches.col("uniprotId").equalTo(oncoKBMutations.col("Uniprot"))
                .and(pdbMismatches.col("uniprotResNum").equalTo(oncoKBMutations.col("Position")))
                .and(pdbMismatches.col("uniprotLetter").equalTo(oncoKBMutations.col("Ref")))
                .and(pdbMismatches.col("pdbLetter").equalTo(oncoKBMutations.col("Varaint"))))
                .drop(oncoKBMutations.col("Uniprot")).drop(oncoKBMutations.col("Position")).drop(oncoKBMutations.col("Ref"))
                .withColumnRenamed("Gene", "gene").withColumnRenamed("Varaint", "mutation")
                .withColumn("type", lit("mutant"))
                .select("gene","uniprotId","uniprotResNum","uniprotLetter","pdbId","chainId","pdbResNum","pdbLetter","type","details");

        Dataset<Row> uniprotPdb = DataProvider.getUniprotToPdbMapping();
        Dataset<Row> uniprotWild = DataProvider.getUniprotToPdbWildType();

        Dataset<Row> tmp = uniprotWild.join(mutations, uniprotWild.col("uniprotId").equalTo(mutations.col("uniprotId")))
                .select(mutations.col("gene"), uniprotWild.col("uniprotId"), mutations.col("uniprotResNum"),
                        mutations.col("uniprotLetter"), uniprotWild.col("pdbId")).distinct();

        Dataset<Row> whildtype = tmp.join(uniprotPdb, tmp.col("uniprotId").equalTo(uniprotPdb.col("uniProtId"))
                                                .and(tmp.col("uniprotResNum").equalTo(uniprotPdb.col("uniProtPos")))
                                                .and(tmp.col("pdbId").equalTo(uniprotPdb.col("pdbId"))))
                .drop(uniprotPdb.col("uniProtId")).drop(uniprotPdb.col("uniProtPos"))
                .drop(uniprotPdb.col("pdbId")).drop(uniprotPdb.col("insCode"))
                .withColumnRenamed("pdbAtomPos", "pdbResNum").distinct()
                .withColumn("pdbLetter", col("uniprotLetter"))
                .withColumn("type", lit("wild")).withColumn("details", lit("-"));

        Dataset<Row> report = mutations.union(whildtype).sort("uniprotId", "uniprotResNum", "type").cache();
        report.show(500);
        report.write().mode(SaveMode.Overwrite).csv(DataLocationProvider.getOncoKBMutationsInPDBRResultsLocation());
    }

    public static void main(String[] args) {
        run();
    }
}
