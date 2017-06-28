package mmtf.workshop.mutantpdb.sandbox;

import mmtf.workshop.mutantpdb.io.DataProvider;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.col;

/**
 * Created by Yana Valasatava on 6/28/17.
 */
public class ShowDataset {

    public static void main(String[] args) {

        DataProvider provider = new DataProvider();

        Dataset<Row> df = provider.getMutationsToStructures();
        df.filter(col("pdbId").equalTo("1A1U")).show();


    }
}
