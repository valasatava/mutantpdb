package mmtf.workshop.mutantpdb;

import mmtf.workshop.mutantpdb.io.DataLocationProvider;
import mmtf.workshop.mutantpdb.io.DataProvider;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.col;

/**
 * Created by Yana Valasatava on 7/10/17.
 */
public class MapMutationsToPDBPositions {

    private static final Logger logger = LoggerFactory.getLogger(MapMutationsToPDBPositions.class);

    public static Dataset<Row> run()
    {
        Dataset<Row> mutations = DataProvider.getOncoKBMutations();

        //canonical = FilterUniprotCanonicalResidue(mutations);

        Dataset<Row> uniprotpdbmapping = DataProvider.getUniprotToPdbMapping();
        Dataset<Row> mutationsmapping = mutations.join(uniprotpdbmapping, mutations.col("Uniprot").equalTo(uniprotpdbmapping.col("uniProtId"))
                .and(mutations.col("Position").equalTo(uniprotpdbmapping.col("uniProtPos"))))
                .drop("Uniprot").drop("Position")
                .select(col("Gene"), col("uniProtId"), col("uniProtPos"), col("Ref"), col("Varaint"),
                        col("pdbId"), col("chainId"), col("pdbAtomPos"), col("insCode"));
        logger.info("Mutation are mappend to PDB structures");

        return mutationsmapping;
    }

    public static void writeAsParquetFile(Dataset<Row> mutationsmapping)
    {
        mutationsmapping.write().mode(SaveMode.Overwrite).parquet(DataLocationProvider.getMutationsMappingLocation());
    }

    public static void main(String[] args)
    {
        Dataset<Row> mutationsmapping = run();
        writeAsParquetFile(mutationsmapping);
    }
}