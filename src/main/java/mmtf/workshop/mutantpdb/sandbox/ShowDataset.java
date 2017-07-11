package mmtf.workshop.mutantpdb.sandbox;

import mmtf.workshop.mutantpdb.utils.SparkUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yana Valasatava on 6/28/17.
 */
public class ShowDataset {

    private static final Logger logger = LoggerFactory.getLogger(ShowDataset.class);

    public static void main(String[] args)
    {
        Dataset<Row>  df = SparkUtils.getSparkSession().read().parquet("/Users/yana/spark/parquet/uniprotpdb/20170710/");
        df.cache();
        df.show();
        logger.info("Number of rows: "+df.count());
    }
}