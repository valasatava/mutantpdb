package mmtf.workshop.mutantpdb.filters;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Created by Yana Valasatava on 7/14/17.
 */
public class FilterUniprotCanonicalResidue implements IDataframeFilter {

    private Dataset<Row> uniprot;
    public FilterUniprotCanonicalResidue() {
        //uniprot = DataProvider.
    }

    @Override
    public Dataset<Row> filter(Dataset<Row> data) {
        return null;
    }
}
