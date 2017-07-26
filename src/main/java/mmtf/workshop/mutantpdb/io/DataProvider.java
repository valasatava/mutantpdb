package mmtf.workshop.mutantpdb.io;

import mmtf.workshop.mutantpdb.utils.SparkUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.col;

/**
 * Created by Yana Valasatava on 6/28/17.
 */
public class DataProvider {

    public static Dataset<Row> getOncoKBMutations()
    {
        DataLocationProvider p = new DataLocationProvider();
        Dataset<Row>  df = SparkUtils.readCSV(p.getOncoKBFileLocation(), "\t")
                .select("Gene", "Uniprot", "Ref", "Position", "Varaint");
        return df;
    }

    public static Dataset<Row> getUniprotToPdbMapping()
    {
        Dataset<Row>  df = SparkUtils.readParquet(DataLocationProvider.getUniprotToPdbMappingLocation());
        return df;
    }

    public static Dataset<Row> getUniprotToPdbMismatches()
    {
        Dataset<Row>  df = SparkUtils.readParquet(DataLocationProvider.getUniprotToPdbMismatchesLocation());
        return df;
    }

    public static Dataset<Row> getUniprotToPdbWildType()
    {
        Dataset<Row>  df = SparkUtils.readParquet(DataLocationProvider.getUniprotToPdbWildTypeLocation());
        return df;
    }

    public static Dataset<Row> getMutationsToStructures()
    {
        Dataset<Row>  df = SparkUtils.getSparkSession().read()
                .parquet(DataLocationProvider.getMutationsMappingLocation());
        return df;
    }

    public static void main(String[] args) {
        Dataset<Row> df = getUniprotToPdbWildType();
        df.filter(col("uniprotId").equalTo("O60674")).show();
    }
}
